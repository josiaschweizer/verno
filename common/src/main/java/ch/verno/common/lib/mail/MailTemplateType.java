package ch.verno.common.lib.mail;

import jakarta.annotation.Nonnull;

public enum MailTemplateType {
  WELCOME("WELCOME"),
  COURSE_INVITE("COURSE_INVITE"),
  COURSE_REMINDER("COURSE_REMINDER"),
  ;

  @Nonnull private final String key;

  MailTemplateType(@Nonnull final String key) {
    this.key = key;
  }

  @Nonnull
  public String getKey() {
    return key;
  }

  @Nonnull
  public static MailTemplateType fromKey(@Nonnull final String key) {
    for (final var type : values()) {
      if (type.getKey().equals(key)) {
        return type;
      }
    }

    throw new IllegalArgumentException("Invalid MailTemplateType key: " + key);
  }
}
