package ch.verno.common.lib.url;

import ch.verno.common.lib.api.ApiQueryParam;
import ch.verno.lib.Publ;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public final class UrlBuilder {

  @Nonnull private final String baseUrl;
  @Nonnull private final String path;

  @Nonnull private final StringBuilder queryString;

  private UrlBuilder(@Nonnull final String baseUrl,
                     @Nonnull final String path) {
    this.baseUrl = baseUrl;
    this.path = path;

    this.queryString = new StringBuilder();
  }

  @Nonnull
  public static UrlBuilder of(@Nonnull final String baseUrl, @Nonnull final String path) {
    return new UrlBuilder(baseUrl, path);
  }

  @Nonnull
  public UrlBuilder withQueryParam(@Nonnull final String key, @Nullable final String value) {
    if (value == null || value.isBlank()) {
      return this;
    }

    queryString.append(queryString.isEmpty() ? Publ.Char.QUESTION_MARK : Publ.Char.AMPERSAND);
    queryString.append(key).append(Publ.Char.EQUALS).append(value);
    return this;
  }

  @Nonnull
  public UrlBuilder withAccessToken(@Nullable final String accessToken) {
    return withQueryParam(ApiQueryParam.ACCESS_TOKEN, accessToken);
  }

  @Nonnull
  public UrlBuilder withDisposition(@Nullable final String disposition) {
    return withQueryParam(ApiQueryParam.DISPOSITION, disposition);
  }

  @Nonnull
  public String build() {
    final var base = UrlUtil.buildSafeUrl(baseUrl, path);
    return base + queryString;
  }
}