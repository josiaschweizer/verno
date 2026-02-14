package ch.verno.publ;

public class ApiUrl {

  private static final String API = "/api";
  private static final String INTERNAL = "/internal";
  private static final String V1 = "/v1";
  public static final String VERSION = V1;

  public static final String BASE_API = API + VERSION;

  public static final String TEMP_FILE_REPORT = INTERNAL + BASE_API + "/temp-file/report";
  public static final String TEMP_FILE_IMPORT = INTERNAL + BASE_API + "/temp-file/import";
  public static final String TEMP_FILE_EXPORT = INTERNAL + BASE_API + "/temp-file/export";
  public static final String FILES = INTERNAL + BASE_API + "/files";

  public static final String DEBUG = BASE_API + "/_debug";
  public static final String TENANTS = BASE_API + "/tenants";
  public static final String APPLICATION = BASE_API + "/application";

  public static final String COUNT = "/count";

  public static final String DISPOSITION_ATTACHMENT = "?disposition=attachment";
  public static final String DISPOSITION_INLINE = "?disposition=inline";
}
