package com.abm.neo.NeoParts.manager;

import com.abm.neo.NeoParts.dto.CustomerDao;
import com.abm.neo.NeoParts.entity.ResetPasswordToken;
import com.abm.neo.NeoParts.entity.StoreSetupDao;
import com.abm.neo.NeoParts.repository.CustomerRepository;
import com.abm.neo.NeoParts.repository.ResetPasswordRepository;
import com.abm.neo.NeoParts.repository.StoreSetupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Component;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Random;

/**
 * Created by apatel2 on 5/17/17.
 */

@Component
public class CustomerManager {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private StoreSetupRepository storeSetupRepository;

    @Autowired
    private ResetPasswordRepository resetPasswordRepository;


    public void addCustomer(CustomerDao customerDao) {

        if(null != customerDao && customerDao.getOperationType().equalsIgnoreCase("Add")){
            // neo@123
            customerDao.setPassword("$2a$04$.hCVRkdlT1dAUljOrS9IyeUaeVxz/NEy9xFGGTcRy9PTqr.aCWboG");
        }
        CustomerDao customerDao1 = customerRepository.save(customerDao);

        if(null != customerDao1){

            //Send Email to Customer that he or she registered successfully.
            sendEmailToCustomer(customerDao1);

        }
    }

    public List<CustomerDao> getCustomer() {

        return customerRepository.findAll();
    }

    public CustomerDao getUserLoginDetails(String username, String password) {

        CustomerDao customerDao = new CustomerDao();

         customerDao = this.customerRepository.findByEmailAndPassword(username, password);
        if(null != customerDao && customerDao.getEmail().equalsIgnoreCase(username)) {
            customerDao.setValidUser(true);
            return customerDao;
        }
        else if(null == customerDao){
            CustomerDao customerDao1 = new CustomerDao();
            customerDao1.setValidUser(false);
            return customerDao1;
        }

        return new CustomerDao();
    }

    public CustomerDao getCustomerDetailsByEmail(String email) {

        if(null != email){
            return this.customerRepository.findByEmail(email);
        }
        return null;
    }

