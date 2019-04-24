package playdagger;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import javax.inject.Qualifier;

public class Annotations {
  @Qualifier
  @Documented
  @Retention(RUNTIME)
  public @interface UserName {}
}
