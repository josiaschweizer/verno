package ch.verno.ui.i18n;

import jakarta.annotation.Nonnull;

import java.util.Arrays;
import java.util.Optional;

@SuppressWarnings("HardCodedStringLiteral")
public enum I18nBundles {

  PARTICIPANT("participant", "i18n.participant"),
  COURSE("course", "i18n.course"),
  COURSE_LEVEL("courseLevel", "i18n.courseLevel"),

  BASE("base", "i18n.base"),

  COMMON("common", "i18n.common"),
  FILTER("filter", "i18n.filter"),
  SHARED("shared", "i18n.shared"),
  SETTINGS("setting", "i18n.setting");

  @Nonnull
  private final String keyPrefix;

  @Nonnull
  private final String resourceBundleName;

  I18nBundles(@Nonnull final String keyPrefix,
              @Nonnull final String resourceBundleName) {
    this.keyPrefix = keyPrefix;
    this.resourceBundleName = resourceBundleName;
  }

  @Nonnull
  public String getResourceBundleName() {
    return resourceBundleName;
  }

  @Nonnull
  public String getKeyPrefix() {
    return keyPrefix;
  }

  @Nonnull
  public static Optional<I18nBundles> fromKey(@Nonnull final String key) {
    return Arrays.stream(values())
            .filter(bundle -> key.startsWith(bundle.keyPrefix + "."))
            .findFirst();
  }
}