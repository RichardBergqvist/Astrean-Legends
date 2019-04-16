package com.astreanlegends.graphics;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;

public class Display extends Canvas implements Runnable {
	
	private static final long serialVersionUID = 1L;

	public static final int WIDTH = 800;
	public static final int HEIGHT = 600;
	public static final String TITLE = "Astrean Legends In-Development 1.0";
	
	private Thread thread;
	private boolean running = false;
	
	private Screen screen;
	private BufferedImage image;
	private int[] pixels;
	
	public Display() {
		screen = new Screen(WIDTH, HEIGHT);
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
	}
	
	private void start() {
		if (running)
			return;
		running = true;
		thread = new Thread(this);
		thread.start();
	}
	
	private void stop() {
		if (!running)
			return;
		running = false;
		try {
			thread.join();
		} catch (Exception exception) {
			exception.printStackTrace();
			System.exit(0);
		}
	}
	
	public void run() {
		while (running) {
			update();
			render();
		}
	}
	
	private void update() {
		
	}
	
	private void render() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}
		screen.render();
		for (int i = 0; i < WIDTH * HEIGHT; i++) {
			pixels[i] = screen.pixels[i];
		}
		Graphics graphics = bs.getDrawGraphics();
		graphics.drawImage(image, 0, 0, WIDTH, HEIGHT, null);
		graphics.dispose();
		bs.show();
	}
	
	public static void main(String[] arguments) {
		Display game = new Display();
		JFrame frame = new JFrame();
		frame.add(game);
		frame.pack();
		frame.setTitle(Display.TITLE);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(new Dimension(WIDTH, HEIGHT));
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setVisible(true);
		
		System.out.println("Running...");
		
		game.start();
	}
}