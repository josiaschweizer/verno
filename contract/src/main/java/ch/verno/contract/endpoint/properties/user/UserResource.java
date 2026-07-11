package ch.verno.contract.endpoint.properties.user;

import ch.verno.contract.rpc.RpcEndpoint;

@RpcEndpoint
public interface UserResource {

  /**
   * logout current user on backend (SecurityContextHolder)
   */
  boolean logout();

}
