
/*
 * FileName : Client.java
 * Author : Mayank Khera & Johnathan Gonzalez
 * Course : CST8221 -- JAP, Lab Section : 311
 * Assignment : 2
 * Date : December 6, 2019
 * Professor : Daniel Cormier
 * Purpose : 
 */

import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;

/**
 * 
 * @author Mayank Khera & Johnathan Gonzalez
 * @version 1
 * @see default package
 * @since
 *
 */
public class Client {

	/**
	 * main method creates a UI before making the frame visible
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {
				ClientChatUI clientui = new ClientChatUI("Mayank's client UI");
				clientui.setMinimumSize(new Dimension(588, 500));
				clientui.setResizable(false);
				clientui.setLocationByPlatform(true);
				clientui.setVisible(true);
				clientui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			}

		});

	}

}
