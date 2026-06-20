package ch.verno.server.mail;

import jakarta.annotation.Nonnull;
import org.jsoup.Jsoup;

public class MailContentUtil {

  public static boolean looksLikeHtml(@Nonnull final String input) {
    if (input.isBlank()) {
      return false;
    }

    final var doc = Jsoup.parse(input);
    return doc.body().childrenSize() > 0;
  }

}
