package com.charles.zeng;

import java.util.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.*;
import javax.swing.event.*;

/**
 * The frame with panel and buttons.
 */
class BounceFrame extends JFrame {
	/**
	 * Constructs the frame with the panel for showing the bouncing ball and
	 * Start and Close buttons
	 */
	public BounceFrame() {
		setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

		setTitle("BounceThread");
		add(lpane, BorderLayout.CENTER);
		lpane.setBounds(0, 0, 900, 550);

		lpane.add(panel_bg, new Integer(0), 0);
		// lpane.setBackground(Color.white);
		panel_bg.setBounds(0, 0, 900, 550);
		panel_bg.setOpaque(true);
		panel_bg.setBackground(Color.white);

		panel = new BallPanel(); // will be repainted by ball-thread
		// panel.add(panel1);
		lpane.add(panel, new Integer(1), 0);
		// lpane.setBackground(Color.white);
		panel.setBounds(0, 0, 900, 450);
		panel.setOpaque(false);
		JPanel buttonPanel = new JPanel();
		
		faceCombo=new JComboBox();
		faceCombo.addItem("Beginner");
		faceCombo.addItem("Medium");
		faceCombo.addItem("Professional");
		faceCombo.addItem("Expert");
		buttonPanel.add(faceCombo);
		faceCombo.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				if(faceCombo.getSelectedIndex()==0){
					data.AI=5;
				}else if(faceCombo.getSelectedIndex()==1){
					data.AI=3;
				}else if(faceCombo.getSelectedIndex()==2){
					data.AI=1;
				}else if(faceCombo.getSelectedIndex()==3){
					data.AI=0.5;
				}
//				System.out.println(data.AI);
			}
		});
		
		addButton(buttonPanel, "Start / Reset", new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				initial.startBall(panel);
			}
		});

		slider = new JSlider(SwingConstants.HORIZONTAL,1,10,7);
		slider.setMajorTickSpacing(1);
		slider.setBackground(Color.white);
		slider.setPaintTicks(true);
		slider.setSnapToTicks(true);
//		slider.setValue(4);
		ChangeListener listener = new ChangeListener() {
			public void stateChanged(ChangeEvent event) {
				JSlider source = (JSlider) event.getSource();
				data.BALL_SPEED = source.getValue();
			}
		};
		buttonPanel.add(slider);
		slider.addChangeListener(listener);
		
		// addButton(buttonPanel, "Add", new ActionListener() {
		// public void actionPerformed(ActionEvent event) {
		// addBall();
		// }
		// });

		// addButton(buttonPanel, "Auto", new ActionListener() {
		// public synchronized void actionPerformed(ActionEvent event) {
		// panel3.setVisible(false);
		// // System.out.println("what");
		// auto.calculate(); // calculate dx, dy
		// addBall_auto(); // start thread
		// }
		// });

		addButton(buttonPanel, "Close", new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				System.exit(0);
			}
		});

		lpane.add(buttonPanel, new Integer(1), 0);
		// buttonPanel.setBackground(Color.white);
		buttonPanel.setBounds(0, 450, 650, 100);
		buttonPanel.setOpaque(false);

		panel3 = new MousePanel(this);
		panel3.setBounds(50, 50, 700, 350);
		panel3.setOpaque(false);
		lpane.add(panel3, new Integer(2), 0);

	}

	/**
	 * Adds a button to a container.
	 * 
	 * @param c
	 *            the container
	 * @param title
	 *            the button title
	 * @param listener
	 *            the action listener for the button
	 */
	public void addButton(Container c, String title, ActionListener listener) {
		JButton button = new JButton(title);
		c.add(button);
		button.addActionListener(listener);
	}

	/**
	 * Adds a bouncing ball to the canvas and starts a thread to make it bounce
	 */

	public synchronized void addBall() {

		panel3.setVisible(false);

		Runnable r = new BallRunnable(panel, panel3);
		t1 = new Thread(r);
		t1.start();
		// System.out.println("addball");
	}

	public synchronized void addBall_auto() {
		// try {
		// wait();
		// }
		// catch (InterruptedException e) {
		// }
		// while(t1.isAlive()){}
		Runnable r = new BallAuto(panel, panel3);
		t2 = new Thread(r);
		t2.start();
	}

	public MousePanel get_MousePanel() {
		return panel3;
	}
	public void sliderInc(){
		if(slider.getValue()<10)
			slider.setValue(slider.getValue()+1);
	}
	public void sliderDec(){
		if(slider.getValue()>0)
			slider.setValue(slider.getValue()-1);
	}
	public Thread get_t1(){
		return t1;
	}
	public Thread get_t2(){
		return t2;
	}
	public void stop() {
        t1 = null;
        t2=null;
    }

	private JLayeredPane lpane = new JLayeredPane();
	private BallPanel panel;
	private DrawComponent panel_bg = new DrawComponent();
	private MousePanel panel3;
	private volatile Thread t1, t2;
	private JSlider slider;
	private JComboBox faceCombo;
	public static final int DEFAULT_WIDTH = 900;
	public static final int DEFAULT_HEIGHT = 550;
	// public static final int STEPS = 1000;
	// public static final int DELAY = 3;
}
