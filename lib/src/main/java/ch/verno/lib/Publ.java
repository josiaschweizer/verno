package ch.verno.lib;

import java.util.regex.Pattern;

@SuppressWarnings("HardcodedFileSeparator")
public class Publ {

  public static final Integer ZERO = 0;
  public static final Long ZERO_LONG = 0L;
  public static final Integer ONE = 1;
  public static final Integer TWO = 2;
  public static final Integer THREE = 3;
  public static final Integer FOUR = 4;
  public static final Integer FIVE = 5;
  public static final Integer SIX = 6;
  public static final Integer SEVEN = 7;
  public static final Integer EIGHT = 8;
  public static final Integer NINE = 9;

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
  public static final String REQUIRED_STAR = "*";

  public static final String CMD_SIGN = "⌘";
  public static final String CTRL_MAC_SIGN = "⌃";
  public static final String SHIFT_SIGN = "⇧";
  public static final String OPTION_OPTION = "⌥";
  public static final String ARROW_UP = "↑";
  public static final String ARROW_DOWN = "↓";
  public static final String ARROW_LEFT = "←";
  public static final String ARROW_RIGHT = "→";
  public static final String ESC = "Esc";
  public static final String ENTER = "⏎";
  public static final String TAB = "⇥";

  public static final String S = "s";
  public static final String ID = "id";

  public static final String EIGHT_STARS = "*********";

  public static final Pattern KW_PATTERN = Pattern.compile("^KW(\\d{1,2})-(\\d{4})$");

  public static final String PNG = "png";
  public static final String JPEG = "jpeg";
  public static final String PDF = "pdf";
  public static final String SVG = "svg";

  public static final String BOM_ZWNBSP = "\uFEFF";
  public static final String BOM = BOM_ZWNBSP;
  public static final String ZWNBSP = BOM_ZWNBSP;
  public static final String ZERO_WIDTH_SPACE = "\u200B";
  public static final String ZERO_WIDTH_NON_JOINER = "\u200C";
  public static final String ZERO_WIDTH_JOINER = "\u200D";


  public static class Char {
    public static final char SEMICOLON = ';';
    public static final char COMMA = ',';

  }

}
