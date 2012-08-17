package com.pfc.ball;

import com.pfc.conexion.ConexionArduino;
import com.pfc.datostrama.Aceleraciones;
import com.pfc.datostrama.DatosTrama;
import com.pfc.remote.ControladorMando;

import java.applet.Applet;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StartingPoint extends Applet implements Runnable, KeyListener {

	/**
     *
     */
	private static final long serialVersionUID = 5022320510835636310L;
	private Image image;
	private Graphics doubleG;
	private Ball b;
	private Platform p[] = new Platform[7];
	private Item items[] = new Item[10];
	private ConexionArduino conexionArduino;

	@Override
	public void init() {
		setSize(600, 400);
		addKeyListener(this);
	}

	@Override
	public void start() {
		conexionArduino = new ConexionArduino();
		conexionArduino.conectaServidor();
		System.out.println(conexionArduino.isConnected());
		b = new Ball();
		for (int i = 0; i < p.length; i++) {
			Random r = new Random();
			p[i] = new Platform(getWidth() + 200 * i, getHeight() - 40
					- r.nextInt(200));
		}

		for (int i = 0; i < items.length; i++) {
			items[i] = new Item(getWidth() + 2000 * i);
		}

		Thread thread = new Thread(this);
		thread.start();

		new Thread(new Runnable() {
			@Override
			public void run() {

				Double min = 0.1;

				while (true) {
					DatosTrama datosTrama = conexionArduino.leeDatos();
					System.out.println(datosTrama);
					if (datosTrama != null) {
						Aceleraciones aceleraciones = datosTrama
								.getAceleraciones();
						if (aceleraciones != null) {
							Double aceleracionX = aceleraciones.getX();
							if (aceleracionX > min) {

								b.moveRight();
							} else if (aceleracionX < -min) {
								b.moveLeft();
							}
						}
						

					}

				}
			}
		}).start();
	}

	@Override
	public void run() {

		// Actualiza el estado de todos los objeto y los pinta.
		while (true) {

			b.update(this);
			for (int i = 0; i < p.length; i++) {
				p[i].update(this, b);
			}
			for (int i = 0; i < items.length; i++) {
				items[i].update(this, b);
			}

			repaint();

			try {
				Thread.sleep(17); // 1000 milisegundos / 60FPS = 16.666 ms
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	/*
	 * Redefinimos el m?todo 'update' para que no borre el objeto en pantalla y
	 * evitar el parpadeo. (non-Javadoc)
	 * 
	 * @see java.awt.Container#update(java.awt.Graphics)
	 */
	@Override
	public void update(Graphics g) {
		// Double buffering.
		if (image == null) {
			image = createImage(this.getSize().width, this.getSize().height);
			doubleG = image.getGraphics();
		}

		doubleG.setColor(getBackground());
		doubleG.fillRect(0, 0, this.getSize().width, this.getSize().height);

		doubleG.setColor(getForeground());
		paint(doubleG);

		g.drawImage(image, 0, 0, this);
	}

	@Override
	public void paint(Graphics g) {
		b.paint(g);
		for (int i = 0; i < p.length; i++) {
			p[i].paint(g);
		}
		for (int i = 0; i < items.length; i++) {
			items[i].paint(g);
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_LEFT:
			b.moveLeft();
			break;
		case KeyEvent.VK_RIGHT:
			b.moveRight();
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	public String getHost() {
		return conexionArduino.getHost();
	}

	public void setHost(String host) {
		conexionArduino.setHost(host);
	}

	public int getPuerto() {
		return conexionArduino.getPuerto();
	}

	public void setPuerto(int puerto) {
		conexionArduino.setPuerto(puerto);
	}

	public boolean isConnected() {
		return conexionArduino.isConnected();
	}

	public ConexionArduino getConexionArduino() {
		return conexionArduino;
	}

}
