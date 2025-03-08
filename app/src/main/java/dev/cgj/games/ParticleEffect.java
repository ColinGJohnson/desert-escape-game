package dev.cgj.games;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

public class ParticleEffect {
    int x;
    int y;
    int dx;
    int dy;
	Color effectColor;
    int life;
    int numParticles;
    String type;

    private final ArrayList<Particle> effectParticles = new ArrayList<>();

    public ParticleEffect(int x, int y, int dx, int dy, Color effectColor, int life, int numParticles, String type, int size) {
        this.x = x;
        this.y = y;
        this.effectColor = effectColor;
        this.numParticles = numParticles;
        this.type = type;
        this.dy = dy;
        this.dx = dx;
        this.life = life;

        // create new particles
        for (int i = 0; i < numParticles; i++) {
            effectParticles.add(new Particle(x, y, dx, dy, size, life, effectColor, 0));
        }
    }

    public void draw(Graphics2D g) {
        for (Particle effectParticle : effectParticles) {
            effectParticle.draw(g);
        }
    }
}
