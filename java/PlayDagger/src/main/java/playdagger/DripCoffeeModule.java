package playdagger;

import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module(includes = PumpModule.class)
public class DripCoffeeModule {
  @Provides
  @Singleton
  Heater provideHeater() {
    return new ElectricHeater();
  }
}
