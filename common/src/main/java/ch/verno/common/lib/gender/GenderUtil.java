package ch.verno.common.lib.gender;

import ch.verno.lib.language.Language;
import ch.verno.lib.New;

import javax.annotation.Nonnull;
import java.util.List;

public class GenderUtil {

  @Nonnull
  public static String translateToInternalGender(@Nonnull String name) {
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

  @Nonnull
  public static String getDescriptionFromLanguage(@Nonnull Gender gender,
                                                  @Nonnull Language language) {
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
