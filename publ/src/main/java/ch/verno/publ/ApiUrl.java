package ch.verno.publ;

import org.jetbrains.annotations.NonNls;

public class ApiUrl {

  @NonNls private static final String API = "/api";
  @NonNls private static final String INTERNAL = "/internal";
  @NonNls private static final String V1 = "/v1";
  public static final String VERSION = V1;

  public static final String BASE_API = API + VERSION;

  @NonNls public static final String TEMP_FILE_REPORT = INTERNAL + BASE_API + "/temp-file/report";
  @NonNls public static final String TEMP_FILE_IMPORT = INTERNAL + BASE_API + "/temp-file/import";
  @NonNls public static final String TEMP_FILE_EXPORT = INTERNAL + BASE_API + "/temp-file/export";
  @NonNls public static final String FILES = INTERNAL + BASE_API + "/files";

  @NonNls public static final String DEBUG = BASE_API + "/_debug";
  @NonNls public static final String TENANTS = BASE_API + "/tenants";
  @NonNls public static final String APPLICATION = BASE_API + "/application";
  @NonNls public static final String EMAIL = BASE_API + "/email";
  @NonNls public static final String WORKSPACE = BASE_API + "/workspace";

  @NonNls public static final String BILLING = BASE_API + "/billing";
  @NonNls public static final String BILLING_ACCESS_TOKEN = BILLING + "/access-token";
  @NonNls public static final String BILLING_WEBHOOK = BILLING + "/webhook";
  @NonNls public static final String BILLING_SESSION = BILLING + "/session";

  @NonNls public static final String COUNT = "/count";

  @NonNls public static final String DISPOSITION_ATTACHMENT = "?disposition=attachment";
  @NonNls public static final String DISPOSITION_INLINE = "?disposition=inline";

  @NonNls public static final String ENTRY_TOKEN = "entry?token=";
  @NonNls public static final String RESOLVE_ACCESS_TOKEN = "/resolve";
  @NonNls public static final String START_STRIPE_SESSION = "/start";
  @NonNls public static final String STRIP_WEBHOOK = "/stripe";

  @NonNls public static final String WORKSPACE_START = "/start";
  @NonNls public static final String WORKSPACE_SESSION_STATUS = WORKSPACE_START + "/{startSessionId}/status";
  @NonNls public static final String WORKSPACE_SESSION_EVENTS = WORKSPACE_START + "/{startSessionId}/events";
}
