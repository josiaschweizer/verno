package ch.verno.db.storage;

import jakarta.annotation.Nonnull;
import org.jetbrains.annotations.NonNls;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.Optional;

@Component
public class FileSystemObjectStorage implements ObjectStorage {

  @NonNls public static final String STORAGE_ROOT = "${files.storage.root:./data/files}";

  @Nonnull private final Path root;

  public FileSystemObjectStorage(@Value(STORAGE_ROOT) @Nonnull final String root) {
    this.root = Paths.get(root)
            .toAbsolutePath()
            .normalize();
  }

  @Nonnull
  @Override
  public StoredObject put(@Nonnull final String key,
                          @Nonnull final InputStream data,
                          final long size) throws IOException {
    final var target = resolveTarget(key);
    Files.createDirectories(root);
    Files.createDirectories(target.getParent());
    if (Files.exists(target, LinkOption.NOFOLLOW_LINKS)
            && Files.isSymbolicLink(target)) {
      throw new SecurityException("Storage target must not be a symbolic link");
    }

    Files.copy(data, target, StandardCopyOption.REPLACE_EXISTING);
    return new StoredObject(
            key,
            Files.size(target)
    );
  }

  @Nonnull
  @Override
  public Optional<InputStream> get(@Nonnull final String key) throws IOException {
    final var target = resolveTarget(key);
    try {
      if (!Files.exists(target, LinkOption.NOFOLLOW_LINKS)
              || !Files.isRegularFile(target, LinkOption.NOFOLLOW_LINKS)
              || Files.isSymbolicLink(target)) {
        return Optional.empty();
      }

      return Optional.of(Files.newInputStream(target, StandardOpenOption.READ));
    } catch (NoSuchFileException exception) {
      return Optional.empty();
    }
  }

  @Override
  public void delete(@Nonnull final String key) throws IOException {
    final var target = resolveTarget(key);
    if (Files.isSymbolicLink(target)) {
      throw new SecurityException("Storage target must not be a symbolic link");
    }

    Files.deleteIfExists(target);
  }

  @Override
  public boolean exists(@Nonnull final String key) {
    final var target = resolveTarget(key);
    return Files.exists(target, LinkOption.NOFOLLOW_LINKS)
            && Files.isRegularFile(target, LinkOption.NOFOLLOW_LINKS)
            && !Files.isSymbolicLink(target);
  }

  @Nonnull
  private Path resolveTarget(@Nonnull final String key) {
    final var target = root.resolve(key).normalize();
    if (!target.startsWith(root)) {
      throw new SecurityException("Invalid storage key");
    }

    return target;
  }
}