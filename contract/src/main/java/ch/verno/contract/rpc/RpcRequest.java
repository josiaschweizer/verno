package ch.verno.contract.rpc;

import jakarta.annotation.Nonnull;
import tools.jackson.databind.JsonNode;

import java.util.List;

public record RpcRequest(@Nonnull String endpoint,
                         @Nonnull String method,
                         @Nonnull List<JsonNode> arguments) {

}