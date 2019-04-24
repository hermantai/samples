package playdagger;

import javax.inject.Inject;
import playdagger.Annotations.UserName;

public class Thermosipon implements Pump {
  private final Heater heater;
  private final String username;

  @Inject
  Thermosipon(@UserName String username, Heater heater) {
    this.username = username;
    this.heater = heater;
  }

  @Override
  public void pump() {
    System.out.println("user: " + username);
    System.out.println("Thermosiphon.pump()");
  }
}
