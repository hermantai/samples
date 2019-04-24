package playdagger;

import dagger.BindsInstance;
import dagger.Component;
import javax.inject.Singleton;
import playdagger.Annotations.UserName;

@Singleton
@Component(modules = DripCoffeeModule.class)
public interface CoffeeShop {
  CoffeeMaker maker();

  @Component.Builder
  interface Builder {
    @BindsInstance
    Builder userName(@UserName String userName);
    CoffeeShop build();
  }
}
