package ch.verno.db.entity.tenant;

import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

@FilterDef(
        name = TenantFilters.TENANT_FILTER,
        parameters = @ParamDef(name = TenantFilters.PARAM_TENANT_ID, type = long.class)
)
public final class TenantFilters {

  public static final String TENANT_FILTER = "mandantFilter";
  public static final String PARAM_TENANT_ID = "mandantId";

  private TenantFilters() {
  }
}