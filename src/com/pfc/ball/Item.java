package com.pfc.ball;
import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

public class Item {
	/**
	 * Radio de la bola y coordenadas de posición.
	 */
	private int x, y, radius;
	/**
	 * Applet.
	 */
	private StartingPoint jp;
	/**
	 * Velocidades.
	 */
	private double dx = 10;
	private double dy = 0;
	private double maxSpeed = 20;
	/**
	 * Variables para añadir realismo.
	 */
	private double gravity = 15;
	private double energyLoss = .65;
	private double dt = .2;
	private double xFriction = .9;
	/**
	 * Variable para saber si la bola debe caer o no.
	 */
	private boolean fallToFloor = false;

	/**
	 * Crea un objeto especificando su coordenada x.
	 * 
	 * @param x
	 *            coordenada x.
	 */
	public Item(int x) {
		this.x = x;
		Random r = new Random();
		y = r.nextInt(200) + radius;
		radius = 10;
		dx = -2;
	}

	/**
	 * Actualiza el estado del objeto.
	 * 
	 * @param StartingPoint
	 *            applet.
	 * @param b
	 *            objeto Ball del usuario.
	 */
	public void update(StartingPoint startingPoint, Ball b) {

		if (this.jp == null) {
			this.jp = startingPoint;
		}
		if (!fallToFloor) {
			x += dx; // Las plataformas se moverán a la derecha.
			checkForCollision(b);
		} else {
			fallToFloor();
		}

		// Si el objeto desaparece por la izquierda lo colocaremos de nuevo
		// por la derecha de forma aleatoria.
		if (x < 0 - radius) {
			Random r = new Random();
			x = startingPoint.getWidth() + 2000 + r.nextInt(300);
		}
	}

	/**
	 * Comprueba si la bola ha colisionado o no con el objeto.
	 * 
	 * @param ball
	 *            objeto Ball del usuario.
	 */
	private void checkForCollision(Ball ball) {
		// Colisión mejorada con teorema de Pitágoras.
		int ballX = ball.getX();
		int ballY = ball.getY();
		int ballRadius = ball.getRadius();

		int a = x - ballX;
		int b = y - ballY;

		// Distancia a la que colisionarían.
		int collide = radius + ballRadius;

		// Distancia entre centros de los objetos.
		double c = Math.sqrt((double) (a * a) + (double) (b * b));

		// Si colisionan llevamos a cabo una acción.
		if (c < collide) {
			performAction();
		}
	}

	/**
	 * Acción que se llevará a cabo si hay colisión.
	 * 
	 */
	public void performAction() {
		fallToFloor = true;
	}

	/**
	 * Hace que el objeto caiga al suelo.
	 */
	public void fallToFloor() {
		if (x + dx > jp.getWidth() - radius - 1) {
			x = jp.getWidth() - radius - 1; // -1 porque empieza en 0
			dx = -dx;
		} else if (x + dx < 0 + radius) {
			x = 0 + radius;
			dx = -dx;
		} else {
			x += dx;
		}

		// Si está en el suelo la velocidad del eje x sufrirá una fricción.
		if (y == jp.getHeight() - radius - 1) {
			dx *= xFriction;
			// Para detener el movimiento "residual" por el suelo.
			if (Math.abs(dx) < .8) {
				dx = 0;
				dy = 0;
			}
		}

		if (y > jp.getHeight() - radius - 1) { // Si la 'y' sobrepasa el
												// suelo,
												// la pelota rebotará contra
												// él.
			y = jp.getHeight() - radius - 1;
			dy *= energyLoss;
			dy = -dy;

		} else {
			// Velocidad (ley dinámica)
			dy += gravity * dt;
			// Posición (ley dinámica)
			y += dy * dt + .5 * gravity * dt * dt;
		}

	}

	/**
	 * Pinta el objeto.
	 * 
	 * @param g
	 *            Graphics sobre el que se dibujará.
	 */
	public void paint(Graphics g) {
		g.setColor(Color.ORANGE);
		g.fillOval(x - radius, y - radius, radius * 2, radius * 2);
	}

	// Getters and setters

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getRadius() {
		return radius;
	}

	public void setRadius(int radius) {
		this.radius = radius;
	}

	public double getDx() {
		return dx;
	}

	public void setDx(double dx) {
		this.dx = dx;
	}

	public double getDy() {
		return dy;
	}

	public void setDy(double dy) {
		this.dy = dy;
	}

	public double getMaxSpeed() {
		return maxSpeed;
	}

	public void setMaxSpeed(double maxSpeed) {
		this.maxSpeed = maxSpeed;
	}

	public double getGravity() {
		return gravity;
	}

	public void setGravity(double gravity) {
		this.gravity = gravity;
	}

	public double getEnergyLoss() {
		return energyLoss;
	}

	public void setEnergyLoss(double energyLoss) {
		this.energyLoss = energyLoss;
	}

	public double getDt() {
		return dt;
	}

	public void setDt(double dt) {
		this.dt = dt;
	}

	public double getxFriction() {
		return xFriction;
	}

	public void setxFriction(double xFriction) {
		this.xFriction = xFriction;
	}

}
