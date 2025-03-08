package dev.cgj.games;

import java.awt.Color;
import java.awt.Graphics2D;

public class Particle {

	private int x;
	private int y;
	private double dx;
	private double dy;
	private int size;
	private double life = 0;
	private double maxLife;
	private Color color;
	private double speedReduction;

	public Particle(int x, int y, double dx, double dy, int size, int maxLife, Color c, double speedReduction) {
		this.x = x;
		this.y = y;
		this.dx = dx;
		this.dy = dy;
		this.size = size;
		this.maxLife = maxLife;
		this.color = c;
		this.speedReduction = speedReduction;
	}

	public boolean update() {
		x += dx / 10 * Math.random();
		y += dy * Math.random();
		
		dy -= speedReduction;
		dx -= speedReduction;
		
		life++;
		if (life >= maxLife)
			return true;
		return false;
	}

	public void draw(Graphics2D g) {
		
		Graphics2D g2d = (Graphics2D) g.create();

		g2d.setColor(color);
		g2d.fillRect(x - (size / 2), y - (size / 2), (int) (size * (life/maxLife)), (int)(size * (life/maxLife))); 

		g2d.dispose();
	}
} // Particle