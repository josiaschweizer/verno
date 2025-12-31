package ch.verno.common.util;

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
  public static final String UNDERSCORE = "_";

  public static final String S = "s";
  public static final String ID = "id";


  public static final Pattern KW_PATTERN = Pattern.compile("^KW(\\d{1,2})-(\\d{4})$");
}
