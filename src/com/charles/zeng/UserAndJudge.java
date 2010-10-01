package com.charles.zeng;

import java.awt.Component;
import java.util.concurrent.locks.*;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

class judge {
	public void judgeball(int i) {

		judge_white_ball(); // check if the user hit the white ball into
		// pocket
		first_ball_color(); // check the first ball color, and set rules to
		// user
		// and AI
		current_user = i;
		this.frame = Bounce.getFrame();
	}

	public static void judge_white_ball() {
		if (Pocket.get_Poc_num() == 15) {

			int x = 170, y = 195;
			int y_inc = 20;
			Ball b = ballArray.get().get(15);
			Boolean collide;

			do {
				collide = false;
				ballArray.get().get(15).setX(x); // ballArray.get() return all
				// balls
				ballArray.get().get(15).setY(y + y_inc);
				for (Ball c : ballArray.get()) {
					if (b.getBallID() > c.getBallID()) {
						if (Math.pow((x - c.getX()), 2.0)
								+ Math.pow((y + y_inc - c.getY()), 2.0) < data.XSIZE
								* data.YSIZE) {
							collide = true;

						}
					}
				}
				y_inc += 20;
			} while (collide == true);
			b.set_ball_in_pocket(false);	//reset white ball
			b.set_disappear_time(10000);	//reset white ball
		}
	}

	public static void first_ball_color() {
		if (Pocket.get_Poc_num() >= 0 && Pocket.get_Poc_num() != 15
				&& Pocket.get_divide() == 3) {
			int div = Pocket.get_Poc_num() % 2;
			Pocket.set_divide(div); // user to set divide, not AI
			// System.out.println("inner"+div);
			user_ball_color = div;
			// System.out.println(div+" "+Pocket.get_Poc_num());
		}
	}

	// public static void set_current_user(int i) {
	// current_user=i;
	// }

	public synchronized int who_is_next() {
		// System.out.println(current_user);
		// for (Ball b : ballArray.get()){
		// if(b.get_ball_in_pocket()==true){
		// System.out.print(b.getBallID()+" ");
		// }
		// }System.out.println();

		boolean odd_finish = true, even_finish = true; // not really true
		for (Ball b : ballArray.get()) {
			if (b.getBallID() % 2 == 0 && b.get_ball_in_pocket() == false) {
				even_finish = false;
			}
			if (b.getBallID() % 2 == 1 && b.getBallID() != 15
					&& b.get_ball_in_pocket() == false) {
				odd_finish = false;
			}
		}
		if (odd_finish == true) {
			if (user_ball_color == 1) {
//				System.out.println("User win");
				JOptionPane
						.showMessageDialog(frame,
								"I hope none of the ladies were watching. YOU'VE GOT GAME !!!");
			} else {
//				System.out.println("Computer win");
				JOptionPane
						.showMessageDialog(frame,
								"Did you actually think you were going to beat me? I AM THE WINNER !!!");
			}
			return (0);
		} else if (even_finish == true) { // even_finish
			if (user_ball_color == 0) {
//				System.out.println("User win");
				JOptionPane
						.showMessageDialog(frame,
								"I hope none of the ladies were watching. YOU'VE GOT GAME !!!");
			} else {
//				System.out.println("Computer win");
				JOptionPane
						.showMessageDialog(frame,
								"Did you actually think you were going to beat me? I AM THE WINNER !!!");
			}
			return (0);
		}
		if (current_user == 0) {

			if ((Pocket.get_Poc_num() % 2 == user_ball_color || Pocket
					.get_divide() == 3)
					&& (Pocket.get_Poc_num() >= 0 && Pocket.get_Poc_num() != 15)) {
				((BounceFrame) frame).get_MousePanel().add();
				// set initial mouse line for every thread finish time
				((BounceFrame) frame).get_MousePanel().setVisible(true);
				next_user = 0;
//				 System.out.println("0 0");
			} else {
				((BounceFrame) frame).get_MousePanel().setVisible(false);
				((BounceFrame) frame).addBall_auto();
				next_user = 1;
//				 System.out.println("0 1");
			}

		} else {
			// System.out.println("poc_num"+Pocket.get_Poc_num()+ " "+
			// user_ball_color);
			if ((Pocket.get_Poc_num() % 2 != user_ball_color || Pocket
					.get_divide() == 3)
					&& (Pocket.get_Poc_num() >= 0 && Pocket.get_Poc_num() != 15)) {
				((BounceFrame) frame).get_MousePanel().setVisible(false);
				((BounceFrame) frame).addBall_auto();
				next_user = 1;
//				 System.out.println("1 1");
			} else {
				((BounceFrame) frame).get_MousePanel().add();
				((BounceFrame) frame).get_MousePanel().setVisible(true);
				next_user = 0;
//				 System.out.println("1 0");
			}
		}
		return (0);
	}

	public static int get_current_user() {
		return current_user;
	}
	public static void set_current_user(int i) {
		current_user=i;
	}

	public static int get_next_user() {
		return next_user;
	}
	public static void set_next_user(int i) {
		 next_user=i;
	}

	public static void set_user_ball_color(int i) {
		user_ball_color = i;
	}

	public static int get_user_ball_color() {
		return user_ball_color;
	}

	private Thread user, comp;
	private Component frame;
	private static int next_user = 0;
	private static int user_ball_color = -1;
	private static int current_user = 1; // 0 for user, 1 for computer
	private Lock bankLock = new ReentrantLock();
}
