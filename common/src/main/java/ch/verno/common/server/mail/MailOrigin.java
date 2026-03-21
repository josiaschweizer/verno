package ch.verno.common.server.mail;

public enum MailOrigin {
  TENANT_CONFIG,
  ENV // ONLY USE FOR INTERNAL API CALLS (eg get in touch dialog from landing page) !!!!!
}
