package ch.verno.server.mandant;

import ch.verno.common.mandant.MandantContext;

public class CurrentMandant {

  public long id() {
    return MandantContext.getRequired();
  }
}