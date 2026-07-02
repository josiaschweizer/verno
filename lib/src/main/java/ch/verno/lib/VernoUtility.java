package ch.verno.lib;

import org.jetbrains.annotations.NonNls;

public final class VernoUtility {

  private VernoUtility() {
  }

  // Allgemein
  @NonNls public static final String NONE = "0";
  @NonNls public static final String AUTO = "auto";
  @NonNls public static final String FULL_SIZE = "100%";

  @NonNls public static final String ONE_REM = "1rem";
  @NonNls public static final String FOUR_REM = "4rem";

  // Flex
  @NonNls public static final String FLEX_GROW_ZERO = "0";
  @NonNls public static final String FLEX_GROW_ONE = "1";
  @NonNls public static final String FLEX_SHRINK_ZERO = "0";
  @NonNls public static final String FLEX_SHRINK_ONE = "1";

  // Lumo Spacing
  @NonNls public static final String LUMO_SPACE_NONE = "0";
  @NonNls public static final String LUMO_SPACE_XS = "var(--lumo-space-xs)";
  @NonNls public static final String LUMO_SPACE_S = "var(--lumo-space-s)";
  @NonNls public static final String LUMO_SPACE_M = "var(--lumo-space-m)";
  @NonNls public static final String LUMO_SPACE_L = "var(--lumo-space-l)";
  @NonNls public static final String LUMO_SPACE_XL = "var(--lumo-space-xl)";

  // Lumo Component Sizes
  @NonNls public static final String LUMO_SIZE_XS = "var(--lumo-size-xs)";
  @NonNls public static final String LUMO_SIZE_S = "var(--lumo-size-s)";
  @NonNls public static final String LUMO_SIZE_M = "var(--lumo-size-m)";
  @NonNls public static final String LUMO_SIZE_L = "var(--lumo-size-l)";
  @NonNls public static final String LUMO_SIZE_XL = "var(--lumo-size-xl)";

  // Icons
  @NonNls public static final String LUMO_ICON_SIZE_XS = "var(--lumo-icon-size-xs)";
  @NonNls public static final String LUMO_ICON_SIZE_S = "var(--lumo-icon-size-s)";
  @NonNls public static final String LUMO_ICON_SIZE_M = "var(--lumo-icon-size-m)";
  @NonNls public static final String LUMO_ICON_SIZE_L = "var(--lumo-icon-size-l)";

  // Typography
  @NonNls public static final String LUMO_FONT_FAMILY = "var(--lumo-font-family)";

  @NonNls public static final String LUMO_FONT_SIZE_XXS = "var(--lumo-font-size-xxs)";
  @NonNls public static final String LUMO_FONT_SIZE_XS = "var(--lumo-font-size-xs)";
  @NonNls public static final String LUMO_FONT_SIZE_S = "var(--lumo-font-size-s)";
  @NonNls public static final String LUMO_FONT_SIZE_M = "var(--lumo-font-size-m)";
  @NonNls public static final String LUMO_FONT_SIZE_L = "var(--lumo-font-size-l)";
  @NonNls public static final String LUMO_FONT_SIZE_XL = "var(--lumo-font-size-xl)";
  @NonNls public static final String LUMO_FONT_SIZE_XXL = "var(--lumo-font-size-xxl)";
  @NonNls public static final String LUMO_FONT_SIZE_XXXL = "var(--lumo-font-size-xxxl)";

  @NonNls public static final String LUMO_LINE_HEIGHT_XS = "var(--lumo-line-height-xs)";
  @NonNls public static final String LUMO_LINE_HEIGHT_S = "var(--lumo-line-height-s)";
  @NonNls public static final String LUMO_LINE_HEIGHT_M = "var(--lumo-line-height-m)";
  @NonNls public static final String LUMO_LINE_HEIGHT_L = "var(--lumo-line-height-l)";

  @NonNls public static final String FONT_WEIGHT_NORMAL = "400";
  @NonNls public static final String FONT_WEIGHT_MEDIUM = "500";
  @NonNls public static final String FONT_WEIGHT_SEMIBOLD = "600";
  @NonNls public static final String FONT_WEIGHT_BOLD = "700";

  // Text colors
  @NonNls public static final String LUMO_BODY_TEXT_COLOR = "var(--lumo-body-text-color)";
  @NonNls public static final String LUMO_HEADER_TEXT_COLOR = "var(--lumo-header-text-color)";
  @NonNls public static final String LUMO_SECONDARY_TEXT_COLOR = "var(--lumo-secondary-text-color)";
  @NonNls public static final String LUMO_DISABLED_TEXT_COLOR = "var(--lumo-disabled-text-color)";

