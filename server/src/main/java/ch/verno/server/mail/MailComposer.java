package ch.verno.server.mail;

import ch.verno.contract.mail.MailContentDto;
import ch.verno.contract.mail.MailDto;
import jakarta.annotation.Nonnull;
import org.simplejavamail.api.email.Email;
import org.simplejavamail.api.email.EmailPopulatingBuilder;
import org.simplejavamail.email.EmailBuilder;

public class MailComposer {

  @Nonnull
  public static Email createMail(@Nonnull final MailDto mailDto) {
    final var builder = EmailBuilder.startingBlank()
            .from(mailDto.from())
            .to(mailDto.recipient())
            .withSubject(mailDto.recipient());

    configFrom(mailDto, builder);
    configContent(mailDto, builder);

    return builder.buildEmail();
  }

  private static void configFrom(@Nonnull final MailDto mailDto,
                                 @Nonnull final EmailPopulatingBuilder builder) {
    if (mailDto.fromName() == null) {
      builder.from(mailDto.from());
    } else {
      builder.from(mailDto.fromName(), mailDto.from());
    }
  }

  private static void configContent(@Nonnull final MailDto mailDto,
                                    @Nonnull final EmailPopulatingBuilder builder) {
    final var contentDto = mailDto.contentDto();
    if (contentDto.mailContentType() != null) {
      if (contentDto.mailContentType() == MailContentDto.MailContentType.HTML) {
        builder.withHTMLText(contentDto.content());
      } else {
        builder.withPlainText(contentDto.content());
      }
    } else if (MailContentUtil.looksLikeHtml(contentDto.content())) {
      builder.withHTMLText(contentDto.content());
    } else {
      builder.withPlainText(contentDto.content());
    }
  }


}
