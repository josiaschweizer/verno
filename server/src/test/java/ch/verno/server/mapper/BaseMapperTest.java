package ch.verno.server.mapper;

import ch.verno.publ.Publ;
import jakarta.annotation.Nullable;
import org.junit.jupiter.api.Assertions;

import javax.annotation.Nonnull;
import java.time.OffsetDateTime;

public abstract class BaseMapperTest {

  protected static final OffsetDateTime CREATED_AT = OffsetDateTime.now();
  protected static final OffsetDateTime UPDATED_AT = OffsetDateTime.now();
  protected static final OffsetDateTime EXPIRES_AT = OffsetDateTime.now();

  protected void assertEmptyString(@Nonnull final String value) {
    Assertions.assertEquals(Publ.EMPTY_STRING, value);
  }

  protected void assertNullId(@Nullable final Long value) {
    Assertions.assertNull(value);
  }

  protected void assertNotNullId(@Nullable final Long value) {
    Assertions.assertNotNull(value);
  }
}