package tn.esprit.easytripdesktopapp.services;

import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import tn.esprit.easytripdesktopapp.utils.StripeConfig;

public class StripeService {

    public String createCheckoutSession(double amount, String currency) {
        try {
            StripeConfig.init(); // Initialiser Stripe

            SessionCreateParams params =
                    SessionCreateParams.builder()
                            .setMode(SessionCreateParams.Mode.PAYMENT)
                            .setSuccessUrl("http://localhost:4242/success") // URL après paiement réussi
                            .setCancelUrl("http://localhost:4242/cancel")   // URL en cas d'annulation
                            .addLineItem(
                                    SessionCreateParams.LineItem.builder()
                                            .setQuantity(1L)
                                            .setPriceData(
                                                    SessionCreateParams.LineItem.PriceData.builder()
                                                            .setCurrency(currency)
                                                            .setUnitAmount((long) (amount * 100))
                                                            .setProductData(
                                                                    SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                            .setName("Payer Réservation")
                                                                            .build()
                                                            )
                                                            .build()
                                            )
                                            .build()
                            )
                            .build();

            Session session = Session.create(params);
            return session.getUrl(); // Retourne l'URL de paiement

        } catch (StripeException e) {
            e.printStackTrace();
            return null;
        }
    }

}