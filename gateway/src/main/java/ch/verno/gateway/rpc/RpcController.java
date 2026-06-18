package ch.verno.gateway.rpc;

import ch.verno.contract.rpc.RpcRequest;
import ch.verno.contract.rpc.RpcResponse;
import ch.verno.server.config.rpc.RpcDispatcher;
import jakarta.annotation.Nonnull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rpc")
public class RpcController {

  @Nonnull private final RpcDispatcher dispatcher;

  public RpcController(@Nonnull final RpcDispatcher dispatcher) {
    this.dispatcher = dispatcher;
  }

  @PostMapping
  public RpcResponse call(@RequestBody @Nonnull final RpcRequest request) {
    try {
      final var result = dispatcher.dispatch(request);
      return RpcResponse.success(result);
    } catch (Exception exception) {
      return RpcResponse.error(exception.getMessage());
    }
  }
}