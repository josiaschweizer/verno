package ch.verno.common.rpc.auth;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public record InternalRpcPrincipal(@Nonnull String username,
                                   @Nullable Long tenantId) {
}