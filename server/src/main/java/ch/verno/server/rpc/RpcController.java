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

  @PostMapping
  public RpcResponse call(@RequestBody @Nonnull final RpcRequest request) {
    try {
      final var result = dispatcher.dispatch(request);
      return RpcResponse.success(result);
    } catch (final java.lang.reflect.InvocationTargetException exception) {
      final var targetException = exception.getTargetException();

      return RpcResponse.error(
              targetException.getClass().getSimpleName()
                      + ": "
                      + targetException.getMessage()
      );
    } catch (final Exception exception) {
      return RpcResponse.error(
              exception.getClass().getSimpleName()
                      + ": "
                      + exception.getMessage()
      );
    }
  }
}