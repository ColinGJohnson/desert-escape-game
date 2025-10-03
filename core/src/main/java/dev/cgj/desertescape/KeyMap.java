package dev.cgj.desertescape;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import java.util.List;

public class KeyMap {
  public enum KeyBinding {
    STEER_LEFT(List.of(Input.Keys.LEFT, Input.Keys.A)),
    STEER_RIGHT(List.of(Input.Keys.RIGHT, Input.Keys.D)),
    ACCELERATE(List.of(Input.Keys.UP, Input.Keys.W)),
    REVERSE(List.of(Input.Keys.DOWN, Input.Keys.S)),
    BRAKE_HAND(List.of(Input.Keys.SPACE)),
    PAUSE(List.of(Input.Keys.ESCAPE)),
    MENU_UP(List.of(Input.Keys.UP, Input.Keys.W)),
    MENU_DOWN(List.of(Input.Keys.DOWN, Input.Keys.S)),
    MENU_SELECT(List.of(Input.Keys.ENTER, Input.Keys.SPACE)),
    TOGGLE_DEBUG(List.of(Input.Keys.LEFT_BRACKET));

    private final List<Integer> keys;

    KeyBinding(List<Integer> defaultKeys) {
      this.keys = defaultKeys;
    }

    public List<Integer> getKeys() {
      return keys;
    }
  }

  public static boolean isPressed(KeyBinding binding) {
    return binding.getKeys().stream()
      .anyMatch(key -> Gdx.input.isKeyPressed(key));
  }

  public static boolean isJustPressed(KeyBinding binding) {
    return binding.getKeys().stream()
      .anyMatch(key -> Gdx.input.isKeyJustPressed(key));
  }
}
