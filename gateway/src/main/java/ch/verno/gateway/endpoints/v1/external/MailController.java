package ch.verno.gateway.endpoints.v1.external;

import ch.verno.contract.api.exernal.email.SendMailRequest;
import ch.verno.contract.api.exernal.email.SendMailResponse;
import ch.verno.contract.endpoint.mail.MailResource;
import ch.verno.common.lib.api.ApiUrl;
import ch.verno.contract.mail.MailConfigOptions;
import ch.verno.contract.mail.MailContentDto;
import ch.verno.contract.mail.MailDto;
import ch.verno.gateway.base.BaseController;
import ch.verno.rpc.rpc.RpcFactory;
import jakarta.annotation.Nonnull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiUrl.EMAIL)
public class MailController extends BaseController {

  @Nonnull private final MailResource mailResource;

  public MailController(@Nonnull final RpcFactory rpcFactory) {
    this.mailResource = rpcFactory.create(MailResource.class);
  }

  @PostMapping
  public SendMailResponse sendEmail(@RequestBody @Nonnull final SendMailRequest request) {
    final var mailContentDto = MailContentDto.plain(request.subject(), request.message());
    final var mailConfigOptions = new MailConfigOptions(MailConfigOptions.MailOrigin.ENV);

    final var dto = new MailDto(
            null,
            request.from(),
            request.to(),
            mailContentDto,
            mailConfigOptions
    );
    mailResource.sendMail(dto);
    return SendMailResponse.forSuccess();
  }

}
