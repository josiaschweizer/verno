package ch.verno.api.endpoints.external;

import ch.verno.api.base.BaseController;
import ch.verno.common.api.dto.exernal.email.SendEmailRequest;
import ch.verno.common.api.dto.exernal.email.SendEmailResponse;
import ch.verno.common.gate.server.MailServerGate;
import ch.verno.common.server.mail.MailConfigOptions;
import ch.verno.common.server.mail.MailOrigin;
import ch.verno.publ.ApiUrl;
import jakarta.annotation.Nonnull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiUrl.EMAIL)
public class EmailController extends BaseController {

  @Nonnull private final MailServerGate mailServerGate;

  public EmailController(@Nonnull final MailServerGate mailServerGate) {
    this.mailServerGate = mailServerGate;
  }

  @PostMapping
  public SendEmailResponse sendEmail(@RequestBody @Nonnull final SendEmailRequest request) {
    mailServerGate.sendMail(
            request.from(),
            request.to(),
            request.subject(),
            request.message(),
            new MailConfigOptions(MailOrigin.ENV)
    );
    return SendEmailResponse.forSuccess();
  }

}
