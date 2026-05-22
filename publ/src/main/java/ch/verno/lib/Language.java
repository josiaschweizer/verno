package ch.verno.lib;

import jakarta.annotation.Nonnull;

public enum Language {
  AF("af"), // Afrikaans
  SQ("sq"), // Albanian
  AM("am"), // Amharic
  AR("ar"), // Arabic
  HY("hy"), // Armenian
  AZ("az"), // Azerbaijani
  EU("eu"), // Basque
  BE("be"), // Belarusian
  BN("bn"), // Bengali
  BS("bs"), // Bosnian
  BG("bg"), // Bulgarian
  CA("ca"), // Catalan
  ZH("zh"), // Chinese
  HR("hr"), // Croatian
  CS("cs"), // Czech
  DA("da"), // Danish
  NL("nl"), // Dutch
  EN("en"), // English
  ET("et"), // Estonian
  FI("fi"), // Finnish
  FR("fr"), // French
  GL("gl"), // Galician
  KA("ka"), // Georgian
  DE("de"), // German
  EL("el"), // Greek
  GU("gu"), // Gujarati
  HE("he"), // Hebrew
  HI("hi"), // Hindi
  HU("hu"), // Hungarian
  IS("is"), // Icelandic
  ID("id"), // Indonesian
  GA("ga"), // Irish
  IT("it"), // Italian
  JA("ja"), // Japanese
  KN("kn"), // Kannada
  KK("kk"), // Kazakh
  KO("ko"), // Korean
  LV("lv"), // Latvian
  LT("lt"), // Lithuanian
  MK("mk"), // Macedonian
  MS("ms"), // Malay
  MT("mt"), // Maltese
  MR("mr"), // Marathi
  MN("mn"), // Mongolian
  NE("ne"), // Nepali
  NO("no"), // Norwegian
  FA("fa"), // Persian
  PL("pl"), // Polish
  PT("pt"), // Portuguese
  PA("pa"), // Punjabi
  RO("ro"), // Romanian
  RU("ru"), // Russian
  SR("sr"), // Serbian
  SK("sk"), // Slovak
  SL("sl"), // Slovenian
  ES("es"), // Spanish
  SW("sw"), // Swahili
  SV("sv"), // Swedish
  TA("ta"), // Tamil
  TE("te"), // Telugu
  TH("th"), // Thai
  TR("tr"), // Turkish
  UK("uk"), // Ukrainian
  UR("ur"), // Urdu
  UZ("uz"), // Uzbek
  VI("vi"), // Vietnamese
  CY("cy"), // Welsh
  XH("xh"), // Xhosa
  ZU("zu"); // Zulu

  @Nonnull private final String code;

  Language(@Nonnull String code) {
    this.code = code;
  }

  @Nonnull
  public static Language fromCode(@Nonnull String code) {
    for (Language lang : Language.values()) {
      if (lang.getCode().equalsIgnoreCase(code)) {
        return lang;
      }
    }

    return null;
  }

  @Nonnull
  public String getCode() {
    return code;
  }
}