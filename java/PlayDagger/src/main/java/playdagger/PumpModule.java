package playdagger;

import dagger.Binds;
import dagger.Module;

@Module
abstract class PumpModule {
  @Binds
  abstract Pump providePump(Thermosipon pump);
}