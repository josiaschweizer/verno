package ch.verno.server.mandant;

public class CurrentMandant {

  public long id() {
    return MandantContext.getRequired();
  }
}