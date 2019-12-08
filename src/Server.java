
/*
 * FileName : Server.java
 * Author : Mayank Khera & Johnathan Gonzalez
 * Course : CST8221 -- JAP, Lab Section : 311
 * Assignment : 2
 * Date : December 6, 2019
 * Professor : Daniel Cormier
 * Purpose : 
 */

import java.awt.Dimension;
import java.awt.EventQueue;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JFrame;

/**
 * 
 * @author Mayank Khera & Johnathan Gonzalez
 * @version 1
 * @see default package
 * @since
 *
 */
public class Server {

	/**
	 * main method responsible to
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		int port;

		if (args.length > 0) {
			port = Integer.parseInt(args[0]);
			System.out.println("Using port: " + port);
		} else {
			port = 65535;
			System.out.println("Using default port: " + port);
		}

		ServerSocket serverSocket;

		try {
			serverSocket = new ServerSocket(port);

			int friend = 0;

			while (true) {
				Socket socket = serverSocket.accept();

				if (socket.getSoLinger() != -1)
					socket.setSoLinger(true, 5);

				if (!socket.getTcpNoDelay())
					socket.setTcpNoDelay(true);

				System.out.println("Connecting to a client Socket[addr=" + socket.getInetAddress() + ", port="
						+ socket.getPort() + ", localport=" + socket.getLocalPort() + "]");

				friend++;

				final String title = "Mayank's Friend " + friend;

				launchClient(socket, title);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * launches the another frame with friend's chat UI
	 * 
	 * @param socket
	 * @param title
	 */
	public static void launchClient(Socket socket, String title) {

		EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {
				ServerChatUI scui = new ServerChatUI(socket);
				scui.setTitle(title);
				scui.setMinimumSize(new Dimension(585, 500));
				scui.setLocationRelativeTo(null);
				scui.setResizable(false);
				scui.setVisible(true);
				scui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			}

		});

	}

}
