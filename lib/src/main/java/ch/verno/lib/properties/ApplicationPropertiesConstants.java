package ch.verno.lib.properties;

import org.jetbrains.annotations.NonNls;

public class ApplicationPropertiesConstants {

  @NonNls public static final String VERNO_RPC_URL = "${verno.rpc.url}";
  @NonNls public static final String VERNO_RPC_SECRET = "${RPC_INTERNAL_SECRET}";

  // returns a list of env-variable keys which gets loaded into the properties in the UI
  // with this we can prevent that all env-variables gets loaded into the properties and then could be abused accidentally
  // this constant does not have to contain ${...} - it's directly used in the environment.getProperties
  @NonNls public static final String VERNO_UI_LOADED_ENV = "verno.ui.loaded-env";

}
