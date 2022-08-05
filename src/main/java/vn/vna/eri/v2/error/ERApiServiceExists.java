package vn.vna.eri.v2.error;

public class ERApiServiceExists
  extends IllegalStateException {

  public ERApiServiceExists() {
    super("Api Service has already been installed");
  }
}
