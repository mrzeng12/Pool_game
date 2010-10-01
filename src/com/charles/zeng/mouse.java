package com.charles.zeng;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.awt.geom.*;

import javax.swing.*;

class MousePanel extends JPanel {
	public MousePanel(BounceFrame frame) {

		line = new Line2D.Double(0, 0, 0, 0);

		current = new Ellipse2D.Double(300 - SIDELENGTH / 2,
				200 - SIDELENGTH / 2, SIDELENGTH, SIDELENGTH);

		this.frame = frame; // to get frame.addBall method
		addMouseListener(new MouseHandler());
		addMouseMotionListener(new MouseMotionHandler());
		addMouseWheelListener(new MouseWheelEventDemo());
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		g2.draw(current);
		g2.draw(line);

	}

	/**
	 * Adds a square to the collection.
	 * 
	 * @param p
	 *            the center of the square
	 */
	public void add(Point2D p) {
		double x = p.getX();
		double y = p.getY();
		current = new Ellipse2D.Double(x - SIDELENGTH / 2, y - SIDELENGTH / 2,
				SIDELENGTH, SIDELENGTH);
		if (ballArray.get().size() > 0) {
			whit_x = ballArray.get().get(15).getX();
			whit_y = ballArray.get().get(15).getY();
			line = new Line2D.Double(whit_x - 50 + 10, whit_y - 50 + 10, x, y);
		} else
			line = new Line2D.Double(0, 0, 0, 0);
		;
		repaint();
	}

	public void add() {
		add(new Point2D.Double(MouseInfo.getPointerInfo().getLocation().x - 58,
				MouseInfo.getPointerInfo().getLocation().y - 77));
	}

	/**
	 * Removes a square from the collection.
	 * 
	 * @param s
	 *            the square to remove
	 */

	private class MouseHandler extends MouseAdapter {
		public void mousePressed(MouseEvent event) {

		}

		public void mouseClicked(MouseEvent event) {

			mouse_point_x = event.getPoint().getX() + 40;
			mouse_point_y = event.getPoint().getY() + 40;
			double velocity = data.BALL_SPEED;
			double ang = Math.atan2(-(mouse_point_y - ballArray.get().get(15)
					.getY()), (mouse_point_x - ballArray.get().get(15).getX()));
			dx = velocity * Math.cos(ang);
			dy = -velocity * Math.sin(ang);
			frame.addBall();

		}
	}

	private class MouseMotionHandler implements MouseMotionListener {
		public void mouseMoved(MouseEvent event) {

			add(event.getPoint());
			setCursor(cursor);
		}

		public void mouseDragged(MouseEvent event) {

		}
	}

	private class MouseWheelEventDemo implements MouseWheelListener {
		public void mouseWheelMoved(MouseWheelEvent e) {
			int notches = e.getWheelRotation();
			if (notches < 0) {
				// System.out.println("+");
				((BounceFrame) Bounce.getFrame()).sliderInc();
			} else {
				// System.out.println("-");
				((BounceFrame) Bounce.getFrame()).sliderDec();
			}

		}
	}
	
//	private class AboutDialog extends JDialog{
//		
//	}

	public static double getDX() {
		return dx;
	}

	public static double getDY() {
		return dy;
	}

	private double mouse_point_x;
	private double mouse_point_y;
	private static double dx;
	private static double dy;
	private double whit_x;
	private double whit_y;
	private Line2D line;
	private BounceFrame frame;
	private static final double SIDELENGTH = 20.0;
	private Ellipse2D current;
	// the square containing the mouse cursor
	// Get the default toolkit
	Toolkit toolkit = Toolkit.getDefaultToolkit();

	// Load an image for the cursor
	Image image = toolkit.getImage("D:/Documents/pencil.gif");
	// Create the hotspot for the cursor
	Point hotSpot = new Point(0, 0);
	// Cursor cursor = toolkit.createCustomCursor(image, hotSpot, "Pencil");
	Cursor cursor = toolkit.createCustomCursor(toolkit.createImage(""),
			new Point(), null);

	// Cursor cursor1 = toolkit.c;
}