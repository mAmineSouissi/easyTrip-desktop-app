package tn.esprit.easytripdesktopapp.utils;

import com.stripe.Stripe;

public class StripeConfig {
    private static final String SECRET_KEY = "sk_test_51QxrebGbhMituiTEyBO9Dw6k5Sg1oKIPaKdNff7zkvbB3lIq6m4O8eUSePd6LN9l1q0ix12qbIFWeRspaJWBQ2DM00A7ooLPdX";

    public static void init() {
        Stripe.apiKey = SECRET_KEY;
    }
}