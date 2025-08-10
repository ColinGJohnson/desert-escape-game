package dev.cgj.games.old;

import dev.cgj.games.escape.DesertEscape;

import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import javax.imageio.ImageIO;

public class SpriteStore {
	private static final SpriteStore INSTANCE = new SpriteStore();
	private final HashMap<String, Sprite> sprites = new HashMap<>();

	public SpriteStore() {

		// Preload images to avoid hanging when the associated entities first load.
		getSprite("/sprites/sc1.png");
		getSprite("/sprites/skull.png");
		getSprite("/sprites/cactus.png");
		getSprite("/sprites/cone.png");
		getSprite("/sprites/shot.jpg");
		getSprite("/sprites/health.png");
		getSprite("/sprites/rocket.png");
		getSprite("/sprites/nitro.png");
		getSprite("/sprites/body2.png");
		getSprite("/sprites/body3.png");
	}

	public static SpriteStore getInstance() {
		return INSTANCE;
	}

	public Sprite getSprite(String path) {
		if (sprites.get(path) != null) {
			return sprites.get(path);
		}

		Sprite sprite = new Sprite(getImageResource(path));
		sprites.put(path, sprite);
		return sprite;
	}

	public Image getImageResource(String path) {
		try (InputStream imageStream = DesertEscape.class.getResourceAsStream(path)) {
			if (imageStream == null) {
				throw new IllegalArgumentException("Image resource not found: " + path);
			}
			return ImageIO.read(imageStream);
		} catch (IOException e) {
			throw new RuntimeException("Failed to load image resource: " + path, e);
		}
	}
}
