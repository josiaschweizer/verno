package ch.verno.contract.rpc;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.annotation.Nonnull;

import java.util.List;

public record RpcRequest(@Nonnull String endpoint,
                         @Nonnull String method,
                         @Nonnull List<JsonNode> arguments) {

}