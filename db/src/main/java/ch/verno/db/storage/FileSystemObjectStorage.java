package ch.verno.db.storage;

import jakarta.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.nio.file.*;

@Component
public class FileSystemObjectStorage implements ObjectStorage {

  private final Path root;

  public FileSystemObjectStorage(@Value("${files.storage.root:./data/files}") @Nonnull final String root) {
    this.root = Paths.get(root).toAbsolutePath().normalize();
  }

  @Override
  public void put(@Nonnull final String key,
                  @Nonnull final InputStream data,
                  final long size) throws Exception {
    Files.createDirectories(root);
    Path target = root.resolve(key).normalize();
    if (!target.startsWith(root)) {
      throw new SecurityException("Invalid storage key");
    }

    Files.createDirectories(target.getParent());
    Files.copy(data, target, StandardCopyOption.REPLACE_EXISTING);
  }

  @Nonnull
  @Override
  public InputStream get(@Nonnull final String key) throws Exception {
    Path target = root.resolve(key).normalize();
    if (!target.startsWith(root)) {
      throw new SecurityException("Invalid storage key");
    }

    return Files.newInputStream(target, StandardOpenOption.READ);
  }

  @Override
  public void delete(@Nonnull final String key) throws Exception {
    final var target = root.resolve(key).normalize();
    if (!target.startsWith(root)) {
      throw new SecurityException("Invalid storage key");
    }

    Files.deleteIfExists(target);
  }

  @Override
  public boolean exists(@Nonnull final String key) throws Exception {
    Path target = root.resolve(key).normalize();
    if (!target.startsWith(root)) {
      throw new SecurityException("Invalid storage key");
    }

    return Files.exists(target);
  }
}