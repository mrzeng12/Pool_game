package com.charles.zeng;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

/**
 * A ball that moves and bounces off the edges of a rectangle
 */
class ballArray {
	private static ArrayList<Ball> balls = new ArrayList<Ball>();
	private static double[] ghost_balls_x = new double[16];
	private static double[] ghost_balls_y = new double[16];
	private static boolean[] ghost_balls_pocket = new boolean[16];
	private static int[] ghost_disappear_time = new int[16]; 

	public static void add(Ball myball) {
		balls.add(myball);
	}

	public static void remove(int index) {
		balls.remove(index);
	}

	public static ArrayList<Ball> get() {
		return balls;
	}

	public static void backup() {
		for (int i = 0; i < balls.size(); i++) {
			ghost_balls_x[i] = balls.get(i).getX();
			ghost_balls_y[i] = balls.get(i).getY();
			ghost_balls_pocket[i] = balls.get(i).get_ball_in_pocket();
			ghost_disappear_time[i] = balls.get(i).get_disappear_time();			
		}
	}

	public static void restore() {
		for (int i = 0; i < balls.size(); i++) {
			balls.get(i).setX(ghost_balls_x[i]);
			balls.get(i).setY(ghost_balls_y[i]);
			balls.get(i).set_ball_in_pocket(ghost_balls_pocket[i]);
			balls.get(i).set_disappear_time(ghost_disappear_time[i]);
		}
	}

	public static double totalspeed() {
		double t_speed = 0;
		for (Ball b : balls) {
			t_speed += b.getDX();
			t_speed += b.getDY();
		}
		return t_speed;
	}

}

class Ball {
	public Ball() {
		myBallID = ballID;
		ballID++;
	}

	public Ball(double x, double y) {
		myBallID = ballID;
		ballID++;
		this.x = x;
		this.y = y;
	}

	/**
	 * Moves the ball to the next position, reversing direction if it hits one
	 * of the edges
	 */
	public synchronized void move(int i) {

		x += dx;
		y += dy;		
		
		double sq = 14.142;
		double ab = 7.071;
		// hit the wall
		if (this.myBallID != Pocket.get_Poc_num()) { // if already moving out,
			// no bounce
			if (x < 50) {
				if ((y + 10 > 50 + 2 * sq - ab) && (y + 10 < 400 - 2 * sq + ab)) {
					x = 50;
					dx = -dx * data.WALL_ZUNI;
					if (this.getBallID() == 15)
						Pocket.set_hit_wall(); // judge if white ball hit the
					// wall
				} else {
					Pocket.add(this.myBallID);
					set_ball_in_pocket(true);
					set_disappear_time(i);
				}
			}
			if (x + XSIZE >= 750 && x + XSIZE <= 800) {
				if ((y + 10 > 50 + 2 * sq - ab) && (y + 10 < 400 - 2 * sq + ab)) {
					x = 750 - XSIZE;
					dx = -dx * data.WALL_ZUNI;
					if (this.getBallID() == 15)
						Pocket.set_hit_wall();
				} else {
					Pocket.add(this.myBallID);
					set_ball_in_pocket(true);
					set_disappear_time(i);
				}
			}
			if (y < 50 && x + XSIZE <= 800) {
				if (((x + 10 > 50 + 2 * sq - ab) && (x + 10 < 385 + ab))
						|| (x + 10 > 415 - ab) && (x + 10 < 750 - 2 * sq + ab)) {
					y = 50;
					dy = -dy * data.WALL_ZUNI;
					if (this.getBallID() == 15)
						Pocket.set_hit_wall();
					// System.out.println(this.getBallID() + " " + "bounce");
				} else {
					Pocket.add(this.myBallID);
					set_ball_in_pocket(true);
					set_disappear_time(i);
				}
			}
			if (y + YSIZE >= 400 && x + XSIZE <= 800) {
				if (((x + 10 > 50 + 2 * sq - ab) && (x + 10 < 385 + ab))
						|| (x + 10 > 415 - ab) && (x + 10 < 750 - 2 * sq + ab)) {
					y = 400 - YSIZE;
					dy = -dy * data.WALL_ZUNI;
					if (this.getBallID() == 15)
						Pocket.set_hit_wall();
				} else {
					Pocket.add(this.myBallID);
					set_ball_in_pocket(true);
					set_disappear_time(i);
				}
			}
		}
		// move out of bounds, and show the movie
		if (x < 30) {
			set_aside();
		}
		if (x + XSIZE >= 770 && x + XSIZE <= 800) {
			set_aside();
		}
		if (y < 30 && x + XSIZE <= 800) {
			set_aside();
		}
		if (y + YSIZE >= 420 && x + XSIZE <= 800) {
			set_aside();
		}
		if(i>get_disappear_time()+40)	
			//disappear after certain time, in case the previous code does not work
			set_aside();

		double temp = Math.sqrt(dx * dx + dy * dy);
//		if (temp <= data.STOP_SPEED) {
//			dx = 0;
//			dy = 0;
////		} else if (this.myBallID != Pocket.get_Poc_num()) { // if already moving
//			// out, no ZUNI
//		} else if (this.get_ball_in_pocket()==false) { // if already moving
//			dx *= data.ZUNI;
//			dy *= data.ZUNI;
//		}
		if (this.get_ball_in_pocket()==false) {
			if (temp <= data.STOP_SPEED) {
				dx = 0;
				dy = 0;
			}
			else{
				dx *= data.ZUNI;
				dy *= data.ZUNI;
			}
		}
	}

