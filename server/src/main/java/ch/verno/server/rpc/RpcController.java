package ch.verno.server.rpc;

import ch.verno.common.exceptions.db.DBNotFoundException;
import ch.verno.contract.rpc.RpcRequest;
import ch.verno.contract.rpc.RpcResponse;
import ch.verno.lib.exception.stripe.StripeTokenException;
import ch.verno.server.config.rpc.RpcDispatcher;
import jakarta.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.InvocationTargetException;

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
    } catch (final InvocationTargetException exception) {
      return toErrorResponse(exception.getTargetException());
    } catch (final Exception exception) {
      return toErrorResponse(exception);
    }
  }

  @Nonnull
  private RpcResponse toErrorResponse(@Nonnull final Throwable throwable) {
    LOGGER.error("RPC call failed", throwable);

    if (throwable instanceof DBNotFoundException notFound) {
      return RpcResponse.error(null, notFound.getMessage());
    } else if (throwable instanceof StripeTokenException stripeTokenException) {
      return RpcResponse.error(HttpStatus.UNAUTHORIZED.value(), stripeTokenException.getMessage());
    }

    return RpcResponse.error("INTERNAL_ERROR", null);
  }
}