package ch.verno.common.db.enums;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.annotation.Nonnull;

import java.io.IOException;

public enum Role {
  User(0, "User", 3),
  Superuser(1, "Superuser", 2),
  Administrator(2, "Administrator", 1),
  ;

  private final int id;
  @Nonnull private final String name;
  private final int hierarchyLevel;

  Role(final int id,
       @Nonnull final String name,
       final int hierarchyLevel) {
    this.id = id;
    this.name = name;
    this.hierarchyLevel = hierarchyLevel;
  }

  @Nonnull
  public static Role getInstance(final int id) {
    for (Role role : values()) {
      if (role.getId() == id) {
        return role;
      }
    }

    throw new IllegalArgumentException("role does not exist: " + id);
  }

  static class RoleDeserializer extends JsonDeserializer<Role> {

    @Override
    public Role deserialize(@Nonnull final JsonParser parser,
                            @Nonnull final DeserializationContext context) throws IOException {
      final var node = (JsonNode) parser.getCodec().readTree(parser);
      final var value = node.get("id").asInt();
      return getInstance(value);
    }
  }

  public int getId() {
    return id;
  }

  @Nonnull
  public String getName() {
    return name;
  }

  public int getHierarchyLevel() {
    return hierarchyLevel;
  }
}
