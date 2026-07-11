package ch.verno.lib.properties;

import org.jetbrains.annotations.NonNls;

public class ApplicationPropertiesConstants {

  @NonNls public static final String RPC_URL = "${verno.rpc.url}";
  @NonNls public static final String RPC_SECRET = "${RPC_INTERNAL_SECRET}";
  @NonNls public static final String API_RESOURCE_ACCESS_TOKEN = "${API_RESOURCE_ACCESS_TOKEN}";

  // returns a list of env-variable keys which gets loaded into the properties in the UI
  // with this we can prevent that all env-variables gets loaded into the properties and then could be abused accidentally
  // this constant does not have to contain ${...} - it's directly used in the environment.getProperties
  @NonNls public static final String UI_LOADED_ENV = "verno.ui.loaded-env";

  @NonNls public static final String GATEWAY_LOADED_ENV = "verno.gateway.loaded-env";

}
