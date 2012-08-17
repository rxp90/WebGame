package com.pfc.ball;
import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

public class Platform {
	/**
	 * Velocidad de desplazamiento.
	 */
	private int dx;
	/**
	 * Coordenadas de posición, ancho y alto.
	 */
	private int x, y, width, height;

	/**
	 * Constructor por defecto.
	 */
	public Platform() {
		dx = -1;
		x = 300;
		y = 300;
		width = 120;
		height = 40;
	}

	/**
	 * Constructor con dos coordenadas.
	 * 
	 * @param x
	 *            coordenada x.
	 * @param y
	 *            coordenada y.
	 */
	public Platform(int x, int y) {
		this.x = x;
		this.y = y;
		width = 120;
		height = 40;
		dx = -1;
	}

	/**
	 * Actualiza el estado del objeto.
	 * 
	 * @param StartingPoint
	 *            applet.
	 * @param b
	 *            objeto Ball del usuario.
	 */
	public void update(StartingPoint StartingPoint, Ball b) {

		x += dx; // Las plataformas se moverán a la izquierda.

		checkForCollision(b);

		// Si la plataforma desaparece por la izquierda la colocaremos de nuevo
		// por la derecha de forma aleatoria.
		if (x < 0 - width) {
			Random r = new Random();
			y = StartingPoint.getHeight() - height - r.nextInt(400);
			x = StartingPoint.getWidth() + r.nextInt(300);
		}
	}

	/**
	 * Comprueba si la bola ha colisionado con la plataforma.
	 * 
	 * @param b
	 *            objeto Ball del usuario.
	 */
	private void checkForCollision(Ball b) {
		int ballX = b.getX();
		int ballY = b.getY();
		int radius = b.getRadius();

		if (ballY + radius > y && ballY + radius < y + height) {
			if (ballX > x && ballX < x + width) {
				// double newDY = b.getDy() * -1;// Cambio la dirección del
				// movimiento.
				double newDY = b.getGameDy(); // Uso la velocidad del juego.
				b.setY(y - radius); // Coloco la bola sobre la plataforma. De
									// no hacer esto, acabaría
									// atravesándola.
				b.setDy(newDY);
			}
		}
	}

	public void paint(Graphics g) {
		g.setColor(Color.BLUE);
		g.fillRect(x, y, width, height);
	}
}