	public synchronized void init() { // initialize the movement, only the
		// latest move(white)
		if (myBallID == ballID - 1) {
			// x = data.POS_DX;
			// y = data.POS_DY;
			// dx = data.BALL_DX;
			// dy = data.BALL_DY;
			dx = MousePanel.getDX();
			dy = MousePanel.getDY();
		} else {
			dx = 0;
			dy = 0;
		}
	}

	public synchronized void init_auto() { // initialize the movement, only the
		// latest
		// move(white)
		if (myBallID == ballID - 1) {
			dx = auto.get_dx();
			dy = auto.get_dy();
		} else {
			dx = 0;
			dy = 0;
		}
	}

	/**
	 * Gets the shape of the ball at its current position.
	 */
	public Ellipse2D getShape() {
		return new Ellipse2D.Double(x, y, XSIZE, YSIZE);
	}

	public String message() {
		return Integer.toString(this.myBallID);
	}

	public void set_aside() {//decide where to put pocket balls before Judge
		x = 835;
		//y = 22 * this.myBallID;
		dx = 0;
		dy = 0;
		// System.out.println(judge.get_current_user()+" "+this.myBallID);
		if (judge.get_user_ball_color() == -1) {
			// the position should be decided before Judge
			if (judge.get_next_user() == 0) {
				y = 40 + (this.myBallID / 2) * 22;
			} else if (judge.get_next_user() == 1) {
				y = 260 + (this.myBallID / 2) * 22;
			}
		} else if (judge.get_user_ball_color() == 1) {
			if (this.getBallID() % 2 == 1) {
				y = 40 + (this.myBallID / 2) * 22;
			} else {
				y = 260 + (this.myBallID / 2) * 22;
			}
		} else if (judge.get_user_ball_color() == 0) {
			if (this.getBallID() % 2 == 0) {
				y = 40 + (this.myBallID / 2) * 22;
			} else {
				y = 260 + (this.myBallID / 2) * 22;
			}
		}
	}

	public void setDX(double dx) {
		this.dx = dx;
	}

	public void setDY(double dy) {
		this.dy = dy;
	}

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getDX() {
		return dx;
	}

	public double getDY() {
		return dy;
	}

	public int getBallID() {
		return myBallID;
	}
	public static void resetBallID(){
		ballID=0;
	}

	public boolean get_ball_in_pocket() {
		return ball_in_pocket;
	}

	public void set_ball_in_pocket(boolean i) {
		ball_in_pocket = i;
	}
	public void set_disappear_time(int i){
		disappear_time=i;
	}
	public int  get_disappear_time(){
		return disappear_time;
	}

	private static final int XSIZE = data.XSIZE;
	private static final int YSIZE = data.YSIZE;
	private double x = 0;
	private double y = 0;
	private double dx = 0;
	private double dy = 0;
	private boolean ball_in_pocket = false;
	private int disappear_time=10000;

	private int myBallID = 0;
	private static int ballID = 0;
}