package ch.verno.db.entity.mandant;

import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

@FilterDef(
        name = MandantFilters.MANDANT_FILTER,
        parameters = @ParamDef(name = MandantFilters.PARAM_MANDANT_ID, type = long.class)
)
public final class MandantFilters {

  public static final String MANDANT_FILTER = "mandantFilter";
  public static final String PARAM_MANDANT_ID = "mandantId";

  private MandantFilters() {
  }
}