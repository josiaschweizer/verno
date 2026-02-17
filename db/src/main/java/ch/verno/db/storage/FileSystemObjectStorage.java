package ch.verno.db.storage;

import jakarta.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.Optional;

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
  public Optional<InputStream> get(@Nonnull final String key) throws IOException {
    final Path target = root.resolve(key).normalize();

    if (!target.startsWith(root)) {
      throw new SecurityException("Invalid storage key");
    }

    try {
      if (!Files.exists(target, LinkOption.NOFOLLOW_LINKS) ||
              !Files.isRegularFile(target, LinkOption.NOFOLLOW_LINKS)) {
        return Optional.empty();
      }

      return Optional.of(Files.newInputStream(target, StandardOpenOption.READ));
    } catch (NoSuchFileException e) {
      return Optional.empty();
    }
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