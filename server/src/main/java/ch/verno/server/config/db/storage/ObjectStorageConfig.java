package ch.verno.server.config.db.storage;

import ch.verno.db.storage.FileSystemObjectStorage;
import ch.verno.db.storage.ObjectStorage;
import jakarta.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ObjectStorageConfig {

  @Bean
  @Nonnull
  public ObjectStorage objectStorage(@Value("${files.storage.root:./data/files}") @Nonnull final String storageRoot) { //TODO USE CONFIG PROVIDER
    return new FileSystemObjectStorage(storageRoot);
  }
}