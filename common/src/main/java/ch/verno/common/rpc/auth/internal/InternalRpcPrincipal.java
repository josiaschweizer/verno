package ch.verno.common.rpc.auth.internal;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public record InternalRpcPrincipal(@Nonnull String username,
                                   @Nullable Long tenantId) {
}