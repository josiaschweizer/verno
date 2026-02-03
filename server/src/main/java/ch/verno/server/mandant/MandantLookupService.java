package ch.verno.server.mandant;

import ch.verno.server.repository.MandantRepository;
import jakarta.annotation.Nonnull;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class MandantLookupService {

  @Nonnull
  private final MandantRepository mandantRepository;

  private final Map<String, Long> cacheBySlug = new ConcurrentHashMap<>();

  public MandantLookupService(@Nonnull final MandantRepository mandantRepository) {
    this.mandantRepository = mandantRepository;
  }

  @Nonnull
  public Optional<Long> findMandantIdBySlug(@Nonnull final String slug) {
    final var key = slug.trim().toLowerCase();
    final var cached = cacheBySlug.get(key);
    if (cached != null) {
      return Optional.of(cached);
    }

    final var idOpt = mandantRepository.findIdBySlug(key);
    idOpt.ifPresent(id -> cacheBySlug.put(key, id));
    return idOpt;
  }

  public void evictSlug(@Nonnull final String slug) {
    cacheBySlug.remove(slug.trim().toLowerCase());
  }

  public void clearCache() {
    cacheBySlug.clear();
  }
}