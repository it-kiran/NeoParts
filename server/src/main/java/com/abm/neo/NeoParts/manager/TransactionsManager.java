package com.abm.neo.NeoParts.manager;

import com.abm.neo.NeoParts.dto.CustomerDao;
import com.abm.neo.NeoParts.entity.*;
import com.abm.neo.NeoParts.repository.*;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;


/**
 * Created by apatel2 on 5/18/17.
 */
@Component
public class TransactionsManager {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TransactionLineItemRepository transactionLineItemRepository;

    @Autowired
    private WebTransactionLineItemRepository webTransactionLineItemRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private StoreSetupRepository storeSetupRepository;

    @Autowired
    private PaymentRepository paymentRepository;

//    @Autowired
//    private Utility utility;
//
//    @Autowired
//    private ProductRepository productRepository;

    @Autowired
    private CustomerManager customerManager;

    private BaseFont bfBold;
    private BaseFont bf;
    private int pageNumber = 0;


    public TransactionDao addTransaction(List<WebTransactionLineItemDao> webTransactionLineItemDaos) {

        List<TransactionLineItemDao> transactionLineItemList = new ArrayList<>();
        TransactionDao transactionDao = new TransactionDao();

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        if(null !=webTransactionLineItemDaos && webTransactionLineItemDaos.size() > 0){

            String currentDate = dtf.format(now);
            int totalQuantity = 0;
            double totalAmount = 0.00;
            String customerName;
            String customerPhoneNo = null;

            for(WebTransactionLineItemDao lineItem: webTransactionLineItemDaos){

                TransactionLineItemDao transactionLineItemDao = new TransactionLineItemDao();

                transactionLineItemDao.setProductId(lineItem.getProductId());
                transactionLineItemDao.setProductNo(lineItem.getProductNo());
                transactionLineItemDao.setDescription(lineItem.getDescription());
                transactionLineItemDao.setDate(currentDate);
                transactionLineItemDao.setRetail(lineItem.getRetail());
                transactionLineItemDao.setRetailWithDiscount(lineItem.getRetail());
                transactionLineItemDao.setSaleQuantity(lineItem.getSaleQuantity());
                transactionLineItemDao.setTotalProductPrice(lineItem.getRetail() * lineItem.getSaleQuantity());
                transactionLineItemDao.setStatus(lineItem.getStatus());
                
                totalQuantity = +totalQuantity +lineItem.getSaleQuantity();
                totalAmount =+ totalAmount +lineItem.getRetail();
                
                customerPhoneNo = lineItem.getCustomerPhoneNo();
                
                transactionLineItemList.add(transactionLineItemDao);
            }

             CustomerDao customerDao = customerRepository.findByPhoneNo(customerPhoneNo);
            
            // Now set transactionDao
            transactionDao.setCustomerPhoneno(customerPhoneNo);
            if(null != customerDao)
            {
                transactionDao.setCustomerFirstLastName(customerDao.getName());
            }
            transactionDao.setDate(currentDate);
            transactionDao.setStatus("Online");
            transactionDao.setSubtotal(totalAmount);
            transactionDao.setTotalAmount(totalAmount);
            transactionDao.setQuantity(totalQuantity);
            transactionDao.setTransactionLineItemDaoList(transactionLineItemList);
            
            transactionDao =  transactionRepository.save(transactionDao);

            // This mean Online order successfully inserted into main transaction table, so i need to delete line item from web line item table.
            if(null != transactionDao && transactionDao.getTransactionComId() > 0) {
                webTransactionLineItemRepository.deleteAllByCustomerPhoneNo(transactionDao.getCustomerPhoneno());

                // TODO also need to send an email to customer with order details.
            }

            
        }
        
        return transactionDao;
    }

