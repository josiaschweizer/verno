package ch.verno.provisioner.db.repository;

import ch.verno.provisioner.db.entity.TenantRecord;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class TenantRepository {

  private final JdbcTemplate jdbc;

  public TenantRepository(final JdbcTemplate jdbc) {
    this.jdbc = jdbc;
  }

  public UUID insertTenant(final String tenantKey,
                           final String tenantName,
                           final String subdomain,
                           final String preferredLanguage) {
    final var keyHolder = new GeneratedKeyHolder();

    jdbc.update(con -> {
      final PreparedStatement ps = con.prepareStatement("""
              insert into tenants (tenant_key, tenant_name, tenant_subdomain, preferred_language)
              values (?, ?, ?, ?)
              returning id
              """, Statement.RETURN_GENERATED_KEYS);
      ps.setString(1, tenantKey);
      ps.setString(2, tenantName);
      ps.setString(3, subdomain);
      ps.setString(4, preferredLanguage);
      return ps;
    }, keyHolder);

    final var keys = keyHolder.getKeys();
    if (keys != null && keys.get("id") != null) {
      return (UUID) keys.get("id");
    }

    final var id = jdbc.queryForObject("select id from tenants where tenant_key = ?", UUID.class, tenantKey);
    if (id == null) throw new IllegalStateException("Could not resolve tenant id after insert");
    return id;
  }

  public List<TenantRecord> findAll() {
    return jdbc.query("""
            select
              id,
              tenant_key,
              tenant_name,
              tenant_subdomain,
              preferred_language,
              status,
              schema_name,
              provisioned_at,
              db_status,
              created_at,
              updated_at
            from tenants
            order by created_at desc
            """, (rs, rowNum) -> new TenantRecord(
            rs.getObject("id", UUID.class),
            rs.getString("tenant_key"),
            rs.getString("tenant_name"),
            rs.getString("tenant_subdomain"),
            rs.getString("preferred_language"),
            rs.getString("status"),
            rs.getString("schema_name"),
            rs.getObject("provisioned_at", OffsetDateTime.class),
            rs.getString("db_status"),
            rs.getObject("created_at", OffsetDateTime.class),
            rs.getObject("updated_at", OffsetDateTime.class)
    ));
  }

  public Optional<TenantRecord> findByTenantKey(final String tenantKey) {
    final var list = jdbc.query("""
            select
              id,
              tenant_key,
              tenant_name,
              tenant_subdomain,
              preferred_language,
              status,
              schema_name,
              provisioned_at,
              db_status,
              created_at,
              updated_at
            from tenants
            where tenant_key = ?
            """, (rs, rowNum) -> new TenantRecord(
            rs.getObject("id", UUID.class),
            rs.getString("tenant_key"),
            rs.getString("tenant_name"),
            rs.getString("tenant_subdomain"),
            rs.getString("preferred_language"),
            rs.getString("status"),
            rs.getString("schema_name"),
            rs.getObject("provisioned_at", OffsetDateTime.class),
            rs.getString("db_status"),
            rs.getObject("created_at", OffsetDateTime.class),
            rs.getObject("updated_at", OffsetDateTime.class)
    ), tenantKey);

    if (list.isEmpty()) return Optional.empty();
    return Optional.of(list.getFirst());
  }

  public void callProvisionFunction(final String tenantKey,
                                    final String adminEmail,
                                    final String adminDisplayName,
                                    final String adminPasswordHash) {
    // The DB function returns a value; consume it via queryForObject instead of update()
    jdbc.queryForObject(
            "select provision_tenant(?, ?, ?, ?)",
            Object.class,
            tenantKey,
            adminEmail,
            adminDisplayName,
            adminPasswordHash
    );
  }
}