    private boolean sendEmailToCustomer(CustomerDao customerDao) throws MailException {

        boolean response = false;
        if (true) {
            //First get customer details to send an email.
//            customerDao = customerRepository.findByPhoneNo(transactionDao.getCustomerPhoneno());

            if (true) {

                StoreSetupDao storeSetupDao = storeSetupRepository.findOne(1);

                if(null !=storeSetupDao && null != customerDao) {
                    String from = storeSetupDao.getEmail();
                    String to = customerDao.getEmail();


                    String newline = System.getProperty("line.separator");
                    String content = "Dear " + customerDao.getName() + newline
                            + newline
                            + newline
                            + "Welcome to " + storeSetupDao.getName() + " Please Visit http://neocellularparts.com/#/customer/signup" + newline
                            + "Please Login With Following Details"
                            + newline
                            + "Username : " + customerDao.getEmail() + newline
                            + "Password:neo@123" + newline
                            + newline
                            + newline
                            + newline
                            + newline
                            + "Thank You" + newline
                            + storeSetupDao.getName();


                    String subject = " Welcome " + customerDao.getName();

                    final String password = storeSetupDao.getEmailPassword();

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
                        //outputStream = new ByteArrayOutputStream();
                        //byte[] bytes = getA4Receipt(receiptId);

                        //construct the pdf body part
                        //DataSource dataSource = new ByteArrayDataSource(bytes, "application/pdf");
                        //MimeBodyPart pdfBodyPart = new MimeBodyPart();
                        //pdfBodyPart.setDataHandler(new DataHandler(dataSource));
                        //pdfBodyPart.setFileName("Invoice.pdf");

                        //construct the mime multi part
                        MimeMultipart mimeMultipart = new MimeMultipart();
                        mimeMultipart.addBodyPart(textBodyPart);
                        //mimeMultipart.addBodyPart(pdfBodyPart);

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

        }

        return response;

//        SimpleMailMessage mail = new SimpleMailMessage();
//        mail.setTo(customerDto.getEmail());
//        mail.setFrom("exwirelessatl@gmail.com");
//        mail.setSubject("Thank You For Joining Excel-Wireless");
//        mail.setText("Dear :" + customerDto.getFirstName() + "We have received your request for joining Excel-Wireless," +
//                " We will get back to you with in 24 hours. ");
//        javaMailSender.send(mail);
    }

    public boolean sendEmailToResetPassword(String email) {

        boolean response = false;
        CustomerDao customerDao = customerRepository.findByEmail(email);
        StoreSetupDao storeSetupDao = storeSetupRepository.findOne(1);

        if(null !=storeSetupDao && null != customerDao && null != customerDao.getEmail())
        {
            // Now First I need to generate the token and set the expiry date for the token for to reset password link.
            String token = generateRandomToken();
            ResetPasswordToken resetPasswordToken = new ResetPasswordToken();

            String currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            resetPasswordToken.setEmail(customerDao.getEmail());
            resetPasswordToken.setCreatedDate(currentDate);
            //resetPasswordToken.setExpiryDate();
            resetPasswordToken.setToken(token);

            resetPasswordRepository.save(resetPasswordToken);

            String from = storeSetupDao.getEmail();
            String to = customerDao.getEmail();
//             String to = "";

            String newline = System.getProperty("line.separator");


            String content = "Dear " + customerDao.getName()
                    + newline
                    + newline
                    + newline
                    + "We have received your request to reset password for your account."
                    + newline
                    + "If you requested this password change, please click on link below to reset your password here:"
                    + newline
                    + newline
                    + newline
                    +"http://neocellularparts.com/#/customer/reset-password?email="+customerDao.getEmail()+"&token="+token;



        String subject = " Reset Password Conformation For " + customerDao.getName();

        final String password = storeSetupDao.getEmailPassword();

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

            //construct the mime multi part
            MimeMultipart mimeMultipart = new MimeMultipart();
            mimeMultipart.addBodyPart(textBodyPart);

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


return response;
    }

    private String generateRandomToken() {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        return buffer.toString();
    }

    public void resetPassword(CustomerDao customerDao) throws ParseException {

        if(null != customerDao){

            //First Validate the token and experation date and email address.
            boolean isValidToken =  validateToken(customerDao);

            if(isValidToken && customerDao.getPassword().equals(customerDao.getConfirmPassword())){
                customerRepository.changePassword(customerDao.getPassword(), customerDao.getEmail());
            }
        }

    }

    private boolean validateToken(CustomerDao customerDao) throws ParseException {

        boolean response = false;
        ResetPasswordToken resetPasswordToken = resetPasswordRepository.findFirstByEmailOrderByCreatedDateDesc(customerDao.getEmail());

        if(null != resetPasswordToken)
        {
            // first check the token Expiration date-- > here i am making link active for 10 min only.
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            String currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            Date date1 = format.parse(resetPasswordToken.getCreatedDate());
            Date currentDate1 = format.parse(currentDate);

            long difference = currentDate1.getTime() - date1.getTime();

            // Which is 10 min--> link will expired after 10 min
            if(difference <= 600000) {

                if (resetPasswordToken.getEmail().equalsIgnoreCase(customerDao.getEmail())
                        && resetPasswordToken.getToken().equals(customerDao.getToken())) {
                    response = true;
                }
            }
        }

        return response;
    }
//    private void sendEmailToAdmin(CustomerDto customerDto) throws MailException {
//
//        SimpleMailMessage mail = new SimpleMailMessage();
//        mail.setTo("exwirelessatl@gmail.com");
//        mail.setFrom("exwirelessatl@gmail.com");
//        mail.setSubject("Thank You For Joining Excel-Wireless");
//        mail.setText("FirstName :" + customerDto.getFirstName()
//                + " Phone No:" + customerDto.getPhoneNo());
//        javaMailSender.send(mail);
//    }
}

