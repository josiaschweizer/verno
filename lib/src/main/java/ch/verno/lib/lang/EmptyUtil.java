package ch.verno.lib.lang;

import jakarta.annotation.Nullable;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

/**
 * Utility class for checking whether values should be treated as empty.
 */
public final class EmptyUtil {

  private EmptyUtil() {
  }

  /**
   * Checks whether the given string is null, empty or blank.
   *
   * @param value the string to check
   * @return true if the string is null, empty or blank
   */
  public static boolean isEmpty(@Nullable final String value) {
    return value == null || value.isBlank();
  }

  /**
   * Checks whether the given string is not empty.
   *
   * @param value the string to check
   * @return true if the string is not null, not empty and not blank
   */
  public static boolean isNotEmpty(@Nullable final String value) {
    return !isEmpty(value);
  }

  /**
   * Checks whether the given integer is null or zero.
   *
   * @param value the integer to check
   * @return true if the integer is null or zero
   */
  public static boolean isEmpty(@Nullable final Integer value) {
    return value == null || value == 0;
  }

  /**
   * Checks whether the given integer is not empty.
   *
   * @param value the integer to check
   * @return true if the integer is not null and not zero
   */
  public static boolean isNotEmpty(@Nullable final Integer value) {
    return !isEmpty(value);
  }

  /**
   * Checks whether the given integer is positive.
   *
   * @param value the integer to check
   * @return true if the integer is not null and greater than zero
   */
  public static boolean isPositive(@Nullable final Integer value) {
    return value != null && value > 0;
  }

  /**
   * Checks whether the given integer is not negative.
   *
   * @param value the integer to check
   * @return true if the integer is not null and greater than or equal to zero
   */
  public static boolean isNotNegative(@Nullable final Integer value) {
    return value != null && value >= 0;
  }

  /**
   * Checks whether the given long is null or zero.
   *
   * @param value the long to check
   * @return true if the long is null or zero
   */
  public static boolean isEmpty(@Nullable final Long value) {
    return value == null || value == 0L;
  }

  /**
   * Checks whether the given long is not empty.
   *
   * @param value the long to check
   * @return true if the long is not null and not zero
   */
  public static boolean isNotEmpty(@Nullable final Long value) {
    return !isEmpty(value);
  }

  /**
   * Checks whether the given long is positive.
   *
   * @param value the long to check
   * @return true if the long is not null and greater than zero
   */
  public static boolean isPositive(@Nullable final Long value) {
    return value != null && value > 0L;
  }

  /**
   * Checks whether the given long is not negative.
   *
   * @param value the long to check
   * @return true if the long is not null and greater than or equal to zero
   */
  public static boolean isNotNegative(@Nullable final Long value) {
    return value != null && value >= 0L;
  }

  /**
   * Checks whether the given double is null or zero.
   *
   * @param value the double to check
   * @return true if the double is null or zero
   */
  public static boolean isEmpty(@Nullable final Double value) {
    return value == null || value == 0D;
  }

  /**
   * Checks whether the given double is not empty.
   *
   * @param value the double to check
   * @return true if the double is not null and not zero
   */
  public static boolean isNotEmpty(@Nullable final Double value) {
    return !isEmpty(value);
  }

  /**
   * Checks whether the given double is positive.
   *
   * @param value the double to check
   * @return true if the double is not null and greater than zero
   */
  public static boolean isPositive(@Nullable final Double value) {
    return value != null && value > 0D;
  }

  /**
   * Checks whether the given double is not negative.
   *
   * @param value the double to check
   * @return true if the double is not null and greater than or equal to zero
   */
  public static boolean isNotNegative(@Nullable final Double value) {
    return value != null && value >= 0D;
  }

  /**
   * Checks whether the given boolean is null or false.
   *
   * @param value the boolean to check
   * @return true if the boolean is null or false
   */
  public static boolean isEmpty(@Nullable final Boolean value) {
    return value == null || !value;
  }

  /**
   * Checks whether the given boolean is not empty.
   *
   * @param value the boolean to check
   * @return true if the boolean is not null and true
   */
  public static boolean isNotEmpty(@Nullable final Boolean value) {
    return !isEmpty(value);
  }

  /**
   * Checks whether the given collection is null or contains no elements.
   *
   * @param value the collection to check
   * @return true if the collection is null or empty
   */
  public static boolean isEmpty(@Nullable final Collection<?> value) {
    return value == null || value.isEmpty();
  }

  /**
   * Checks whether the given collection is not empty.
   *
   * @param value the collection to check
   * @return true if the collection is not null and contains at least one element
   */
  public static boolean isNotEmpty(@Nullable final Collection<?> value) {
    return !isEmpty(value);
  }

  /**
   * Checks whether the given map is null or contains no entries.
   *
   * @param value the map to check
   * @return true if the map is null or empty
   */
  public static boolean isEmpty(@Nullable final Map<?, ?> value) {
    return value == null || value.isEmpty();
  }

  /**
   * Checks whether the given map is not empty.
   *
   * @param value the map to check
   * @return true if the map is not null and contains at least one entry
   */
  public static boolean isNotEmpty(@Nullable final Map<?, ?> value) {
    return !isEmpty(value);
  }

  /**
   * Checks whether the given object array is null or contains no elements.
   *
   * @param value the object array to check
   * @return true if the object array is null or empty
   */
  public static boolean isEmpty(@Nullable final Object[] value) {
    return value == null || value.length == 0;
  }

  /**
   * Checks whether the given object array is not empty.
   *
   * @param value the object array to check
   * @return true if the object array is not null and contains at least one element
   */
  public static boolean isNotEmpty(@Nullable final Object[] value) {
    return !isEmpty(value);
  }

  /**
   * Checks whether the given optional is null or contains no value.
   *
   * @param value the optional to check
   * @return true if the optional is null or empty
   */
  public static boolean isEmpty(@Nullable final Optional<?> value) {
    return value == null || value.isEmpty();
  }

  /**
   * Checks whether the given optional contains a value.
   *
   * @param value the optional to check
   * @return true if the optional is not null and contains a value
   */
  public static boolean isNotEmpty(@Nullable final Optional<?> value) {
    return !isEmpty(value);
  }
}