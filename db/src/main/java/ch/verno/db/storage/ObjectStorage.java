package ch.verno.db.storage;

import jakarta.annotation.Nonnull;

import java.io.InputStream;

public interface ObjectStorage {

  void put(@Nonnull String key, @Nonnull InputStream data, long size) throws Exception;

  @Nonnull
  InputStream get(@Nonnull String key) throws Exception;

  void delete(@Nonnull String key) throws Exception;

  boolean exists(@Nonnull String key) throws Exception;
}
