package com.stripe.sample;

import com.stripe.Stripe;
import io.github.cdimascio.dotenv.Dotenv;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.PaymentIntentCreateParams.ConfirmationMethod;
import com.stripe.net.RequestOptions;

public class Klarna
{
    public static void main(String[] args)
    {
        try {
            // Docs: https://stripe.com/docs/payments/ideal
            Dotenv dotenv = Dotenv.load();
            Stripe.apiKey = dotenv.get("STRIPE_SECRET_KEY");

            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .addPaymentMethodType("klarna")
                .putExtraParam("payment_method_data[type]", "klarna")
                .putExtraParam("payment_method_data[billing_details][address][country]", "DE")
                .putExtraParam("line_items[0][quantity]", 2)
                .putExtraParam("line_items[0][description]", "Grand day out")
                .putExtraParam("line_items[0][product_details][unit_amount]", 1999)
                .putExtraParam("line_items[0][product_details][currency]", "eur")
                .putExtraParam("line_items[0][product_details][product_sku]", "grand_day_out")
                .setReturnUrl("http://m.com/finishedPayment")
                .setConfirm(true)
                .build();
            PaymentIntent paymentIntent = PaymentIntent.create(params, RequestOptions.builder().setStripeVersionOverride("2020-08-27; line_items_beta=v1").build());
            System.out.println(paymentIntent);
            System.out.println(paymentIntent.getNextAction().getRedirectToUrl().getUrl().toString());
            System.out.println("^^^ Follow the redirect then press any key");

            // The customer follows the redirect URL to their iDEAL bank!
            System.in.read();

            // Manually confirm the payment
            PaymentIntent confirmedPaymentIntent = paymentIntent.confirm();
            System.out.println(confirmedPaymentIntent);

        } catch (Exception exception) {
            System.out.println(exception);
        }
    }
}
