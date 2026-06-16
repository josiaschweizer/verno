package ch.verno.publ;

import org.jetbrains.annotations.NonNls;
import org.springframework.http.MediaType;

@SuppressWarnings("HardcodedFileSeparator")
public class VernoConstants {

  public static final int DEFAULT_SMTP_PORT = 587;

  public static final String ADMIN_ROLE = "ADMIN";
  public static final String STATUS_CREATED = "CREATED";

  @NonNls public static final String LOCALHOST = "localhost";
  public static final String IP_172_0_0_1 = "127.0.0.1";
  public static final String URL_DOUBLE_POINT_1 = "::1";
  @NonNls public static final String URL_DOUBLE_POINT_SLASH_SLASH = "://";
  @NonNls public static final String HTTP = "http";
  public static final String HTTP_INCL = HTTP + URL_DOUBLE_POINT_SLASH_SLASH;
  @NonNls public static final String HTTPS = "https";
  public static final String HTTPS_INCL = HTTPS + URL_DOUBLE_POINT_SLASH_SLASH;

  @NonNls public static final String ATTR_PUBLIC_NO_TENANT = "PUBLIC_NO_TENANT";
  @NonNls public static final String ATTR_TENANT_ID = "TENANT_ID";
  @NonNls public static final String AUTO_LOGIN_ATTEMPTED = "AUTO_LOGIN_ATTEMPTED";
  @NonNls public static final String SPRING_SECURITY_CONTEXT = "SPRING_SECURITY_CONTEXT";

  @NonNls public static final String TEMP_FILE_BASE_DIR = "verno-temp-";
  @NonNls public static final String FILE_BASE_DIR = "verno-files-";

  public static final MediaType OCTET_STREAM = MediaType.APPLICATION_OCTET_STREAM;

  @NonNls public static final String X_TENANT = "X-Mandant";

  public static final Integer MAX_MAIL_BATCH_SIZE = 100;
  public static final Double MAIL_SEND_DELAY_MS = 500.0;

  public static final String SESSION_STRIPE_PRICE_ID = "stripePriceId";
  public static final String SESSION_USER_ID = "userId";
  public static final String SESSION_TENANT_ID = "tenantId";

  // OS Constants
  @NonNls public static final String IPHONE = "iphone";
  @NonNls public static final String IPAD = "ipad";
  @NonNls public static final String IPOD = "ipod";
  @NonNls public static final String IOS = "ios";
  @NonNls public static final String ANDROID = "android";
  @NonNls public static final String MOBILE = "mobile";
  @NonNls public static final String WINDOWS = "windows";
  @NonNls public static final String MAC_OS_X = "mac os x";
  @NonNls public static final String MACINTOSH = "macintosh";
  @NonNls public static final String DARWIN = "darwin";
  @NonNls public static final String X_11 = "x11";
  @NonNls public static final String LINUX = "linux";
}
