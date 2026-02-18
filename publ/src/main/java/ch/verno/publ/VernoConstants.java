package ch.verno.publ;

import org.springframework.http.MediaType;

public class VernoConstants {

  private VernoConstants() {
  }

  public static final String ADMIN_ROLE = "ADMIN";
  public static final String STATUS_CREATED = "CREATED";

  public static final String LOCALHOST = "localhost";
  public static final String IP_172_0_0_1 = "127.0.0.1";
  public static final String URL_DOUBLE_POINT_1 = "::1";

  public static final String ATTR_PUBLIC_NO_TENANT = "PUBLIC_NO_TENANT";
  public static final String ATTR_TENANT_ID = "TENANT_ID";
  public static final String AUTO_LOGIN_ATTEMPTED = "AUTO_LOGIN_ATTEMPTED";
  public static final String SPRING_SECURITY_CONTEXT = "SPRING_SECURITY_CONTEXT";

  public static final String TEMP_FILE_BASE_DIR = "verno-temp-";
  public static final String FILE_BASE_DIR = "verno-files-";

  public static final MediaType OCTET_STREAM = MediaType.APPLICATION_OCTET_STREAM;

  public static final String X_TENANT = "X-Mandant";
}
