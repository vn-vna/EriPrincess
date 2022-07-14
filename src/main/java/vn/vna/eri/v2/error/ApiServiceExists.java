package vn.vna.eri.v2.error;

public class ApiServiceExists extends IllegalStateException {

  public ApiServiceExists() {
    super("Api Service has already been installed");
  }
}
