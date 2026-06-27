package ch.verno.server.rpc;

import ch.verno.contract.rpc.RpcRequest;
import ch.verno.contract.rpc.RpcResponse;
import ch.verno.server.config.rpc.RpcDispatcher;
import jakarta.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rpc")
public class RpcController {

  @Nonnull private static final Logger LOGGER = LoggerFactory.getLogger(RpcController.class);

  @Nonnull private final RpcDispatcher dispatcher;

  public RpcController(@Nonnull final RpcDispatcher dispatcher) {
    this.dispatcher = dispatcher;
  }

  @Nonnull
  @PostMapping
  public RpcResponse call(@RequestBody @Nonnull final RpcRequest request) {
    try {
      final var result = dispatcher.dispatch(request);
      return RpcResponse.success(result);
    } catch (Exception exception) {
      LOGGER.error(
              "RPC call failed: {}#{}",
              request.endpoint(),
              request.method(),
              exception
      );

      final var message = exception.getMessage() != null
              ? exception.getMessage()
              : exception.getClass().getName();

      return RpcResponse.error(message);
    }
  }
}