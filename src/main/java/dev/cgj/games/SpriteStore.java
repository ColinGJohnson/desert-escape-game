package dev.cgj.games;

import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import javax.imageio.ImageIO;

public class SpriteStore {
	private static final SpriteStore INSTANCE = new SpriteStore();
	private final HashMap<String, Sprite> sprites = new HashMap<>();

	public static SpriteStore get() {
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
		try (InputStream imageStream = Main.class.getResourceAsStream(path)) {
			if (imageStream == null) {
				throw new IllegalArgumentException("Image resource not found: " + path);
			}
			return ImageIO.read(imageStream);
		} catch (IOException e) {
			throw new RuntimeException("Failed to load image resource: " + path, e);
		}
	}
}