    public boolean sendEmail(int receiptId) throws DocumentException {

        TransactionDao transactionDao = getTransactionById(receiptId);
        boolean response = false;
        if (null != transactionDao && transactionDao.getCustomerPhoneno().length() > 1 && null != transactionDao.getTransactionLineItemDaoList() && null != transactionDao.getPaymentDao()) {
            //First get customer details to send an email.
            CustomerDao customerDao;
            customerDao = customerRepository.findByPhoneNo(transactionDao.getCustomerPhoneno());

            if (null != customerDao && null != customerDao.getEmail() && null != transactionDao.getStoreSetupDao() && null != transactionDao.getStoreSetupDao().getEmail() && null != transactionDao.getStoreSetupDao().getEmailPassword()) {

                String from = transactionDao.getStoreSetupDao().getEmail();
                String to = customerDao.getEmail();

                String newline = System.getProperty("line.separator");
                String content = "Dear " + transactionDao.getCustomerFirstLastName() + newline
                        + newline
                        + newline
                        + "Thank you for shopping with us, We appreciate your business." + newline
                        + "Please find attachment for your order details."
                        + newline
                        + newline
                        + newline
                        + newline
                        + "Thank You" + newline
                        + transactionDao.getStoreSetupDao().getName();


                String subject = transactionDao.getStoreSetupDao().getName() + " ORDER DETAILS";
                final String password = transactionDao.getStoreSetupDao().getEmailPassword();

                Properties props = new Properties();
                props.setProperty("mail.transport.protocol", "smtp");
                props.setProperty("mail.host", "smtp.gmail.com");
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.port", "465");
                props.put("mail.debug", "true");
                props.put("mail.smtp.socketFactory.port", "465");
                props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                props.put("mail.smtp.socketFactory.fallback", "false");
                Session session = Session.getDefaultInstance(props,
                        new javax.mail.Authenticator() {
                            protected PasswordAuthentication getPasswordAuthentication() {
                                return new PasswordAuthentication(from, password);
                            }
                        });
                ByteArrayOutputStream outputStream = null;

                try {
                    //construct the text body part
                    MimeBodyPart textBodyPart = new MimeBodyPart();
                    textBodyPart.setText(content);

                    //now write the PDF content to the output stream
                    outputStream = new ByteArrayOutputStream();
                    byte[] bytes = getA4Receipt(receiptId);

                    //construct the pdf body part
                    DataSource dataSource = new ByteArrayDataSource(bytes, "application/pdf");
                    MimeBodyPart pdfBodyPart = new MimeBodyPart();
                    pdfBodyPart.setDataHandler(new DataHandler(dataSource));
                    pdfBodyPart.setFileName("Invoice.pdf");

                    //construct the mime multi part
                    MimeMultipart mimeMultipart = new MimeMultipart();
                    mimeMultipart.addBodyPart(textBodyPart);
                    mimeMultipart.addBodyPart(pdfBodyPart);

                    Transport transport = session.getTransport();
                    InternetAddress addressFrom = new InternetAddress(from);

                    MimeMessage message = new MimeMessage(session);

                    message.setSender(addressFrom);
                    message.setSubject(subject);
                    message.setContent(mimeMultipart);
                    message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

                    transport.connect();
                    Transport.send(message);
                    transport.close();
                    response = true;

                    System.out.println("sent from " + to +
                            ", to " + to +
                            "; server = " + from + ", port = " + from);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    response = false;
                } finally {
                    //clean off
                    if (null != outputStream) {
                        try {
                            outputStream.close();
                            outputStream = null;
                        } catch (Exception ex) {
                            response = false;
                        }
                    }
                }

            }

        }

        return response;

    }
    public TransactionDao getTransactionById(int transactionCompId) {

        List<PaymentDao> paymentDaoList = new ArrayList<>();
        // just basic java logic to get the details from db
        TransactionDao transactionDao = transactionRepository.findOne(transactionCompId);

        transactionDao.setStoreSetupDao(storeSetupRepository.findOne(1));

        List<TransactionLineItemDao> transactionLineItemDaoList = new ArrayList<>();

        for (TransactionLineItemDao lineItem : transactionDao.getTransactionLineItemDaoList()) {

            if (null == lineItem.getDescription() || lineItem.getDescription().length() < 1) {
                ProductDao productDao = productRepository.findOneByProductId(lineItem.getProductId());

                if (null != productDao) {
                    lineItem.setDescription(productDao.getDescription());
                    transactionLineItemDaoList.add(lineItem);
                }

            } else {
                transactionLineItemDaoList.add(lineItem);
            }

        }

        transactionDao.setTransactionLineItemDaoList(transactionLineItemDaoList);
        List<Object[]> result = paymentRepository.getPaymentDetailsByTransactionId(transactionDao.getTransactionComId());
        if (null != result) {
            for (Object[] j : result) {
                PaymentDao paymentDao = new PaymentDao();

                paymentDao.setTransactionPaymentId((Integer) j[0]);
                paymentDao.setTransactionComId((Integer) j[1]);
                paymentDao.setStatus(j[2].toString());
                paymentDao.setDate(j[3].toString());
                paymentDao.setType(j[4].toString());
                paymentDao.setAmount(Double.parseDouble(j[5].toString()));

                paymentDaoList.add(paymentDao);
            }
        }
        transactionDao.setPaymentDao(paymentDaoList);
        return transactionDao;
    }

