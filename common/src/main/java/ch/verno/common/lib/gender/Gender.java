package ch.verno.common.lib.gender;

import jakarta.annotation.Nonnull;

public enum Gender {
  MALE(InternalGenderConstants.INTERNAL_MALE),
  FEMALE(InternalGenderConstants.INTERNAL_FEMALE);

  @Nonnull private final String internalName;

  Gender(@Nonnull final String internalName) {
    this.internalName = internalName;
  }

  @Nonnull
  public String getInternalName() {
    return internalName;
  }
}
