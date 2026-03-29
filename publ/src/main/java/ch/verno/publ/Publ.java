package ch.verno.publ;

import java.util.regex.Pattern;

public class Publ {

  public static final Integer ZERO = 0;
  public static final Long ZERO_LONG = 0L;

  public static final String EMPTY_STRING = "";
  public static final String SPACE = " ";

  public static final String QUESTION_MARK = "?";
  public static final String DASH = "-";
  public static final String SLASH = "/";
  public static final String COMMA = ",";
  public static final String EQUALS = "=";
  public static final String PLUS = "+";
  public static final String SIMPLE_QUOTE = "'";
  public static final String DOT = ".";
  public static final String COLON = ":";
  public static final String UNDERSCORE = "_";
  public static final String AT = "@";
  public static final String HASH = "#";
  public static final String LEFT_PARENTHESIS = "(";
  public static final String RIGHT_PARENTHESIS = ")";
  public static final String MINUS = "−";
  public static final String LEFT_SINGLE_ANGLE_QUOTATION_MARK = "‹";
  public static final String RIGHT_SINGLE_ANGLE_QUOTATION_MARK = "›";

  public static final String CMD_SIGN = "\u2318";
  public static final String CTRL_MAC_SIGN = "\u2303";
  public static final String SHIFT_SIGN = "\u21E7";
  public static final String OPTION_OPTION = "\u2325";
  public static final String ARROW_UP = "\u2191";
  public static final String ARROW_DOWN = "\u2193";
  public static final String ARROW_LEFT = "\u2190";
  public static final String ARROW_RIGHT = "\u2192";
  public static final String ESC = "Esc";
  public static final String ENTER = "\u23CE";
  public static final String TAB = "\u21E5";

  public static final String S = "s";
  public static final String ID = "id";

  public static final String EIGHT_STARS = "*********";

  public static final Pattern KW_PATTERN = Pattern.compile("^KW(\\d{1,2})-(\\d{4})$");

  public static final String PNG = "png";
  public static final String JPEG = "jpeg";
  public static final String PDF = "pdf";
  public static final String SVG = "svg";
}
