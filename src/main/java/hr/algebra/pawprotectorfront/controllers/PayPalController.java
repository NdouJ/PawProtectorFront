package hr.algebra.pawprotectorfront.controllers;

import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/paypal")
public class PayPalController {

    @Autowired
    private APIContext apiContext;

    @GetMapping("/donate")
    public RedirectView donate(@RequestParam("amount") double amount) {
        Amount paypalAmount = new Amount();
        paypalAmount.setCurrency("USD");
        paypalAmount.setTotal(String.format("%.2f", amount));

        Transaction transaction = new Transaction();
        transaction.setDescription("Donation");
        transaction.setAmount(paypalAmount);

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");

        Payment payment = new Payment();
        payment.setIntent("sale");
        payment.setPayer(payer);
        payment.setTransactions(transactions);

        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl("http://localhost:8080/paypal/cancel");
        redirectUrls.setReturnUrl("http://localhost:8080/paypal/success");
        payment.setRedirectUrls(redirectUrls);

        try {
            Payment createdPayment = payment.create(apiContext);
            return new RedirectView(createdPayment.getLinks().stream()
                    .filter(link -> link.getRel().equals("approval_url"))
                    .findFirst()
                    .get()
                    .getHref());
        } catch (PayPalRESTException e) {
            e.printStackTrace();
            return new RedirectView("/paypal/cancel");
        }
    }

    @GetMapping("/success")
    public ModelAndView success(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId) {
        try {
            Payment payment = Payment.get(apiContext, paymentId);

            PaymentExecution paymentExecution = new PaymentExecution();
            paymentExecution.setPayerId(payerId);
            Payment executedPayment = payment.execute(apiContext, paymentExecution);

            if (executedPayment.getState().equals("approved")) {
                return new ModelAndView("success");
            }
        } catch (PayPalRESTException e) {
            e.printStackTrace();
        }
        return new ModelAndView("failure");
    }

    @GetMapping("/cancel")
    public ModelAndView cancel() {
        return new ModelAndView("cancel");
    }
}

