package com.charles.zeng;

import java.awt.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.geom.*;
import java.io.File;
import java.io.IOException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * Shows an animated bouncing ball.
 */
public class Bounce {
	public static void main(String[] args) {
		frame = new BounceFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	public static JFrame getFrame() {
		return frame;
	}

	private static JFrame frame;
}

/**
 * A runnable that animates a bouncing ball.
 */
class BallRunnable implements Runnable {
	/**
	 * Constructs the runnable.
	 * 
	 * @aBall the ball to bounce
	 * @aPanel the component in which the ball bounces
	 */
	// public BallRunnable(Ball aBall, Component aComponent)
	public BallRunnable(Component aComponent, MousePanel panel3) {
		component = aComponent;
		this.panel3 = panel3;
	}

	public synchronized void run() {
		Thread thisThread = Thread.currentThread();

		try {

			// Ball c = new Ball();
			// ballArray.add(c);
			while (judge.get_next_user() != 0) {
				try {
					wait();
				} catch (InterruptedException e) {

				}
			}

			Pocket.reset_Poc_num(); // prevent poc_num from
			// setting
			// white
			// forever
			for (Ball b : ballArray.get()) {
				b.init();
			}
			for (int i = 1; i <= data.STEPS; i++) {
				for (Ball b : ballArray.get()) {
					b.move(i);
				}
				// check if two ball collides
				collision.checkCollision();
				component.repaint();
				Thread.sleep(data.DELAY);
				if (ballArray.totalspeed() == 0)
					break; // fast quit
			}
			judge jud = new judge();
			jud.judgeball(0);
			wait(400); // time for white ball returns
			component.repaint();
			// panel3.add(); // set initial mouse line for every thread finish
			// time
			wait(200); // to prevent jud.who_is_next() happens before .repaint()
			if (((BounceFrame) Bounce.getFrame()).get_t1() == thisThread)
				jud.who_is_next(); // may add new thread here

		} catch (InterruptedException e) {
		}
		notifyAll();

	}

	// private Ball ball;
	private Component component;
	private MousePanel panel3;
	// private int working_lock;

}

class BallAuto implements Runnable {
	/**
	 * Constructs the runnable.
	 * 
	 * @aBall the ball to bounce
	 * @aPanel the component in which the ball bounces
	 */
	// public BallRunnable(Ball aBall, Component aComponent)
	public BallAuto(Component aComponent, MousePanel panel3) {
		component = aComponent;
		this.panel3 = panel3;
	}

	public synchronized void run() {
		Thread thisThread = Thread.currentThread();

		try {
			// Thread.sleep(2000);
			// wait();
			while (judge.get_next_user() != 1) {
				try {
					wait();
				} catch (InterruptedException e) {
				}
			}
			// System.out.println("b4_calc");
			auto.calculate();
			// System.out.println("calc");
			Pocket.reset_Poc_num();
			for (Ball b : ballArray.get()) {
				b.init_auto();
			}
			for (int i = 1; i <= data.STEPS; i++) {
				for (Ball b : ballArray.get()) {
					b.move(i);
				}
				// check if two ball collides
				collision.checkCollision();
				component.repaint();
				Thread.sleep(data.DELAY);
				if (ballArray.totalspeed() == 0)
					break; // fast quit
			}
			Thread.sleep(100); // sleep before judge
			judge jud = new judge();
			jud.judgeball(1);
			component.repaint();
			wait(200);
			if (((BounceFrame) Bounce.getFrame()).get_t2() == thisThread)
				jud.who_is_next();
			// System.out.println("computer done");
		} catch (Exception e) {
		}
		notifyAll();

	}

	// private Ball ball;
	private Component component;
	private MousePanel panel3;

}

/**
 * The panel that draws the balls.
 */
class BallPanel extends JPanel {
	/**
	 * Add a ball to the panel.
	 * 
	 * @param b
	 *            the ball to add
	 */
	private Image image1, image2, image3, image4;

	// private GradientPaint gradient =
	// new GradientPaint(0, 0, Color.red, b.getX(), 20, Color.yellow,
	// false);

