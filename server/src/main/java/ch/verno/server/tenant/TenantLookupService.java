package ch.verno.server.tenant;

import ch.verno.common.db.dto.table.TenantDto;
import ch.verno.server.mapper.TenantMapper;
import ch.verno.server.repository.TenantRepository;
import jakarta.annotation.Nonnull;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class TenantLookupService {

  @Nonnull
  private final TenantRepository tenantRepository;

  private final Map<String, Long> cacheBySlug = new ConcurrentHashMap<>();

  public TenantLookupService(@Nonnull final TenantRepository tenantRepository) {
    this.tenantRepository = tenantRepository;
  }

  @Nonnull
  public Optional<Long> findTenantIdBySlug(@Nonnull final String slug) {
    final var key = slug.trim().toLowerCase();
    final var cached = cacheBySlug.get(key);
    if (cached != null) {
      return Optional.of(cached);
    }

    final var idOpt = tenantRepository.findIdBySlug(key);
    idOpt.ifPresent(id -> cacheBySlug.put(key, id));
    return idOpt;
  }

  @Nonnull
  public List<TenantDto> findAllTenants() {
    return tenantRepository.findAll().stream()
            .map(TenantMapper::toDto)
            .toList();
  }

  public void evictSlug(@Nonnull final String slug) {
    cacheBySlug.remove(slug.trim().toLowerCase());
  }

  public void clearCache() {
    cacheBySlug.clear();
  }
}