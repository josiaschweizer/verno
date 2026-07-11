package ch.verno.common.lib.gender;

import ch.verno.lib.New;
import ch.verno.lib.lib.language.Language;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class GenderUtil {

  /**
   * Returns the gender for the given internal gender name.
   *
   * @param internalName the internal gender name, for example {@code MALE} or {@code FEMALE}
   * @return the matching gender
   * @throws IllegalStateException if no matching gender exists
   */
  @Nonnull
  public static Gender getGenderFromInternalName(@Nonnull final String internalName) {
    for (final var value : Gender.values()) {
      if (value.name().equalsIgnoreCase(internalName)) {
        return value;
      }
    }

    throw new IllegalStateException("Unknown internal name: " + internalName);
  }

  /**
   * Returns the gender for the given translated or user-facing name.
   *
   * @param name the gender name to resolve
   * @return the matching gender, or {@code null} if no gender could be resolved
   */
  @Nullable
  public static Gender getGenderFromName(@Nonnull final String name) {
    if (getMaleGenderNames().contains(name)) {
      return Gender.MALE;
    } else if (getFemaleGenderNames().contains(name)) {
      return Gender.FEMALE;
    }

    return null;
  }

  /**
   * Converts a translated or user-facing gender name to the internal gender name.
   *
   * @param name the gender name to translate
   * @return the internal gender name, or the original name if no match was found
   */
  @Nonnull
  public static String translateToInternalGender(@Nonnull final String name) {
    final var maleGenderName = getMaleGenderNames();
    final var femaleGenderName = getFemaleGenderNames();

    for (final var maleName : maleGenderName) {
      if (maleName.equalsIgnoreCase(name)) {
        return GenderConstants.INTERNAL_MALE;
      }
    }

    for (final var femaleName : femaleGenderName) {
      if (femaleName.equalsIgnoreCase(name)) {
        return GenderConstants.INTERNAL_FEMALE;
      }
    }

    return name;
  }

  /**
   * Returns the gender description in the requested language.
   *
   * @param gender the gender to describe
   * @param language the language used for the description
   * @return the translated gender description
   */
  @Nonnull
  public static String getDescriptionFromLanguage(@Nonnull final Gender gender,
                                                  @Nonnull final Language language) {
    if (gender.equals(Gender.MALE)) {
      return switch (language) {
        case DE -> GenderConstants.MALE_GERMAN;
        case FR -> GenderConstants.MALE_FRENCH;
        default -> GenderConstants.INTERNAL_MALE;
      };
    } else {
      return switch (language) {
        case DE -> GenderConstants.FEMALE_GERMAN;
        case FR -> GenderConstants.FEMALE_FRENCH;
        default -> GenderConstants.INTERNAL_FEMALE;
      };
    }
  }

  /**
   * Returns all supported male gender names.
   *
   * @return known male gender names in different languages and formats
   */
  @Nonnull
  private static List<String> getMaleGenderNames() {
    final var list = New.<String>arrayList();

    // Internal / technical
    list.add(GenderConstants.INTERNAL_MALE);
    list.add("Male");
    list.add("MALE");
    list.add("M");
    list.add("m");

    // Deutsch
    list.add(GenderConstants.MALE_GERMAN);
    list.add("männlich");
    list.add("Maennlich");
    list.add("maennlich");
    list.add("Mann");
    list.add("mann");
    list.add("Herr");
    list.add("herr");
    list.add("Hr");
    list.add("Hr.");
    list.add("H");
    list.add("h");
    list.add("m.");
    list.add("männl.");
    list.add("maennl.");
    list.add("männliche Person");
    list.add("männlicher Teilnehmer");
    list.add("männlicher Kontakt");

    // Französisch
    list.add(GenderConstants.MALE_FRENCH);
    list.add("masculin");
    list.add("Homme");
    list.add("homme");
    list.add("Monsieur");
    list.add("monsieur");
    list.add("M.");
    list.add("Mr");
    list.add("M");
    list.add("masc.");
    list.add("hom.");
    list.add("personne masculine");
    list.add("participant masculin");
    list.add("contact masculin");

    // Englisch
    list.add("male");
    list.add("man");
    list.add("gentleman");
    list.add("mr");
    list.add("mr.");
    list.add("mister");
    list.add("sir");
    list.add("boy");
    list.add("masculine");
    list.add("m.");
    list.add("male person");
    list.add("male participant");
    list.add("male contact");

    return list;
  }

  /**
   * Returns all supported female gender names.
   *
   * @return known female gender names in different languages and formats
   */
  @Nonnull
  private static List<String> getFemaleGenderNames() {
    final var list = New.<String>arrayList();

    // Internal / technical
    list.add(GenderConstants.INTERNAL_FEMALE);
    list.add("Female");
    list.add("FEMALE");
    list.add("F");
    list.add("f");
    list.add("W");
    list.add("w");

    // Deutsch
    list.add(GenderConstants.FEMALE_GERMAN);
    list.add("weiblich");
    list.add("Frau");
    list.add("frau");
    list.add("Dame");
    list.add("dame");
    list.add("Fr");
    list.add("Fr.");
    list.add("Fräulein");
    list.add("fraeulein");
    list.add("weibl.");
    list.add("w.");
    list.add("f.");
    list.add("weibliche Person");
    list.add("weiblicher Teilnehmer");
    list.add("weibliche Teilnehmerin");
    list.add("weiblicher Kontakt");
    list.add("weibliche Kontaktperson");

    // Französisch
    list.add(GenderConstants.FEMALE_FRENCH);
    list.add("féminin");
    list.add("Feminin");
    list.add("feminin");
    list.add("Femme");
    list.add("femme");
    list.add("Madame");
    list.add("madame");
    list.add("Mme");
    list.add("Mme.");
    list.add("Mademoiselle");
    list.add("mademoiselle");
    list.add("Mlle");
    list.add("Mlle.");
    list.add("fém.");
    list.add("fem.");
    list.add("personne féminine");
    list.add("participante féminine");
    list.add("contact féminin");

    // Englisch
    list.add("female");
    list.add("woman");
    list.add("lady");
    list.add("mrs");
    list.add("mrs.");
    list.add("ms");
    list.add("ms.");
    list.add("miss");
    list.add("madam");
    list.add("ma'am");
    list.add("girl");
    list.add("feminine");
    list.add("f.");
    list.add("female person");
    list.add("female participant");
    list.add("female contact");

    return list;
  }

}