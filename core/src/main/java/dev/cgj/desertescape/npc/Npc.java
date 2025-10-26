package dev.cgj.desertescape.npc;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import dev.cgj.desertescape.Player;

public interface Npc extends Disposable {

  void update(float delta, Player player);

  void draw(SpriteBatch batch);
}
