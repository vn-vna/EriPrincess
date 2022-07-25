package vn.vna.eri.v2.schema;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import vn.vna.eri.v2.utils.UTJsonClass;

@Getter
@Setter
public class ARTemplate<R>
    implements UTJsonClass {

  protected Boolean success;
  protected String  error;
  protected Long    took;
  protected List<R> results;

  public ARTemplate() {
    this.success = false;
    this.error   = null;
    this.took    = null;
    this.results = new ArrayList<>();
  }

}