  // Semantic colors
  @NonNls public static final String LUMO_PRIMARY_COLOR = "var(--lumo-primary-color)";
  @NonNls public static final String LUMO_PRIMARY_TEXT_COLOR = "var(--lumo-primary-text-color)";
  @NonNls public static final String LUMO_PRIMARY_CONTRAST_COLOR = "var(--lumo-primary-contrast-color)";

  @NonNls public static final String LUMO_SUCCESS_COLOR = "var(--lumo-success-color)";
  @NonNls public static final String LUMO_SUCCESS_TEXT_COLOR = "var(--lumo-success-text-color)";
  @NonNls public static final String LUMO_SUCCESS_CONTRAST_COLOR = "var(--lumo-success-contrast-color)";

  @NonNls public static final String LUMO_ERROR_COLOR = "var(--lumo-error-color)";
  @NonNls public static final String LUMO_ERROR_TEXT_COLOR = "var(--lumo-error-text-color)";
  @NonNls public static final String LUMO_ERROR_CONTRAST_COLOR = "var(--lumo-error-contrast-color)";

  @NonNls public static final String LUMO_WARNING_COLOR = "var(--lumo-warning-color)";
  @NonNls public static final String LUMO_WARNING_TEXT_COLOR = "var(--lumo-warning-text-color)";
  @NonNls public static final String LUMO_WARNING_CONTRAST_COLOR = "var(--lumo-warning-contrast-color)";

  // Surfaces and borders
  @NonNls public static final String LUMO_BASE_COLOR = "var(--lumo-base-color)";
  @NonNls public static final String LUMO_CONTRAST_5 = "var(--lumo-contrast-5pct)";
  @NonNls public static final String LUMO_CONTRAST_10 = "var(--lumo-contrast-10pct)";
  @NonNls public static final String LUMO_CONTRAST_20 = "var(--lumo-contrast-20pct)";
  @NonNls public static final String LUMO_CONTRAST_30 = "var(--lumo-contrast-30pct)";
  @NonNls public static final String LUMO_CONTRAST_40 = "var(--lumo-contrast-40pct)";
  @NonNls public static final String LUMO_CONTRAST_50 = "var(--lumo-contrast-50pct)";
  @NonNls public static final String LUMO_CONTRAST_60 = "var(--lumo-contrast-60pct)";
  @NonNls public static final String LUMO_CONTRAST_70 = "var(--lumo-contrast-70pct)";
  @NonNls public static final String LUMO_CONTRAST_80 = "var(--lumo-contrast-80pct)";
  @NonNls public static final String LUMO_CONTRAST_90 = "var(--lumo-contrast-90pct)";

  @NonNls public static final String LUMO_BORDER_RADIUS_S = "var(--lumo-border-radius-s)";
  @NonNls public static final String LUMO_BORDER_RADIUS_M = "var(--lumo-border-radius-m)";
  @NonNls public static final String LUMO_BORDER_RADIUS_L = "var(--lumo-border-radius-l)";

  @NonNls public static final String LUMO_CONTRAST_10_BORDER = "1px solid var(--lumo-contrast-10pct)";
  @NonNls public static final String LUMO_CONTRAST_20_BORDER = "1px solid var(--lumo-contrast-20pct)";

  // Elevation
  @NonNls public static final String LUMO_BOX_SHADOW_XS = "var(--lumo-box-shadow-xs)";
  @NonNls public static final String LUMO_BOX_SHADOW_S = "var(--lumo-box-shadow-s)";
  @NonNls public static final String LUMO_BOX_SHADOW_M = "var(--lumo-box-shadow-m)";
  @NonNls public static final String LUMO_BOX_SHADOW_L = "var(--lumo-box-shadow-l)";
  @NonNls public static final String LUMO_BOX_SHADOW_XL = "var(--lumo-box-shadow-xl)";

  // Interaction
  @NonNls public static final String LUMO_CLICKABLE_CURSOR = "var(--lumo-clickable-cursor)";

  // Häufige fixe Werte
  @NonNls public static final String LINE_HEIGHT_ONE = "1";
  @NonNls public static final String LINE_HEIGHT_ONE_POINT_TWO = "1.2";
  @NonNls public static final String LINE_HEIGHT_ONE_POINT_FIVE = "1.5";

  @NonNls public static final String SEVEN_HUNDRED_PX_REM = "43.75rem";
}