	public BallPanel() {
		// try {
		// image1 = ImageIO.read(new File(
		// "src/com/charles/zeng/snookertable.PNG"));
		// image2 = ImageIO
		// .read(new File("D:/Documents/haha.PNG"));
		// image3 = ImageIO.read(new File("src/com/charles/zeng/RedBall.PNG"));
		// image4 = ImageIO
		// .read(new File("src/com/charles/zeng/whiteball.PNG"));
		// image4 = ImageIO.read(new File(
		// "C:/Users/Charles/Desktop/blue-ball.jpg"));
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		for (Ball b : ballArray.get()) {

			if (b.getBallID() == 15) {
				g2.setPaint(new GradientPaint((float) b.getX(), (float) b
						.getY(), Color.white, (float) b.getX() + 20, (float) b
						.getY() + 20, Color.gray, false));
				g2.fill(b.getShape());
				// g2.drawImage(image4,(int)b.getX(),(int)b.getY(),20,20,null);
			} else if (b.getBallID() % 2 == 0) {
				g2.setPaint(new GradientPaint((float) b.getX(), (float) b
						.getY(), Color.yellow, (float) b.getX() + 20, (float) b
						.getY() + 20, Color.red, false));
				g2.fill(b.getShape());
				// g2.draw(b.getShape());
			} else if (b.getBallID() % 2 == 1) {
				g2.setPaint(new GradientPaint((float) b.getX(), (float) b
						.getY(), Color.cyan, (float) b.getX() + 20, (float) b
						.getY() + 20, Color.blue, false));
				g2.fill(b.getShape());
				// g2.draw(b.getShape());
			}
			g2.setPaint(Color.white);
			if (b.getBallID() != 15)
				g2.drawString(b.message(), (float) b.getX() + 4, (float) b
						.getY() + 14);

		}
		// // table edge
		// g2.setPaint(Color.black);
		// double sq = 14.142;
		// // g2.draw( new Rectangle2D.Double(50,50,700,350));
		//		
		// //g2.drawImage(image1, 3, 7, 795, 440, null);
		// g2.draw(new Line2D.Double(50 + 2 * sq, 50, 380, 50)); // clockwise,
		// // start from
		// // top-left
		// g2.draw(new Line2D.Double(420, 50, 750 - 2 * sq, 50));
		// g2.draw(new Line2D.Double(50, 50 + 2 * sq, 50, 400 - 2 * sq));
		// g2.draw(new Line2D.Double(50 + 2 * sq, 400, 380, 400));
		// g2.draw(new Line2D.Double(420, 400, 750 - 2 * sq, 400));
		// g2.draw(new Line2D.Double(750, 50 + 2 * sq, 750, 400 - 2 * sq));
		// // delineate line
		// g2.draw(new Line2D.Double(190, 50, 190, 400));
		//		
		// // middle pocket
		// g2.draw(new Line2D.Double(380, 50 - 10, 380, 50));
		// g2.draw(new Line2D.Double(420, 50 - 10, 420, 50));
		//		
		// g2.draw(new Line2D.Double(380, 400, 380, 400 + 10));
		// g2.draw(new Line2D.Double(420, 400, 420, 400 + 10));
		//		
		// // corner pocket
		// g2.draw(new Line2D.Double(50 + sq, 50 - sq, 50 + 2 * sq, 50)); //
		// // top-left
		// g2.draw(new Line2D.Double(50 - sq, 50 + sq, 50, 50 + 2 * sq));
		//		
		// g2.draw(new Line2D.Double(50 - sq, 400 - sq, 50, 400 - 2 * sq)); //
		// // bottom-left
		// g2.draw(new Line2D.Double(50 + sq, 400 + sq, 50 + 2 * sq, 400));
		//		
		// g2.draw(new Line2D.Double(750 - 2 * sq, 50, 750 - sq, 50 - sq)); //
		// // top-right
		// g2.draw(new Line2D.Double(750, 50 + 2 * sq, 750 + sq, 50 + sq));
		//		
		// g2.draw(new Line2D.Double(750, 400 - 2 * sq, 750 + sq, 400 - sq)); //
		// // bottom-right
		// g2.draw(new Line2D.Double(750 - 2 * sq, 400, 750 - sq, 400 + sq));
	}
}

class DrawComponent extends JPanel {
	private Image image1;

	public DrawComponent() {
		// try {
		// image1 = ImageIO.read(new File("src/com/charles/zeng/haha.PNG"));
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		try {
			ImageIcon i = new ImageIcon(getClass().getResource("/haha.PNG"));

			image1 = i.getImage();
		} catch (Exception e) {
			ImageIcon i = new ImageIcon(getClass().getResource(
					"/com/charles/zeng/haha.PNG"));

			image1 = i.getImage();
		}
	}

	public void paintComponent(Graphics gg) {
		super.paintComponent(gg);
		Graphics2D g2 = (Graphics2D) gg;
		g2.drawImage(image1, 8, 8, 785, 435, null);
		g2.setFont(new Font("Lucida Bright", Font.BOLD, 14)); // this font is
		// fast
		g2.drawString("User", 830, 30);
		g2.drawString("Comp", 825, 250);

	}
}
