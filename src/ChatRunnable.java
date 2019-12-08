
/*
 * FileName : ChatRunnable.java
 * Author : Mayank Khera & Johnathan Gonzalez
 * Course : CST8221 -- JAP, Lab Section : 311
 * Assignment : 2
 * Date : December 6, 2019
 * Professor : Daniel Cormier
 * Purpose : 
 */

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.JFrame;
import javax.swing.JTextArea;

/**
 * 
 * @author Mayank Khera & Johnathan Gonzalez
 * @version 1
 * @see default package
 * @since
 *
 */
public class ChatRunnable<T extends JFrame & Accessible> implements Runnable {

	private final T ui;
	private Socket socket;
	private ObjectInputStream inputStream;
	private ObjectOutputStream outputStream;
	private JTextArea display;

	/**
	 * 
	 * @param ui
	 * @param connection
	 */
	public ChatRunnable(T ui, ConnectionWrapper connection) {
		this.socket = connection.getSocket();
		this.inputStream = connection.getInputStream();
		this.outputStream = connection.getOutputStream();
		this.ui = ui;
		this.display = ui.getDisplay();

	}

	/**
	 * 
	 */
	public void run() {
		String strin = "";
		while (true) {
			if (!socket.isClosed()) {
				try {
					strin = (String) inputStream.readObject();

					DateTimeFormatter format = DateTimeFormatter.ofPattern("MMMM dd, HH:mm a");
					String date = format.format(LocalDateTime.now());
					if (strin.trim().equals(ChatProtocolConstants.CHAT_TERMINATOR)) {
						final String terminate = ChatProtocolConstants.DISPLACEMENT + date
								+ ChatProtocolConstants.LINE_TERMINATOR + strin;
						display.append(terminate);
						break;
					} else {
						final String append = ChatProtocolConstants.DISPLACEMENT + date
								+ ChatProtocolConstants.LINE_TERMINATOR + strin;
						display.append(append);
					}
				} catch (ClassNotFoundException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					break;
				}
			} else
				break;
		}

		if (!socket.isClosed()) {
			try {
				outputStream.writeObject(ChatProtocolConstants.DISPLACEMENT + ChatProtocolConstants.CHAT_TERMINATOR
						+ ChatProtocolConstants.LINE_TERMINATOR);
			} catch(IOException e) {
				e.printStackTrace();
			}finally {
				ui.closeChat();
			}
		}

		
	}

}
