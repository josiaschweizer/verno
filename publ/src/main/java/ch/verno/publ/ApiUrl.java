package ch.verno.publ;

public class ApiUrl {

  public static final String BASE_API = "/api/v1";

  public static final String TEMP_FILE_REPORT = BASE_API + "/temp-file/report";
  public static final String TEMP_FILE_IMPORT = BASE_API + "/temp-file/import";
  public static final String TEMP_FILE_EXPORT = BASE_API + "/temp-file/export";

  public static final String DISPOSITION_INLINE = "?disposition=inline";
  public static final String DISPOSITION_ATTACHMENT = "?disposition=attachment";

}