    public byte[] getA4Receipt(int receiptNo) throws DocumentException, IOException, com.itextpdf.text.DocumentException {

        TransactionDao transactionDao;

        Document doc = new Document(PageSize.A4);
        initializeFonts();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        PdfWriter writer = PdfWriter.getInstance(doc, byteArrayOutputStream);

        doc.open();
        PdfContentByte cb = writer.getDirectContent();
        transactionDao = getTransactionById(receiptNo);

        if (null != transactionDao) {

            //printCustomerDetailsTest(cb, transactionDao);
            printStoreDetailsTest(transactionDao, doc);
            printFooterNotes(writer);
            //printTransactionDetailsTest(doc, transactionDao);
            //generateLineItemTable(cb);
        }
        doc.close();

        return byteArrayOutputStream.toByteArray();
    }

    private void printFooterNotes(PdfWriter writer){

        PdfPTable table = new PdfPTable(1);
        table.setTotalWidth(523);
        PdfPCell cell = new PdfPCell(new Phrase("***** ALL SALES ARE FINAL NO EXCHANGE AND NO RETURN ACCEPTED *****",FontFactory.getFont(FontFactory.HELVETICA, 8, Font.NORMAL)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorder(0);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);

        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Any item returned its need to be in original packaging and only eligible for exchange and store credit as long as there is no physical damage and discontinued by carriers. If you prefer a store credit you will be refunded at the current sale price of the item or your purchase price(which is lower) and also 15% Restocking fess apply after 15 days period.",FontFactory.getFont(FontFactory.HELVETICA, 8, Font.NORMAL) ));
        cell.setBorder(0);

        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(cell);


        cell = new PdfPCell(new Phrase("***** NO PHYSICAL DAMAGE COVERED *****",FontFactory.getFont(FontFactory.HELVETICA, 8, Font.NORMAL)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorder(0);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);


        table.addCell(cell);

        FooterTable event = new FooterTable(table);
        writer.setPageEvent(event);
    }

    private void printStoreDetailsTest(TransactionDao transactionDao, Document doc) throws IOException, DocumentException, com.itextpdf.text.DocumentException {

        PdfPTable storeTable = new PdfPTable(2);
        PdfPTable customerTable = new PdfPTable(2);
        PdfPTable lineItemTable = new PdfPTable(6);
        PdfPTable paymentTable = new PdfPTable(2);
        PdfPTable paymentMethod = new PdfPTable(4);
        PdfPTable totalDueAmount = new PdfPTable(2);


        String[] header = new String[]{"PRODUCT NO", "DESCRIPTION", "QTY", "RETAIL","RETWTDIS","AMOUNT"};
        String[] paymentHeader = new String[]{"PAYMENT METHOD", "AMOUNT", "DATE", "TIME"};


        storeTable.setWidthPercentage(100);
        customerTable.setWidthPercentage(100);
        lineItemTable.setWidthPercentage(100);
        paymentTable.setWidthPercentage(100);
        paymentMethod.setWidthPercentage(100);
        totalDueAmount.setWidthPercentage(100);


        PdfPCell logo = new PdfPCell();
        PdfPCell invoiceDetails = new PdfPCell();

        PdfPCell storeDetails = new PdfPCell();
        PdfPCell customerDetails = new PdfPCell();

        PdfPCell paymentType = new PdfPCell();
        PdfPCell paymentAmount = new PdfPCell();

//        PdfPCell paymentMethod1 = new PdfPCell();
//        PdfPCell amount = new PdfPCell();
//        PdfPCell date = new PdfPCell();
//        PdfPCell time = new PdfPCell();

        PdfPCell totalBalanceDue = new PdfPCell();
        PdfPCell totalBalanceDueAmount = new PdfPCell();

        if (null != transactionDao) {

            if (null != transactionDao.getStoreSetupDao()) {
                // Image companyLogo = Image.getInstance("C:\\Users\\MK THE PHONE STORE\\Desktop\\MK LOGO.png");

                try {

                    if (null != transactionDao.getStoreSetupDao().getLogo()) {
                        Image companyLogo = Image.getInstance(transactionDao.getStoreSetupDao().getLogo());
                        logo.addElement(companyLogo);
                        logo.setPadding(0);
                        logo.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
                        logo.setBorder(PdfPCell.NO_BORDER);
                    }
                }
                catch (Exception e){
                    System.out.println("Exception ===> Can not find logo for receipt"+e);
                }

            }

            DateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            Date d1 = null;
            try {
                d1 = f.parse(transactionDao.getDate());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            DateFormat transDate = new SimpleDateFormat("MM-dd-yyyy");//NEED TO CHECK THIS
            DateFormat transTime = new SimpleDateFormat("hh:mm a");

            Paragraph paragraph = new Paragraph("INVOICE #:" + transactionDao.getTransactionComId());
            paragraph.setAlignment(PdfPCell.ALIGN_RIGHT);
            Paragraph paragraph1 = new Paragraph("DATE: " + transDate.format(d1) + " - " + transTime.format(d1));
            paragraph1.setAlignment(PdfPCell.ALIGN_RIGHT);
            Paragraph paragraph2 = new Paragraph("CREATED BY: " + transactionDao.getUsername());
            paragraph2.setAlignment(PdfPCell.ALIGN_RIGHT);

            invoiceDetails.addElement(paragraph);
            invoiceDetails.addElement(paragraph1);
            invoiceDetails.addElement(paragraph2);
            invoiceDetails.setBorder(PdfPCell.NO_BORDER);

            storeTable.addCell(logo);
            storeTable.addCell(invoiceDetails);

            if (null != transactionDao.getStoreSetupDao()) {
                Paragraph storeName = new Paragraph(transactionDao.getStoreSetupDao().getName());
                storeName.setAlignment(PdfPCell.ALIGN_LEFT);
                Paragraph street = new Paragraph(transactionDao.getStoreSetupDao().getStreet());
                street.setAlignment(PdfPCell.ALIGN_LEFT);
                Paragraph city = new Paragraph(transactionDao.getStoreSetupDao().getCity() + " , " + transactionDao.getStoreSetupDao().getState() + " , " + transactionDao.getStoreSetupDao().getZipcode());
                city.setAlignment(PdfPCell.ALIGN_LEFT);
                Paragraph phoneNo = new Paragraph(transactionDao.getStoreSetupDao().getPhoneNo());
                phoneNo.setAlignment(PdfPCell.ALIGN_LEFT);
                Paragraph email = new Paragraph(transactionDao.getStoreSetupDao().getEmail());
                email.setAlignment(PdfPCell.ALIGN_LEFT);

                storeDetails.addElement(storeName);
                storeDetails.addElement(street);
                storeDetails.addElement(city);
                storeDetails.addElement(phoneNo);
                storeDetails.addElement(email);
                storeDetails.setBorder(PdfPCell.NO_BORDER);
            }

            if (null != transactionDao.getCustomerPhoneno()) {
                String phoneNo = transactionDao.getCustomerPhoneno();
                CustomerDao customerDao = customerManager.getCustomerByPhoneNo(phoneNo);

                if (null != customerDao) {

                    Paragraph companyName = new Paragraph(customerDao.getCompanyName());
                    companyName.setAlignment(PdfPCell.ALIGN_RIGHT);
                    Paragraph customerName = new Paragraph(transactionDao.getCustomerFirstLastName());
                    customerName.setAlignment(PdfPCell.ALIGN_RIGHT);
                    Paragraph custStreet = new Paragraph(customerDao.getStreet());
                    custStreet.setAlignment(PdfPCell.ALIGN_RIGHT);
                    Paragraph custCity = new Paragraph(customerDao.getCity() + " , " + customerDao.getState() + " , " + customerDao.getZipCode());
                    custCity.setAlignment(PdfPCell.ALIGN_RIGHT);
                    Paragraph custPhone = new Paragraph(customerDao.getPhoneNo());
                    custPhone.setAlignment(PdfPCell.ALIGN_RIGHT);

                    customerDetails.addElement(companyName);
                    customerDetails.addElement(customerName);
                    customerDetails.addElement(custStreet);
                    customerDetails.addElement(custCity);
                    customerDetails.addElement(custPhone);
                    customerDetails.setBorder(PdfPCell.NO_BORDER);

                    customerTable.addCell(storeDetails);
                    customerTable.addCell(customerDetails);

                    customerTable.setSpacingBefore(25);

                }
            }

            if (null != transactionDao.getTransactionLineItemDaoList()) {

                lineItemTable.setHeaderRows(1);
                lineItemTable.setWidths(new float[]{2.3f, 7.5f, 1.1f, 1.1f, 1.5f, 1.6f});
                lineItemTable.setSpacingBefore(15);
                lineItemTable.setSplitLate(false);

                for (String columnHeader : header) {
                    PdfPCell headerCell = new PdfPCell();
                    headerCell.addElement(new Phrase(columnHeader, FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLD)));
                    headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    headerCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    headerCell.setBorderColor(BaseColor.LIGHT_GRAY);
                    headerCell.setPadding(4);
                    lineItemTable.addCell(headerCell);
                }

                for (TransactionLineItemDao lineItem : transactionDao.getTransactionLineItemDaoList()) {

                    PdfPCell cell1 = new PdfPCell();
                    PdfPCell cell2 = new PdfPCell();
                    PdfPCell cell3 = new PdfPCell();
                    PdfPCell cell4 = new PdfPCell();
                    PdfPCell cell5 = new PdfPCell();
                    PdfPCell cell6 = new PdfPCell();


                    cell1.addElement(new Phrase(lineItem.getProductNo(), FontFactory.getFont(FontFactory.HELVETICA, 8, Font.NORMAL)));
                    cell2.addElement(new Phrase(lineItem.getDescription(), FontFactory.getFont(FontFactory.HELVETICA, 8, Font.NORMAL)));
                    cell3.setCellEvent(new PositionEvent(new Phrase(10, String.valueOf(lineItem.getSaleQuantity()), FontFactory.getFont(FontFactory.HELVETICA, 8, Font.NORMAL)), 0.5f, 0.5f, Element.ALIGN_CENTER));
                    cell4.setCellEvent(new PositionEvent(new Phrase(10, String.valueOf(lineItem.getRetail()), FontFactory.getFont(FontFactory.HELVETICA, 8, Font.NORMAL)), 0.5f, 0.5f, Element.ALIGN_CENTER));
                    cell5.setCellEvent(new PositionEvent(new Phrase(10, String.valueOf(lineItem.getRetailWithDiscount()), FontFactory.getFont(FontFactory.HELVETICA, 8, Font.NORMAL)), 0.5f, 0.5f, Element.ALIGN_CENTER));
                    cell6.setCellEvent(new PositionEvent(new Phrase(10, String.valueOf(lineItem.getTotalProductPrice()), FontFactory.getFont(FontFactory.HELVETICA, 8, Font.NORMAL)), 0.5f, 0.5f, Element.ALIGN_CENTER));

//                    cell3.addElement(new Phrase(String.valueOf(lineItem.getSaleQuantity()),FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL)));
//                    cell4.addElement(new Phrase(String.valueOf(lineItem.getRetailWithDiscount()),FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL)));
//                    cell5.addElement(new Phrase(String.valueOf(lineItem.getTotalProductPrice()),FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL)));

                    cell1.setBorderColor(BaseColor.LIGHT_GRAY);
                    cell2.setBorderColor(BaseColor.LIGHT_GRAY);
                    cell3.setBorderColor(BaseColor.LIGHT_GRAY);
                    cell4.setBorderColor(BaseColor.LIGHT_GRAY);
                    cell5.setBorderColor(BaseColor.LIGHT_GRAY);
                    cell6.setBorderColor(BaseColor.LIGHT_GRAY);


                    lineItemTable.addCell(cell1);
                    lineItemTable.addCell(cell2);
                    lineItemTable.addCell(cell3);
                    lineItemTable.addCell(cell4);
                    lineItemTable.addCell(cell5);
                    lineItemTable.addCell(cell6);

                }

            }

            Paragraph subtotal = new Paragraph("SUBTOTAL", FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL));
            subtotal.setAlignment(PdfPCell.ALIGN_LEFT);
            Paragraph discount = new Paragraph("DISCOUNT", FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL));
            discount.setAlignment(PdfPCell.ALIGN_LEFT);
            Paragraph tax = new Paragraph("TAX", FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL));
            tax.setAlignment(PdfPCell.ALIGN_LEFT);
            Paragraph shipping = new Paragraph("SHIPPING", FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL));
            shipping.setAlignment(PdfPCell.ALIGN_LEFT);
            Paragraph quantity = new Paragraph("QUANTITY", FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL));
            quantity.setAlignment(PdfPCell.ALIGN_LEFT);
            Paragraph total = new Paragraph("TOTAL", FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL));
            total.setAlignment(PdfPCell.ALIGN_LEFT);
            Paragraph balanceDue = new Paragraph("BALANCE DUE", FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD));
            balanceDue.setAlignment(PdfPCell.ALIGN_LEFT);

            paymentType.addElement(subtotal);
            paymentType.addElement(discount);
            paymentType.addElement(tax);
            if (transactionDao.getShipping() > 0) {
                paymentType.addElement(shipping);
            }
            paymentType.addElement(quantity);

            paymentType.addElement(total);
            paymentType.addElement(balanceDue);

            paymentType.setBorder(PdfPCell.NO_BORDER);


            Paragraph subtotal1 = new Paragraph("$ " + transactionDao.getSubtotal(), FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL));
            subtotal1.setAlignment(PdfPCell.ALIGN_RIGHT);
            Paragraph discount1 = new Paragraph("$ " + transactionDao.getTotalDiscount(), FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL));
            discount1.setAlignment(PdfPCell.ALIGN_RIGHT);
            Paragraph tax1 = new Paragraph("$ " + transactionDao.getTax(), FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL));
            tax1.setAlignment(PdfPCell.ALIGN_RIGHT);
            Paragraph shipping1 = new Paragraph(String.valueOf(transactionDao.getShipping()), FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL));
            shipping1.setAlignment(PdfPCell.ALIGN_RIGHT);
            Paragraph quantity1 = new Paragraph(String.valueOf(transactionDao.getQuantity()), FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL));
            quantity1.setAlignment(PdfPCell.ALIGN_RIGHT);
            Paragraph total1 = new Paragraph("$ " + transactionDao.getTotalAmount(), FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL));
            total1.setAlignment(PdfPCell.ALIGN_RIGHT);
            Paragraph balanceDue1 = new Paragraph("$ " + transactionDao.getTransactionBalance(), FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD));
            balanceDue1.setAlignment(PdfPCell.ALIGN_RIGHT);

            paymentAmount.addElement(subtotal1);
            paymentAmount.addElement(discount1);
            paymentAmount.addElement(tax1);
            if (transactionDao.getShipping() > 0) {
                paymentAmount.addElement(shipping1);
            }
            paymentAmount.addElement(quantity1);

            paymentAmount.addElement(total1);
            paymentAmount.addElement(balanceDue1);


            paymentAmount.setBorder(PdfPCell.NO_BORDER);

            paymentTable.addCell(paymentType);
            paymentTable.addCell(paymentAmount);


            paymentTable.setSpacingBefore(10);

            if (null != transactionDao.getPaymentDao()) {

                for (String payment : paymentHeader) {
                    PdfPCell headerCell = new PdfPCell();
                    Paragraph paragraph3 = new Paragraph(payment, FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD));
                    paragraph3.setAlignment(Element.ALIGN_CENTER);
                    headerCell.addElement(paragraph3);
                    headerCell.setBorderColor(BaseColor.LIGHT_GRAY);
                    headerCell.setPadding(8);
                    paymentMethod.addCell(headerCell);
                }


                for (PaymentDao payment : transactionDao.getPaymentDao()) {

                    if (!payment.getStatus().equalsIgnoreCase("Void")) {

                        try {
                            d1 = f.parse(payment.getDate());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        DateFormat payDate = new SimpleDateFormat("MM-dd-yyyy");//NEED TO CHECK THIS
                        DateFormat payTime = new SimpleDateFormat("hh:mm a");

                        PdfPCell cell1 = new PdfPCell();
                        PdfPCell cell2 = new PdfPCell();
                        PdfPCell cell3 = new PdfPCell();
                        PdfPCell cell4 = new PdfPCell();

                        cell1.setFixedHeight(30);
                        cell2.setFixedHeight(30);
                        cell3.setFixedHeight(30);
                        cell4.setFixedHeight(30);

                        cell1.setCellEvent(new PositionEvent(new Phrase(10, payment.getType(), FontFactory.getFont(FontFactory.HELVETICA, 12, Font.NORMAL)), 0.5f, 0.5f, Element.ALIGN_CENTER));
                        cell2.setCellEvent(new PositionEvent(new Phrase(10, "$ " + String.valueOf(payment.getAmount()), FontFactory.getFont(FontFactory.HELVETICA, 12, Font.NORMAL)), 0.5f, 0.5f, Element.ALIGN_CENTER));
                        cell3.setCellEvent(new PositionEvent(new Phrase(10, String.valueOf(payDate.format(d1)), FontFactory.getFont(FontFactory.HELVETICA, 12, Font.NORMAL)), 0.5f, 0.5f, Element.ALIGN_CENTER));
                        cell4.setCellEvent(new PositionEvent(new Phrase(10, payTime.format(d1), FontFactory.getFont(FontFactory.HELVETICA, 12, Font.NORMAL)), 0.5f, 0.5f, Element.ALIGN_CENTER));

                        cell1.setBorderColor(BaseColor.LIGHT_GRAY);
                        cell2.setBorderColor(BaseColor.LIGHT_GRAY);
                        cell3.setBorderColor(BaseColor.LIGHT_GRAY);
                        cell4.setBorderColor(BaseColor.LIGHT_GRAY);

                        paymentMethod.addCell(cell1);
                        paymentMethod.addCell(cell2);
                        paymentMethod.addCell(cell3);
                        paymentMethod.addCell(cell4);

                    }
                }

                paymentMethod.setSpacingBefore(25);

            }

            totalDueAmount.setSpacingBefore(25);

            if (null != transactionDao.getCustomerPhoneno()) {
                Double totalDueBalance;

                //this will give live due amount but this is not what we want so i am commenting this.
                totalDueBalance = transactionRepository.getTransactionDueAmountByCustomer(transactionDao.getCustomerPhoneno());

                if ( null != totalDueBalance && totalDueBalance > 0) {
                    Paragraph totalBalanceDueText = new Paragraph("TOTAL BALANCE DUE", FontFactory.getFont(FontFactory.HELVETICA, 14, Font.BOLD));
                    totalBalanceDueText.setAlignment(PdfPCell.ALIGN_LEFT);

                    totalBalanceDue.addElement(totalBalanceDueText);
                    totalBalanceDue.setBorder(PdfPCell.NO_BORDER);

                    Paragraph totalBalanceDueAmount1 = new Paragraph("$ " + totalDueBalance, FontFactory.getFont(FontFactory.HELVETICA, 14, Font.BOLD));
                    totalBalanceDueAmount1.setAlignment(PdfPCell.ALIGN_RIGHT);

                    totalBalanceDueAmount.addElement(totalBalanceDueAmount1);
                    totalBalanceDueAmount.setBorder(PdfPCell.NO_BORDER);

                    totalDueAmount.addCell(totalBalanceDue);
                    totalDueAmount.addCell(totalBalanceDueAmount);
                }
            }

            doc.add(storeTable);

            doc.add(customerTable);

            doc.add(lineItemTable);

            doc.add(paymentTable);

            doc.add(paymentMethod);

            doc.add(totalDueAmount);

            if (null != transactionDao.getNote() && transactionDao.getNote().length() > 1) {
                Paragraph notes = new Paragraph("Receipt Notes: ");
                Paragraph transactionNotes = new Paragraph(transactionDao.getNote());
                transactionNotes.setSpacingBefore(30f);
                doc.add(notes);
                doc.add(transactionNotes);
            }

        }
    }

    private void initializeFonts() {


        try {
            bfBold = BaseFont.createFont(BaseFont.HELVETICA_BOLD, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
            bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);

        } catch (com.itextpdf.text.DocumentException | IOException e) {
            e.printStackTrace();
        }


    }
    public static class FooterTable extends PdfPageEventHelper {
        protected PdfPTable footer;

        public FooterTable(PdfPTable footer) {
            this.footer = footer;
        }

        public void onEndPage(PdfWriter writer, Document document) {
            footer.writeSelectedRows(0, -4, 36, 64, writer.getDirectContent());
        }
    }
}

