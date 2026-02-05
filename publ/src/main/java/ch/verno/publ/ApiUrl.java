package ch.verno.publ;

public class ApiUrl {

  private static final String API = "/api";
  private static final String V1 = "/v1";
  public static final String VERSION = V1;

  public static final String BASE_API = API + VERSION;

  public static final String TEMP_FILE_REPORT = BASE_API + "/temp-file/report";
  public static final String TEMP_FILE_IMPORT = BASE_API + "/temp-file/import";
  public static final String TEMP_FILE_EXPORT = BASE_API + "/temp-file/export";

  public static final String DEBUG = BASE_API + "/_debug";
  public static final String TENANTS = BASE_API + "/tenants";

  public static final String DISPOSITION_INLINE = "?disposition=inline";
  public static final String DISPOSITION_ATTACHMENT = "?disposition=attachment";

}
