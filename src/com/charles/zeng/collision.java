package com.charles.zeng;

import java.util.ArrayList;

// detect collision
class collision {
	public synchronized static void checkCollision() {
		for (Ball b : ballArray.get()) {
			for (Ball c : ballArray.get()) {
				if (b.getBallID() > c.getBallID()
						&& b.get_ball_in_pocket() == false
						&& c.get_ball_in_pocket() == false) {
					if (Math.pow((b.getX() - c.getX()), 2.0)
							+ Math.pow((b.getY() - c.getY()), 2.0) < data.XSIZE
							* data.YSIZE) {
						// if (b.getBallID() == 15 && c.getBallID() == 17) {
						if (b.getBallID() == 15)
							Pocket.set_first_hit_num(c.getBallID());
						// System.out.println(b.getBallID() + " " +
						// c.getBallID());
						// System.out.println(Math.pow((b.getX() - c.getX()),
						// 2.0)
						// + Math.pow((b.getY() - c.getY()), 2.0));
						rollback(b, c);
						// System.out.println(Math.pow((b.getX() - c.getX()),
						// 2.0)
						// + Math.pow((b.getY() - c.getY()), 2.0));
						makeCollision(b, c);
						// }
					}
				}
			}
		}
	}

	public static void rollback(Ball b, Ball c) {
		Ball first = b;
		Ball second = c;

		double f_dx = first.getDX();
		double s_dx = second.getDX();
		double f_dy = first.getDY();
		double s_dy = second.getDY();
		double f_x = first.getX();
		double s_x = second.getX();
		double f_y = first.getY();
		double s_y = second.getY();

		double f_v = Math.sqrt(f_dx * f_dx + f_dy * f_dy);
		double s_v = Math.sqrt(s_dx * s_dx + s_dy * s_dy);

		double overlap = data.XSIZE
				- Math.sqrt(Math.pow((b.getX() - c.getX()), 2.0)
						+ Math.pow((b.getY() - c.getY()), 2.0));
		// System.out.println(f_dx / f_v * overlap);
		// System.out.println(f_dy / f_v * overlap);
		// System.out.println(s_dx/s_v*overlap);
		if (f_v != 0) {
			first.setX(f_x - f_dx / f_v * overlap);
			first.setY(f_y - f_dy / f_v * overlap);
		} else {
			first.setX(f_x);
			first.setY(f_y);
		}
		if (s_v != 0) {
			second.setX(s_x - s_dx / s_v * overlap);
			second.setY(s_y - s_dy / s_v * overlap);
		} else {
			second.setX(s_x);
			second.setY(s_y);
		}
		// first.setX(f_x-f_dx /f_v*overlap);
		// first.setY(f_y);
		// second.setX(s_x-s_dx/s_v*overlap);
		// second.setY(70);
	}

	public static void makeCollision(Ball b, Ball c) {

		oneCollision(b, c);
		double new2_f_dx = new_f_dx;
		double new2_f_dy = new_f_dy;
		double new2_s_dx = new_s_dx;
		double new2_s_dy = new_s_dy;
		oneCollision(c, b);
		b.setDX(new2_f_dx + new_s_dx);
		b.setDY(new2_f_dy + new_s_dy);
		c.setDX(new2_s_dx + new_f_dx);
		c.setDY(new2_s_dy + new_f_dy);
		// b.setDX(new2_f_dx);
		// b.setDY(new2_f_dy);
		// c.setDX(new2_s_dx);
		// c.setDY(new2_s_dy);

	}

	public static void oneCollision(Ball b, Ball c) {
		Ball first = b;
		Ball second = c;

		double f_dx = first.getDX();
		double s_dx = second.getDX();
		double f_dy = first.getDY();
		double s_dy = second.getDY();

		double anglefirst = Math.atan2(-(c.getY() - b.getY()), c.getX()
				- b.getX());

		double oriAngle = Math.atan2(-f_dy, f_dx);

		// System.out.println("oriAngle "+oriAngle);

		anglefirst -= Math.PI / 2;

		double diff1 = Math.abs(oriAngle - anglefirst);

		while (diff1 > 2 * Math.PI)
			diff1 -= 2 * Math.PI;

		if (diff1 >= Math.PI / 2) {
			anglefirst -= Math.PI;
		}
		anglefirst = reviseAngle(anglefirst);
		// System.out.println("anglefirst "+anglefirst);

		double anglesecond = anglefirst - Math.PI / 2;

		double diff2 = Math.abs(oriAngle - anglesecond);

		while (diff2 > 2 * Math.PI)
			diff2 -= 2 * Math.PI;

		if (diff2 >= Math.PI / 2) {
			anglesecond -= Math.PI;
		}

		anglesecond = reviseAngle(anglesecond);
		// System.out.println("anglesecond "+anglesecond);

		new_f_dx = Math.cos(anglefirst) * Math.sqrt(f_dx * f_dx + f_dy * f_dy)
				* Math.abs(Math.cos(diff1)) * data.COLLIDE_ZUNI;
		new_f_dy = -Math.sin(anglefirst) * Math.sqrt(f_dx * f_dx + f_dy * f_dy)
				* Math.abs(Math.cos(diff1)) * data.COLLIDE_ZUNI;
		new_s_dx = Math.cos(anglesecond) * Math.sqrt(f_dx * f_dx + f_dy * f_dy)
				* Math.abs(Math.cos(diff2)) * data.COLLIDE_ZUNI;
		new_s_dy = -Math.sin(anglesecond)
				* Math.sqrt(f_dx * f_dx + f_dy * f_dy)
				* Math.abs(Math.cos(diff2)) * data.COLLIDE_ZUNI;
	}

	public static double reviseAngle(double my_ang) {
		double ang = my_ang;
		while (ang > Math.PI)
			ang -= 2 * Math.PI;
		while (ang < -Math.PI)
			ang += 2 * Math.PI;

		return (ang);
	}

	private static double new_f_dx, new_f_dy, new_s_dx, new_s_dy;
	// private static int collideballs[][];

}