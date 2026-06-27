package ch.verno.server.rpc.properties.user;

import ch.verno.contract.endpoint.properties.env.EnvResource;
import ch.verno.contract.endpoint.properties.user.UserResource;
import ch.verno.contract.rpc.RpcResource;
import org.springframework.security.core.context.SecurityContextHolder;

@SuppressWarnings("unused")
@RpcResource(EnvResource.class)
public class UserResourceImpl implements UserResource {

  @Override
  public boolean logout() {
    SecurityContextHolder.clearContext();
    return true;
  }
}
