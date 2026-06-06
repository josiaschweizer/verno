package ch.verno.common.exceptions.db;

import jakarta.annotation.Nonnull;

public enum DBNotFoundReason {
  INSTRUCTOR_BY_ID_NOT_FOUND("Instructor not found with id: "),
  PARTICIPANT_BY_ID_NOT_FOUND("Participant not found with id: "),
  ADDRESS_BY_ID_NOT_FOUND("Address not found with id: "),
  PARENT_BY_ID_NOT_FOUND("Parent not found with id: "),
  GENDER_BY_ID_NOT_FOUND("Gender not found with id: "),
  COURSE_BY_ID_NOT_FOUND("Course not found with id: "),
  COURSE_LEVEL_BY_ID_NOT_FOUND("Course not found with id: "),
  COURSE_SCHEDULE_BY_ID_NOT_FOUND("Course schedule not found with id: "),
  TENANT_SETTINGS_BY_ID_NOT_FOUND("Tenant settings not found with id: "),
  USER_SETTING_BY_ID_NOT_FOUND("User settings not found with id: "),
  USER_SETTING_BY_USER_ID_NOT_FOUND("User settings not found for user id: "),
  APP_USER_NOT_FOUND("App user not found with id: "),
  MAIL_TEMPLATE_BY_KEY_NOT_FOUND("Mail template not found with key: "),
  MAIL_CONFIG_BY_TENANT_NOT_FOUND("Mail config not found for tenant: "),
  MAIL_LOG_BY_ID_NOT_FOUND("Mail log not found with id: "),
  TEXT_BY_ID_NOT_FOUND("Text not found with id: "),

  TENANT_BILLING_BY_ID_NOT_FOUND("Tenant billing not found with id: "),
  TENANT_BILLING_BY_TENANT_ID_NOT_FOUND("Tenant billing not found for tenant id: "),
  BILLING_ACCESS_TOKEN_BY_ID_NOT_FOUND("Billing access token not found with id: "),
  BILLING_ACCESS_TOKEN_BY_TOKEN_HASH_NOT_FOUND("Billing access token not found for token hash: "),
  BILLING_WEBHOOK_EVENT_BY_ID_NOT_FOUND("Billing webhook event not found with id: "),
  BILLING_WEBHOOK_EVENT_BY_STRIPE_EVENT_ID_NOT_FOUND("Billing webhook event not found for Stripe event id: "),

  NOT_ABLE_TO_DELETE_ENTITY("Not able to delete entity with");

  @Nonnull
  private final String message;

  DBNotFoundReason(@Nonnull final String message) {
    this.message = message;
  }

  @Nonnull
  public String getMessage() {
    return message;
  }
}