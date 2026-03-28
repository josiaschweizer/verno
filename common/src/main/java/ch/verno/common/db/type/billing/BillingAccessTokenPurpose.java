package ch.verno.common.db.type.billing;

public enum BillingAccessTokenPurpose {
  UPDATE_PAYMENT_METHOD,
  START_CHECKOUT,
  OPEN_BILLING_PORTAL,

  DEV_UPDATE_PAYMENT_METHOD,
  DEV_START_CHECKOUT
}