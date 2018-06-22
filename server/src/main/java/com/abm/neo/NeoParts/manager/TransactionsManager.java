package com.abm.pos.ABMPos.manager;

import com.abm.pos.ABMPos.dao.*;
import com.abm.pos.ABMPos.dto.CustomerFinancialDto;
import com.abm.pos.ABMPos.dto.PaymentDetails;
import com.abm.pos.ABMPos.repository.*;
import com.abm.pos.ABMPos.util.Utility;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;


import com.itextpdf.text.pdf.draw.VerticalPositionMark;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import java.io.OutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import org.thymeleaf.context.Context;

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
import javax.swing.text.TabStop;


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
    private PaymentRepository paymentRepository;
    @Autowired
    private Utility utility;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductInventoryRepository productInventoryRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private StoreCreditRepository storeCreditRepository;

    @Autowired
    private CustomerManager customerManager;

    @Autowired
    private EmailHtmlSender emailHtmlSender;

    @Autowired
    private StoreSetupRepository storeSetupRepository;

    @Autowired
    private CustomerProductPriceRepository customerProductPriceRepository;

    private BaseFont bfBold;
    private BaseFont bf;
    private int pageNumber = 0;


    public TransactionDao addTransaction(TransactionDao transactionDao) {
        TransactionDao transactionDao1 = null;

            assert transactionDao != null;
            if (transactionDao.getStatus().equalsIgnoreCase("Complete") || transactionDao.getStatus().equalsIgnoreCase("Pending")) {

                List<TransactionLineItemDao> transactionLineItemDaoList = new ArrayList<>();
                List<TransactionLineItemDao> transactionLineItemDaoListNew = new ArrayList<>();
                transactionLineItemDaoList = transactionDao.getTransactionLineItemDaoList();

                // This Logic helps to solve the problem with data integrity exception, So i have to delete line item and then insert new details.
//            if(transactionDao.isParkSale()){
//                transactionLineItemRepository.delete(transactionDao.getTransactionLineItemDaoList());
//            }

                // This Logic helps to solve the problem with data integrity exception, So i have to delete line item and then insert new details.
                if (transactionDao.isParkSale()) {
                    transactionLineItemRepository.delete(transactionDao.getTransactionLineItemDaoList());
                }
                if (transactionDao.getTransactionComId() == 0 || transactionDao.isParkSale()) {

                    // Very important to do this other this will create problem again
                    transactionDao.setParkSale(false);
                    for (TransactionLineItemDao lineItemDao : transactionLineItemDaoList) {

                        // I need to set this up to keep track of the inventory, so with this i will know, whether i need to call Product inventory table again or not.
                        // So with first call if parchedQuantity == 0 that mean we can full fill this sale we do not need to check for other inventory for this product but if not
                        // Then we need to keep doing this process until parched Quantity == 0.
                        int purchasedQuantity = lineItemDao.getSaleQuantity();

                        do {
                            // step 1 : get the product from line item and then get the quantity for that product from inventory table on behalf of created time stamps.
                            ProductInventoryDao productInventoryDao;
                            // This call will give product inventory details on behalf of FIFO.
                            productInventoryDao = productInventoryRepository.findFirstByProductNoOrderByCreatedTimestampAsc(lineItemDao.getProductNo());

                            if (null != productInventoryDao) {
                                // This is best case.
                                if (productInventoryDao.getQuantity() > purchasedQuantity) {

                                    // This is Important because i need to set cost price separately that's why i need to do this.
                                    lineItemDao.setCost(productInventoryDao.getCost());
                                    transactionLineItemDaoListNew.add(lineItemDao);

                                    // Here I need to update the Product Inventory Table AS WELL AS PRODUCT TABLE : to keep up with quantity in Product Inventory Table
                                    // VERY IMPORT LOGIC.
                                    reduceQuantityFromProductInventoryTable(productInventoryDao, productInventoryDao.getQuantity() - purchasedQuantity);
                                    purchasedQuantity = 0;
                                }
                                // This means we do not have enough inventory to sale, so we can sale wt we have and then call inventory table again until purchase item == 0.
                                else if (productInventoryDao.getQuantity() > 0) {
                                    lineItemDao.setCost(productInventoryDao.getCost());
                                    transactionLineItemDaoListNew.add(lineItemDao);
                                    purchasedQuantity = purchasedQuantity - productInventoryDao.getQuantity();
                                    reduceQuantityFromProductInventoryTable(productInventoryDao, 0);

                                    // Also this means in Product Inventory table, inventory of this product is 0
                                    // So now we need to delete this row from the table
                                    // Here is the interesting this, we can delete this row only, IF IT IS NOT LAST ROW.
                                    // IF IT IS LAST ROW THEN WE JUST REDUCE THE QUANTITY AND LET ELSE CONDITION HANDLE IT
                                    // THIS CASE HAPPENS WHEN USER HAS NOT UPDATED THE PRODUCT INVENTORY TABLE BUT STILL HE IS TRYING TO SALE THE PRODUCT.

                                    deleteProductInventoryRow(productInventoryDao);

                                }
                                // This means we have last entry in product inventory table and user has not updated the quantity.
                                else {
                                    // This is Important because i need to set cost price separately that's why i need to do this.
                                    lineItemDao.setCost(productInventoryDao.getCost());
                                    transactionLineItemDaoListNew.add(lineItemDao);
                                    reduceQuantityFromProductInventoryTable(productInventoryDao, productInventoryDao.getQuantity() - purchasedQuantity);

                                    purchasedQuantity = 0;
                                }
                            }
                            // TODO need to handle this case  :(
                            else {
                                // Hopefully this logic will work.
                                transactionLineItemDaoListNew.add(lineItemDao);
                                purchasedQuantity = 0;
                                System.out.println("No Inventory details for this product.");
                            }
                        }
                        while (purchasedQuantity != 0);
                    }

                    transactionDao.setTransactionLineItemDaoList(transactionLineItemDaoListNew);
                }

                // Here i need to handle the case where customer is using Store credit to pay the amount.
                // I need to update the store credit for the customer and handle the transaction.
                for (PaymentDao payment : transactionDao.getPaymentDao()) {
                    if (null != transactionDao.getCustomerPhoneno() && payment.getType().equalsIgnoreCase("Store Credit") && payment.getAmount() > 0) {
                        setCustomerStoreCredit(transactionDao);
                    }
                }

                // Here I am handling the logic for the customer price lock where customers price will be saved after every transactions. No matter how is the retail price.
                // Here is the problem though, on return i need to manage this logic on ui, otherwise customer get profited when he does the return.

                //Do
//            if (null != transactionDao.getCustomerPhoneno() && transactionDao.getCustomerPhoneno().length() > 9 && null != transactionDao.getTransactionLineItemDaoList()) {
//                for (TransactionLineItemDao lineItem : transactionDao.getTransactionLineItemDaoList()) {
//
//                    CustomerProductPrice customerProductPrice = new CustomerProductPrice();
//                    CustomerProductPricePK customerProductPricePK = new CustomerProductPricePK();
//
//                    customerProductPricePK.setPhoneNo(transactionDao.getCustomerPhoneno());
//                    customerProductPricePK.setProductNo(lineItem.getProductNo());
//
//                    // Need to do retail with discount, because that is discounted price for customer
//                    customerProductPrice.setRetail(lineItem.getRetailWithDiscount());
//                    customerProductPrice.setCost(lineItem.getCost());
//                    customerProductPrice.setLastUpdatedTimestamp(transactionDao.getDate());
//
//                    customerProductPrice.setCustomerProductPricePK(customerProductPricePK);
//
//                    customerProductPriceRepository.save(customerProductPrice);
//                }
//            }
            } else if (transactionDao.getStatus().equalsIgnoreCase("Return") || transactionDao.getStatus().equalsIgnoreCase("Void")) {

                // this logic help to, manage regular return and RMI return, cause in RMI return we do not need to insert inventory again.
                if (!transactionDao.isRma()) {
                    // Managing inventory for the RETURN or VOID
                    manageProductInventoryAfterSale(transactionDao);
                }

                // Handing store credit here
                // This means user is giving store credit to the customer so i need to add customers store credit with valid reason.
                if (null != transactionDao.getCustomerPhoneno() && null != transactionDao.getPaymentDao()) {

                    CustomerDao customerDao = customerRepository.findByPhoneNo(transactionDao.getCustomerPhoneno());

                    for (PaymentDao paymentDao : transactionDao.getPaymentDao()) {
                        if (null != customerDao && paymentDao.getType().equalsIgnoreCase("Store Credit") && paymentDao.getAmount() > 0) {
                            StoreCreditDao storeCreditDao = new StoreCreditDao();
                            storeCreditDao.setAmount(paymentDao.getAmount());
                            storeCreditDao.setCustomerPhoneno(transactionDao.getCustomerPhoneno());
                            storeCreditDao.setEmployeeName(transactionDao.getUsername());
                            storeCreditDao.setReason("Store Credit For Transaction No: " + transactionDao.getTransactionComId());
                            storeCreditDao.setCreatedTimestamp(transactionDao.getDate());

                            storeCreditRepository.save(storeCreditDao);

                            customerDao.setStoreCredit(customerDao.getStoreCredit() + paymentDao.getAmount());

                            // finally updating customers account details whether it is store credit or on on account choose by the customer on the
                            customerRepository.save(customerDao);
                        }
                    }
                }

                // This logic helps to delete, all payment date when user return the transaction or void the transaction.
                // I need to do this, because in case of void and return, there will be one row with payment status as complete and if i dont delete it, i will show in reporting,
                // So this is very important logic to implement.
                // This will delete all payment details and at the end i am inserting new payment details for this transaction.
                if (transactionDao.getTransactionComId() != 0) {
                    paymentRepository.deletePaymentDetails(transactionDao.getTransactionComId());
                }
            } else if (transactionDao.getStatus().equalsIgnoreCase("Park")) {
                transactionDao.setParkSale(true);
            }


             transactionDao1 = transactionRepository.save(transactionDao);

            for (PaymentDao payment : transactionDao.getPaymentDao()) {

                if (payment.getTransactionPaymentId() <= 0){

                }

                payment.setTransactionComId(transactionDao1.getTransactionComId());

                // This logic helps when user is returing the transactin by giving store credit to the user, so here i need to store store credit as negative value to show correct reporting.
                if (payment.getType().equalsIgnoreCase("Store Credit") && payment.getAmount() > 0 && transactionDao.getStatus().equalsIgnoreCase("Return")) {
                    // funny logic, i love it.
                    payment.setAmount(payment.getAmount() * -1);
                }
            }
            paymentRepository.save(transactionDao.getPaymentDao());

            //System.out.println("Exception"+e);
        return transactionDao1;

//
//        if (transactionDao1.getTransactionComId() != 0) {
//
//            PaymentDao paymentDao;
//            paymentDao = transactionDao.getPaymentDao().get(0);
//
//            if (null != paymentDao) {
//
//                // TODO Need add this logic for store credit
//
//
//                // This logic helps when user is returing the transactin by giving store credit to the user, so here i need to store store credit as negative value to show correct reporting.
////                if (paymentDao.getStoreCredit() > 0 && transactionDao.getStatus().equalsIgnoreCase("Return")) {
////                    // funny logic, i love it.
////                    paymentDao.setStoreCredit(paymentDao.getStoreCredit() * -1);
////                }
//
//                // TODO Need add this logic for store credit
//
////                if(paymentDao.getCash() != 0 || paymentDao.getCredit() != 0  || paymentDao.getDebit() != 0  || paymentDao.getCheckAmount() != 0 || paymentDao.getStoreCredit()  != 0) {
////                    paymentRepository.insertPaymentDetail(transactionDao.getTransactionComId(),
////                            transactionDao.getStatus(),
////                            paymentDao.getDate(),
////                            paymentDao.getCash(),
////                            paymentDao.getCredit(),
////                            paymentDao.getDebit(),
////                            paymentDao.getCheckAmount(),
////                            paymentDao.getStoreCredit(),
////                            paymentDao.getLoyalty(),
////                            paymentDao.getLayby(),
////                            paymentDao.getChangeForCash(),
////                            paymentDao.getCreditCardLast4()
////                    );
////                }
//            }
////            transactionDao.setPaymentDao(paymentDaoList);
//
//        }
        // paymentDaoList = paymentRepository.findAllByTransactionComId(transactionDao.getTransactionComId());


        //transactionDao1.setPaymentDao(paymentDaoList);

    }

    private void manageProductInventoryAfterSale(TransactionDao transactionDao) {

        for (TransactionLineItemDao lineItemDao : transactionDao.getTransactionLineItemDaoList()) {


            ProductInventoryDao productInventoryDao = productInventoryRepository.findFirstByProductNoOrderByCreatedTimestampAsc(lineItemDao.getProductNo());

            // This is very important, if i dont do this then it will messed up complete count of the quantity.
            ProductInventoryDao productInventoryDaoFinal = new ProductInventoryDao();
            if (null != productInventoryDao) {
                productInventoryDaoFinal.setCost(Math.abs(productInventoryDao.getCost()));
                productInventoryDaoFinal.setProductNo(lineItemDao.getProductNo());
                productInventoryDaoFinal.setRetail(Math.abs((lineItemDao.getRetail())));
                productInventoryDaoFinal.setQuantity(lineItemDao.getSaleQuantity());
                productInventoryDaoFinal.setCreatedTimestamp(transactionDao.getDate());
            }

            ProductInventoryDao productInventoryDao1 = productInventoryRepository.save(productInventoryDaoFinal);

            // I need to recalculate the product inventory from inventory table and then set the quantity to PRODUCT tabel to sync product table stock and inventory Table.
            // This is real method who updated the PRODUCT TABLE.
            updateQuantityInProductTable(productInventoryDao1);
        }
    }

    // VERY IMPORT :::: HELPS TO SYNC PRODUCT INVENTORY TABLE AND PRODUCT TABLE.
    private void updateQuantityInProductTable(ProductInventoryDao productInventoryDao) {
        int totalProduct = 0;

        if (null != productInventoryDao) {

            List<ProductInventoryDao> productInventoryDaoList = productInventoryRepository.findAllByProductId(productInventoryDao.getProductId());

            if (null != productInventoryDaoList && productInventoryDaoList.size() > 0) {
                for (ProductInventoryDao productInventoryDao2 : productInventoryDaoList) {
                    totalProduct = totalProduct + productInventoryDao2.getQuantity();
                }
                // No need for this now, Now i need to update quantity in product table
                //productRepository.updateQuantityAfterInventoryUpdate(totalProduct, productInventoryDaoList.get(0).getCost(), productInventoryDaoList.get(0).getProductNo());
            }
        }
    }

    private void setCustomerStoreCredit(TransactionDao transactionDao) {

        CustomerDao customerDao = customerRepository.findByPhoneNo(transactionDao.getCustomerPhoneno());

        // TODO Need add this logic for store credit

        if (null != customerDao) {

            for (PaymentDao payment : transactionDao.getPaymentDao()) {
                if (null != transactionDao.getCustomerPhoneno()) {
                    customerDao.setStoreCredit(customerDao.getStoreCredit() - payment.getAmount());
                    customerRepository.save(customerDao);
                }
            }
        }
    }

    private void deleteProductInventoryRow(ProductInventoryDao productInventoryDao) {
        // First we need to get the count of the row, we can delete row only and only if it is not last row,
        // if it is not last row then we need to keep it.

        int count = productInventoryRepository.getCountOfRowByProductNo(productInventoryDao.getProductNo());

        if (count > 1) {
            productInventoryRepository.delete(productInventoryDao.getId());
        }
    }


    private void reduceQuantityFromProductInventoryTable(ProductInventoryDao productInventoryDao, int newQuantityAfterSubtractionFromPurchasedQuantity) {

        // Here i am just reducing purchasedQuantity from current inventory and updating into table.
        // Need to set purchasedQuantity cause that what customer has bought.(purchasedQuantity i)
        productInventoryDao.setQuantity(newQuantityAfterSubtractionFromPurchasedQuantity);
        ProductInventoryDao productInventoryDao1 = productInventoryRepository.save(productInventoryDao);
        // Here I need to update the Product Table to keep up with quantity in Product Inventory Table
        // VERY IMPORT LOGIC.
        updateQuantityInProductTable(productInventoryDao1);
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

    public List<TransactionDao> getTransactionByDate(String startDate, String endDate) {

        List<TransactionDao> transactionDaoList = new ArrayList<>();

        List<Object[]> result = transactionRepository.getTransactionByDate(startDate, endDate);


        if (null != result) {
            for (Object[] j : result) {
                TransactionDao transactionDao = new TransactionDao();

                transactionDao.setTransactionComId((Integer) j[0]);
                transactionDao.setDate(String.valueOf(j[1]));
                transactionDao.setTotalAmount(Double.parseDouble(j[2].toString()));
                transactionDao.setTax(Double.parseDouble(j[3].toString()));
                transactionDao.setTotalDiscount(Double.parseDouble(j[4].toString()));
                transactionDao.setSubtotal(Double.parseDouble(j[5].toString()));
                transactionDao.setQuantity((Integer) j[6]);
                transactionDao.setCustomerPhoneno(String.valueOf(j[7]));
                transactionDao.setStatus(String.valueOf(j[8]));
                transactionDao.setPreviousBalance(Double.parseDouble(j[9].toString()));
                transactionDao.setTransactionBalance(Double.parseDouble(j[10].toString()));
                transactionDao.setTotalBalanceDue(Double.parseDouble(j[11].toString()));
                transactionDao.setUsername(String.valueOf(j[12]));
                transactionDao.setCustomerFirstLastName(String.valueOf(j[13]));


                transactionDaoList.add(transactionDao);
            }
        }

        return transactionDaoList;
    }

    public TransactionDao voidTransaction(TransactionDao transactionDao) {

        // Managing inventory for the Void Transaction same as return.
        manageProductInventoryAfterSale(transactionDao);
        return transactionRepository.save(transactionDao);
    }

//    public boolean sendEmail(int receiptId) {
//
//        Context context = new Context();
//        TransactionDao transactionDao = getTransactionById(receiptId);
//        String email = null;
//        if (null != transactionDao && transactionDao.getCustomerPhoneno().length() > 1 && null != transactionDao.getTransactionLineItemDaoList() && null != transactionDao.getPaymentDao()) {
//            //First get customer details to send an email.
//            CustomerDao customerDao;
//            customerDao = customerRepository.findByPhoneNo(transactionDao.getCustomerPhoneno());
//
//            if (null != customerDao && null != customerDao.getEmail()) {
//
//                email = customerDao.getEmail();
//                //setting shipping details
//                context.setVariable("firstName", customerDao.getName());
//                context.setVariable("companyName", customerDao.getCompanyName());
//                context.setVariable("addressLine", customerDao.getStreet());
//                context.setVariable("City", customerDao.getCity());
//                context.setVariable("State", customerDao.getState());
//                context.setVariable("zipcode", customerDao.getZipCode());
//                context.setVariable("phoneNo", customerDao.getPhoneNo());
//            }
//
//
//            //setting line item details
//            context.setVariable("lineItem", transactionDao.getTransactionLineItemDaoList());
//
//            //setting transaction details
//            context.setVariable("subtotal", transactionDao.getSubtotal());
//            context.setVariable("shipping", transactionDao.getShipping());
//            context.setVariable("quantity", transactionDao.getQuantity());
//            context.setVariable("discount", transactionDao.getTotalDiscount());
//
//            context.setVariable("storeCredit", transactionDao.getPaymentDao().get(0).getStoreCredit());
////            context.setVariable("previousBalance", transactionDao.getPreviousBalance());
//            context.setVariable("salesTax", transactionDao.getTax());
//            context.setVariable("grandTotal", transactionDao.getTotalAmount());
//            context.setVariable("balance", transactionDao.getTransactionBalance());
//
//
//            //By this logic if email is failing i will get an email
////            if( null != receiptDtoList.get(0).getCustomerDtosList().get(0).getEmail())
////            {
////                email = receiptDtoList.get(0).getCustomerDtosList().get(0).getEmail();
////            }
//        }
//        assert transactionDao != null;
//        EmailStatus emailStatus = emailHtmlSender.send(email, transactionDao.getStoreSetupDao().getName() + " ORDER DETAILS", "template-1", context);
//
//        return emailStatus.isSuccess();
//    }

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

    public byte[] getA4Receipt(int receiptNo) throws DocumentException, IOException {

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

    private void printStoreDetailsTest(TransactionDao transactionDao, Document doc) throws IOException, DocumentException {

        PdfPTable storeTable = new PdfPTable(2);
        PdfPTable customerTable = new PdfPTable(2);
        PdfPTable lineItemTable = new PdfPTable(5);
        PdfPTable paymentTable = new PdfPTable(2);
        PdfPTable paymentMethod = new PdfPTable(4);
        PdfPTable totalDueAmount = new PdfPTable(2);


        String[] header = new String[]{"PRODUCT NO", "DESCRIPTION", "QTY", "RETAIL", "AMOUNT"};
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
                lineItemTable.setWidths(new float[]{2.3f, 7.5f, 1, 1.5f, 1.8f});
                lineItemTable.setSpacingBefore(15);
                lineItemTable.setSplitLate(false);

                for (String columnHeader : header) {
                    PdfPCell headerCell = new PdfPCell();
                    headerCell.addElement(new Phrase(columnHeader, FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD)));
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

                    cell1.addElement(new Phrase(lineItem.getProductNo(), FontFactory.getFont(FontFactory.HELVETICA, 8, Font.NORMAL)));
                    cell2.addElement(new Phrase(lineItem.getDescription(), FontFactory.getFont(FontFactory.HELVETICA, 8, Font.NORMAL)));
                    cell3.setCellEvent(new PositionEvent(new Phrase(10, String.valueOf(lineItem.getSaleQuantity()), FontFactory.getFont(FontFactory.HELVETICA, 8, Font.NORMAL)), 0.5f, 0.5f, Element.ALIGN_CENTER));
                    cell4.setCellEvent(new PositionEvent(new Phrase(10, String.valueOf(lineItem.getRetailWithDiscount()), FontFactory.getFont(FontFactory.HELVETICA, 8, Font.NORMAL)), 0.5f, 0.5f, Element.ALIGN_CENTER));
                    cell5.setCellEvent(new PositionEvent(new Phrase(10, String.valueOf(lineItem.getTotalProductPrice()), FontFactory.getFont(FontFactory.HELVETICA, 8, Font.NORMAL)), 0.5f, 0.5f, Element.ALIGN_CENTER));

//                    cell3.addElement(new Phrase(String.valueOf(lineItem.getSaleQuantity()),FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL)));
//                    cell4.addElement(new Phrase(String.valueOf(lineItem.getRetailWithDiscount()),FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL)));
//                    cell5.addElement(new Phrase(String.valueOf(lineItem.getTotalProductPrice()),FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL)));

                    cell1.setBorderColor(BaseColor.LIGHT_GRAY);
                    cell2.setBorderColor(BaseColor.LIGHT_GRAY);
                    cell3.setBorderColor(BaseColor.LIGHT_GRAY);
                    cell4.setBorderColor(BaseColor.LIGHT_GRAY);
                    cell5.setBorderColor(BaseColor.LIGHT_GRAY);

                    lineItemTable.addCell(cell1);
                    lineItemTable.addCell(cell2);
                    lineItemTable.addCell(cell3);
                    lineItemTable.addCell(cell4);
                    lineItemTable.addCell(cell5);
                }

            }

            Paragraph subtotal = new Paragraph("SUBTOTAL", FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL));
            subtotal.setAlignment(PdfPCell.ALIGN_LEFT);
            Paragraph tax = new Paragraph("TAX", FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL));
            tax.setAlignment(PdfPCell.ALIGN_LEFT);
            Paragraph discount = new Paragraph("DISCOUNT", FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL));
            discount.setAlignment(PdfPCell.ALIGN_LEFT);
            Paragraph quantity = new Paragraph("QUANTITY", FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL));
            quantity.setAlignment(PdfPCell.ALIGN_LEFT);
            Paragraph shipping = new Paragraph("SHIPPING", FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL));
            shipping.setAlignment(PdfPCell.ALIGN_LEFT);
            Paragraph total = new Paragraph("TOTAL", FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL));
            total.setAlignment(PdfPCell.ALIGN_LEFT);
            Paragraph balanceDue = new Paragraph("BALANCE DUE", FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD));
            balanceDue.setAlignment(PdfPCell.ALIGN_LEFT);

            paymentType.addElement(subtotal);
            paymentType.addElement(tax);
            paymentType.addElement(discount);
            paymentType.addElement(quantity);

            if (transactionDao.getShipping() > 0) {
                paymentType.addElement(shipping);
            }
            paymentType.addElement(total);
            paymentType.addElement(balanceDue);

            paymentType.setBorder(PdfPCell.NO_BORDER);


            Paragraph subtotal1 = new Paragraph("$ " + transactionDao.getSubtotal(), FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL));
            subtotal1.setAlignment(PdfPCell.ALIGN_RIGHT);
            Paragraph tax1 = new Paragraph("$ " + transactionDao.getTax(), FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL));
            tax1.setAlignment(PdfPCell.ALIGN_RIGHT);
            Paragraph discount1 = new Paragraph("$ " + transactionDao.getTotalDiscount(), FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL));
            discount1.setAlignment(PdfPCell.ALIGN_RIGHT);
            Paragraph quantity1 = new Paragraph(String.valueOf(transactionDao.getQuantity()), FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL));
            quantity1.setAlignment(PdfPCell.ALIGN_RIGHT);
            Paragraph shipping1 = new Paragraph(String.valueOf(transactionDao.getShipping()), FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL));
            shipping1.setAlignment(PdfPCell.ALIGN_RIGHT);
            Paragraph total1 = new Paragraph("$ " + transactionDao.getTotalAmount(), FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL));
            total1.setAlignment(PdfPCell.ALIGN_RIGHT);
            Paragraph balanceDue1 = new Paragraph("$ " + transactionDao.getTransactionBalance(), FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD));
            balanceDue1.setAlignment(PdfPCell.ALIGN_RIGHT);

            paymentAmount.addElement(subtotal1);
            paymentAmount.addElement(tax1);
            paymentAmount.addElement(discount1);
            paymentAmount.addElement(quantity1);
            if (transactionDao.getShipping() > 0) {
                paymentAmount.addElement(shipping1);
            }
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
                List<Double> totalDueBalance;

                // this will give live due amount but this is not what we want so i am commenting this.
                //totalDueBalance = transactionRepository.getTransactionDueAmountByCustomer(transactionDao.getCustomerPhoneno());

                if ( transactionDao.getTotalBalanceDue() > 0) {
                    Paragraph totalBalanceDueText = new Paragraph("TOTAL BALANCE DUE", FontFactory.getFont(FontFactory.HELVETICA, 14, Font.BOLD));
                    totalBalanceDueText.setAlignment(PdfPCell.ALIGN_LEFT);

                    totalBalanceDue.addElement(totalBalanceDueText);
                    totalBalanceDue.setBorder(PdfPCell.NO_BORDER);

                    Paragraph totalBalanceDueAmount1 = new Paragraph("$ " + transactionDao.getTotalBalanceDue(), FontFactory.getFont(FontFactory.HELVETICA, 14, Font.BOLD));
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

    public byte[] printPaymentReceipt(PaymentDao[] paymentDao) {



        return null;
    }

    public static class FooterTable extends PdfPageEventHelper {
        protected PdfPTable footer;

        public FooterTable(PdfPTable footer) {
            this.footer = footer;
        }

        public void onEndPage(PdfWriter writer, Document document) {
            footer.writeSelectedRows(0, -4, 36, 64, writer.getDirectContent());
        }
//
//        this is a footer xcvxcv sdfsdf sdfsdfsdf sfsfsfsfsfsf" +
//                "sfscdsfsd sdfsdfsdf sfsdfsf s fsfsfsfsfsfsfsfs       sdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsd" +
//                "sdfsdfsdfsdfsdfsdfasdfasdfasdfsadf sdafasdfsadfasdfasdfsadfasdfasdfasdfadsfasdfasdf" +
//                "asdfasdfasdfasdfsdafadsfasdfasdfasd  sd  das f adsf asd f ads f ds f asd f sad f s dafsda f sad fa sdf asd f asd f asd f sad f sad f asdf asd f sad f asd f asdf a sdf as df asd f sa dfasdfas df sad f sad f asdf" +
//                "asdf sda f sdaf asdf asdf a sdf  dsaf dsa f sd f ds f sda f sda f sad f sad f asd fa sd f sdaf as

//    class MyFooter extends PdfPageEventHelper {
//        Font ffont = new Font(Font.FontFamily.UNDEFINED, 5, Font.ITALIC);
//
//        public void onEndPage(PdfWriter writer, Document document) {
//            PdfContentByte cb = writer.getDirectContent();
//            Phrase footer = new Phrase(" f asd f asd f asd f as sdf asd", ffont);
//            ColumnText.showTextAligned(cb, Element.ALIGN_CENTER,
//                    footer,
//                    (document.right() - document.left()) / 2 + document.leftMargin(),
//                    document.bottom() - 10, 0);
//        }
    }

        public List<TransactionDao> getPendingInvoiceByCustomer(String phoneNo) {

            return transactionRepository.findAllByStatusEqualsAndCustomerPhoneno("Pending", phoneNo);
        }

        public List<TransactionDao> getAllInvoiceByCustomer(String startDate, String endDate, String phoneNo) {

            List<TransactionDao> transactionDaoList = new ArrayList<>();
            List<TransactionDao> newTransactionDaoList = new ArrayList<>();


            transactionDaoList = transactionRepository.findAllByCustomerPhonenoAndStatusAndDateBetweenOrderByDateDesc(phoneNo, "Pending", startDate, endDate);

            if (null != transactionDaoList) {
                for (TransactionDao transactionDao : transactionDaoList) {

                    List<PaymentDao> paymentDaoList = paymentRepository.findAllByTransactionComId(transactionDao.getTransactionComId());
                    if (null != paymentDaoList) {
                        transactionDao.setPaymentDao(paymentDaoList);
                    }

                    newTransactionDaoList.add(transactionDao);
                }
            }
            return newTransactionDaoList;
        }

        public List<PaymentDao> getPaymentDetailsById(int transactionCompId) {

            return paymentRepository.findAllByTransactionComId(transactionCompId);
        }


        private void initializeFonts() {


            try {
                bfBold = BaseFont.createFont(BaseFont.HELVETICA_BOLD, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
                bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);

            } catch (DocumentException | IOException e) {
                e.printStackTrace();
            }


        }

        public CustomerFinancialDto getCustomerFinancialDetails(String startDate, String endDate, String phoneNo) {

            CustomerFinancialDto customerFinancialDto = new CustomerFinancialDto();

            List<Object[]> result = transactionRepository.getCustomerFinancialDetails(startDate, endDate, phoneNo);

            if (null != result) {
                for (Object[] j : result) {
                    customerFinancialDto.setDueBalance(Double.parseDouble(j[0].toString()));
                    customerFinancialDto.setTotalSpending(Double.parseDouble(j[1].toString()));
                    customerFinancialDto.setTotalReturn(Double.parseDouble(j[2].toString()));
                }
            }

            return customerFinancialDto;
        }

//    private void printPageNumber(PdfContentByte cb) {
//
//
//        cb.beginText();
//        cb.setFontAndSize(bfBold, 8);
//        cb.showTextAligned(PdfContentByte.ALIGN_CENTER, "Page No. " + (pageNumber + 1), 570, 25, 0);
//        cb.endText();
//
//        pageNumber++;
//
//    }

//    private void createCustomerDetails(PdfContentByte cb, float x, float y, String text) {
//
//
//        cb.beginText();
//        cb.setFontAndSize(bfBold, 8);
//        cb.setTextMatrix(x, y);
//
//        if (null != text)
//            cb.showText(text.trim());
//        cb.endText();
//
//    }


    }

