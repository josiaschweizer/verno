package ch.verno.publ;

import org.jetbrains.annotations.NonNls;

public class VernoSecrets {

  @NonNls public static final String ENV_STRIPE_SECRET_KEY = "STRIPE_SECRET_KEY";
  @NonNls public static final String ENV_STRIPE_PRICE_ID_BASIC_PACKAGE = "STRIPE_PRICE_ID_BASIC_PACKAGE";
  @NonNls public static final String ENV_STRIPE_PRICE_ID_PRO_PACKAGE = "STRIPE_PRICE_ID_PRO_PACKAGE";
  @NonNls public static final String STRIPE_WEBHOOK_SECRET = "STRIPE_WEBHOOK_SECRET";

  @NonNls public static final String ENV_BILLING_API_USERNAME = "BILLING_API_USERNAME";
  @NonNls public static final String ENV_BILLING_API_PASSWORD = "BILLING_API_PASSWORD";

  @NonNls public static final String SMTP_HOST = "SMTP_HOST";
  @NonNls public static final String SMTP_PORT = "SMTP_PORT";
  @NonNls public static final String SMTP_USER = "SMTP_USER";
  @NonNls public static final String SMTP_PASS = "SMTP_PASS";
  @NonNls public static final String SMTP_TLS = "SMTP_TLS";
  @NonNls public static final String SMTP_SECURITY = "SMTP_SECURITY";

  @NonNls public static final String VERNO_BASE_URL_PATTERN = "VERNO_BASE_URL_PATTERN";
}
