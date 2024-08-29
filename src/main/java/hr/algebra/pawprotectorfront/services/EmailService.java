package hr.algebra.pawprotectorfront.services;


import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Value("${sendgrid.api.key}")
    private String sendGridApiKey;

    public void sendEmail(String toEmail, String password) {
        SendGrid sg = new SendGrid(sendGridApiKey);

        Email from = new Email("jelena.ndou@gmail.com");
        String subject = "Thank you for registration, your temporary password is: " + password;
        Email to = new Email(toEmail);
        Content content = new Content("text/plain", "Body of your email");

        Mail mail = new Mail(from, subject, to, content);

        Request request = new Request();
        try {

            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);

            System.out.println("SendGrid API Response:");
            System.out.println("Status Code: " + response.getStatusCode());
            System.out.println("Body: " + response.getBody());
            System.out.println("Headers: " + response.getHeaders());
            System.out.println("Recipient Email: " + toEmail);


            if (response.getStatusCode() >= 200 && response.getStatusCode() < 300) {
                System.out.println("Email sent successfully!");
            } else {
                System.out.println("Failed to send email. Check SendGrid API response for details.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
