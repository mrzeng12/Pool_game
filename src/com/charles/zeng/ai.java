package com.charles.zeng;

import java.util.Random;

import java.util.ArrayList;

class Pocket {
	private static int Poc_num = -1;
	private static int first_hit_num = -1;
	private static int hit_wall = 0;
	private static int divide = 3;

	public static int get_Poc_num() {
		return Poc_num;
	}

	public static int get_first_hit_num() {
		return first_hit_num;
	}

	public static int get_hit_wall() {
		return hit_wall;
	}

	public static int get_divide() {
		return divide;
	}

	public static void add(int id) {
		if (Poc_num != 15) // whenever a whiteball is in, Poc_num won't change
			Poc_num = id;
	}

	public static void set_hit_wall() {
		if (hit_wall == 0)
			hit_wall = 1;
	}

	public static void set_first_hit_num(int id) {
		if (first_hit_num == -1) {
			first_hit_num = id;
			// System.out.println(first_hit_num);
		}
	}
	public static void reset_first_hit_num() {
		first_hit_num= -1;
	}

	public static void reset_Poc_num() {
		Poc_num = -1;
	}
	public static void reset_hit_wall() {
		hit_wall= 0;
	}

	public static void set_divide(int id) {
		divide = id;
	}

	public static void backup() {
		Poc_num = -1;
		first_hit_num = -1;
		hit_wall = 0;
	}

	public static void restore() {
	}
}

class auto {

	public static void calculate() {
		double velocity = Math.random() * 3.0 + 5.0;
		double random;
		double temp_dx = -20;
		double temp_dy = -20;
		// dx = 4;
		// dy = 4;
		boolean success = false;
		boolean hitsth = false;
		boolean hitwall = false;
		for (double ang = 0; ang < 100; ang += data.AI) {
			random = ang / 100.0;
			double angle = 2 * Math.PI * random - Math.PI;
			dx = velocity * Math.cos(angle);
			dy = -velocity * Math.sin(angle);

			Pocket.backup();
			ballArray.backup();

			for (Ball b : ballArray.get()) {
				b.init_auto();
			}
			for (int i = 1; i <= data.STEPS; i++) {
				for (Ball b : ballArray.get()) {
					b.move(i);
				}
				// check if two ball collides
				collision.checkCollision();
				if (ballArray.totalspeed() == 0)
					break; // fast quit
			}

			if (Pocket.get_first_hit_num() >= 0
					&& Pocket.get_first_hit_num() % 2 != Pocket.get_divide()
					// initial: 3, can hit everyball
					&& Pocket.get_Poc_num() != 15) {
				if (Pocket.get_Poc_num() >= 0 && Pocket.get_Poc_num() != 15
						&& Pocket.get_Poc_num() % 2 != Pocket.get_divide()) {

					if (Pocket.get_divide() == 3) {
						// after hit in the first ball, the color allowed to hit
						// is set
						int div = (int) (-(Pocket.get_Poc_num() % 2 - 0.5) + 0.5);
						// 1 changes to 0, 0 changes to 1
						// System.out.println(Pocket.get_Poc_num()+" "+div);
						Pocket.set_divide(div); // for AI to set divide, only
												// once
						// System.out.println("inner"+div);
						judge.set_user_ball_color(div);
					}
					success = true;
					 System.out.println("pocket");
					 System.out.println();
					break;
				} else if (temp_dx == -20 && temp_dy == -20) {
					// hit right ball, but not into the pocket
					temp_dx = dx;
					temp_dy = dy;
					hitsth = true;
					if (Pocket.get_hit_wall() == 1){
						hitwall = true;
						System.out.println("hit wall");
					}
					else
					 System.out.println("direct hit");
				} else if (hitwall == true && Pocket.get_hit_wall() == 0) {
					// previous one hit the wall, choose another one
					temp_dx = dx;
					temp_dy = dy;
					hitwall = false;
					 System.out.println("hit ball-revised");
				}
			}

			Pocket.restore();
			ballArray.restore();

		}
		Pocket.restore();
		ballArray.restore();
		if (success == false) {
			if (hitsth == true) {
				dx = temp_dx;
				dy = temp_dy;
			} else {
				dx = 4;
				dy = 2;
			}

		}
		// System.out.println(dx + " " + dy);
	}

	public static double get_dx() {
		return dx;
	}

	public static double get_dy() {
		return dy;
	}

	private static double dx;
	private static double dy;
}