
/*
 * FileName : ServerChatUI.java
 * Author : Mayank Khera & Johnathan Gonzalez
 * Course : CST8221 -- JAP, Lab Section : 311
 * Assignment : 2
 * Date : December 6, 2019
 * Professor : Daniel Cormier
 * Purpose : 
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

/**
 * 
 * @author Mayank Khera & Johnathan Gonzalez
 * @version 1
 * @see default package
 * @since
 *
 */
public class ServerChatUI extends JFrame implements Accessible {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1320531047649144439L;
	private Socket socket;
	private JTextField message;
	private JButton sendButton;
	private JTextArea display;
	private ObjectOutputStream outputStream;
	private ConnectionWrapper connection;

	/**
	 * constructor
	 * 
	 * @param socket
	 */
	public ServerChatUI(Socket socket) {
		this.socket = socket;
		setFrame(createUI());
		runClient();
	}

	/**
	 * getter for chat display
	 * 
	 * @returns JTextArea display
	 */
	public JTextArea getDisplay() {
		return display;
	}

	/**
	 * tries to close chat by closing all connection using an instance of
	 * connectionwrapper
	 */
	public void closeChat() {
		try {
			connection.closeConnection();
			dispose();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param panel
	 */
	public final void setFrame(JPanel panel) {
		add(panel);
		addWindowListener(new WindowController());
	}

	/**
	 * this method is responsible for making UI for Mayank's Friend
	 * 
	 * @return JPanel - mainPanel - with all the UI in it
	 */
	public JPanel createUI() {

		JPanel mainPanel = new JPanel();
		JPanel messageField = new JPanel();
		JPanel chatDisplay = new JPanel();
		JPanel topPanel = new JPanel();

		sendButton = new JButton("Send");

		TitledBorder displayBorder;

		message = new JTextField(41);

		display = new JTextArea(30, 45);

		JScrollPane scrollBar = new JScrollPane(display);

		Controller controller = new Controller();

		topPanel.setLayout(new BorderLayout());
		topPanel.add(messageField, BorderLayout.SOUTH);

		messageField.setBorder(
				BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK, 10), "MESSAGE"));
		messageField.add(message);
		messageField.add(sendButton);

		message.setText("Type message");

		sendButton.setPreferredSize(new Dimension(81, 19));
		sendButton.addActionListener(controller);
		sendButton.setActionCommand("send");
		sendButton.setMnemonic('S');

		displayBorder = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLUE, 10),
				" CHAT DISPLAY");
		displayBorder.setTitleJustification(TitledBorder.CENTER);

		chatDisplay.setLayout(new BorderLayout());
		chatDisplay.setBorder(displayBorder);
		chatDisplay.add(scrollBar);

		mainPanel.setLayout(new BorderLayout());
		mainPanel.setBackground(Color.GREEN);
		mainPanel.add(topPanel, BorderLayout.NORTH);
		mainPanel.add(chatDisplay, BorderLayout.CENTER);

		display.setEditable(false);
		display.setBackground(Color.WHITE);

		return mainPanel;

	}

	/**
	 * 
	 */
	public void runClient() {
		connection = new ConnectionWrapper(socket);

		try {
			connection.createStreams();
			outputStream = connection.getOutputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Runnable runnable = new ChatRunnable<ServerChatUI>(this, connection);
		Thread thread = new Thread(runnable);
		thread.start();
	}

	/**
	 * this class is responsible for closing the window and disposing off the frame
	 * 
	 * @author Mayank Khera & Johnathan Gonzalez
	 * @version 1
	 * @see default package
	 * @since
	 *
	 */
	class WindowController extends WindowAdapter {

		@Override
		public void windowClosing(WindowEvent event) {
			super.windowClosing(event);
			System.out.println("ServerUI window closing");
			try {
				outputStream.writeObject(ChatProtocolConstants.DISPLACEMENT + ChatProtocolConstants.CHAT_TERMINATOR
						+ ChatProtocolConstants.LINE_TERMINATOR);
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				dispose();
				System.out.println("Chat closed!");
			}
			dispose();
			System.exit(0);
		}

		/**
		 * once the window is closed this method prints out a statement on the console
		 */
		public void windowClosed(WindowEvent event) {
			super.windowClosed(event);
			System.out.println("Server UI Closed");
		}
	}

	/**
	 * this class implements the actionListners for sendButton
	 * 
	 * @author Mayank Khera & Johnathan Gonzalez
	 * @version 1
	 * @see default package
	 * @since
	 *
	 */
	class Controller implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent event) {
			String ac = event.getActionCommand();
			if (ac.equals("send"))
				send();
		}

		/**
		 * displays message sent on the chat display using outputStream
		 */
		private void send() {
			String sendMessage = message.getText();
			display.append(sendMessage + ChatProtocolConstants.LINE_TERMINATOR);

			try {
				outputStream.writeObject(
						ChatProtocolConstants.DISPLACEMENT + sendMessage + ChatProtocolConstants.LINE_TERMINATOR);
			} catch (IOException e) {
				display.append(e.getMessage());
			}
		}
	}

}
