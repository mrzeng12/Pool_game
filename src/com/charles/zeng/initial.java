package com.charles.zeng;

class initial{
	public static void startBall(BallPanel panel) {
		//reset static ball ID, clear all the balls
		((BounceFrame) Bounce.getFrame()).stop();		
		Ball.resetBallID();
		ballArray.get().clear();
		judge.set_current_user(1);
		judge.set_next_user(0);
		judge.set_user_ball_color(-1);
		Pocket.reset_Poc_num();
		Pocket.reset_hit_wall();
		Pocket.reset_first_hit_num();
		Pocket.set_divide(3);
		((BounceFrame) Bounce.getFrame()).get_MousePanel().add();
		((BounceFrame) Bounce.getFrame()).get_MousePanel().setVisible(true);
				
		
		Ball b;
		double x=680;
		double dx=data.LAYOUT_DX;
		
		double y=171;
		double dy=data.LAYOUT_DY;
		
		for(double i=y; i<y+9*dy; i=i+2*dy){
			b = new Ball(x,i);			
			ballArray.add(b);
		}
		for(double i=y+dy; i<y+8*dy; i=i+2*dy){
			b = new Ball(x-dx,i);			
			ballArray.add(b);
		}
		for(double i=y+2*dy; i<y+7*dy; i=i+2*dy){
			b = new Ball(x-2*dx,i);			
			ballArray.add(b);
		}
		for(double i=y+3*dy; i<y+6*dy; i=i+2*dy){
			b = new Ball(x-3*dx,i);			
			ballArray.add(b);
		}
		for(double i=y+4*dy; i<y+5*dy; i=i+2*dy){
			b = new Ball(x-4*dx,i);			
			ballArray.add(b);
		}
		//whiteball
		b=new Ball(data.POS_DX, data.POS_DY);
		ballArray.add(b);
		
		
//		for (Ball each_b : ballArray.get()) {
//			each_b.move(panel.getBounds());
//		}
		panel.repaint();
	}
}