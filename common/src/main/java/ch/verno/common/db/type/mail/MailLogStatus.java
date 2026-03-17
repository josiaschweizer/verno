package ch.verno.common.db.type.mail;

import jakarta.annotation.Nonnull;

public enum MailLogStatus {
  QUEUED("mail.queued"),
  SENT("mail.send"),
  FAILED("mail.failed");

  @Nonnull private final String displayKey;

  MailLogStatus(@Nonnull final String displayKey) {
    this.displayKey = displayKey;
  }

  @Nonnull
  public String getDisplayKey() {
    return displayKey;
  }
}