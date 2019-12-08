
/*
 * FileName : ConnectionWrapper.java
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

/**
 * 
 * @author Mayank Khera & Johnathan Gonzalez
 * @version 1
 * @see default package
 * @since
 *
 */
public class ConnectionWrapper {

	private Socket socket;
	private ObjectOutputStream outputStream;
	private ObjectInputStream inputStream;

	/**
	 * constructor
	 * 
	 * @param socket
	 */
	public ConnectionWrapper(Socket socket) {
		this.socket = socket;
	}

	/**
	 * gettern for socket
	 * 
	 * @return socket
	 */
	public Socket getSocket() {
		return socket;
	}

	/**
	 * getter for the outputStream
	 * 
	 * @return ObjectOutputStream
	 */
	public ObjectOutputStream getOutputStream() {
		return outputStream;
	}

	/**
	 * getter for inputStream
	 * 
	 * @return OnjectInputStream
	 */
	public ObjectInputStream getInputStream() {
		return inputStream;
	}

	/**
	 * creates a new instance of an ObjectInputStream and returns it
	 * 
	 * @return ObjectInputStream
	 * @throws IOException
	 */
	public ObjectInputStream createObjectIStreams() throws IOException {
		inputStream = new ObjectInputStream(socket.getInputStream());
		return inputStream;
	}

	/**
	 * creates a new Instance for ObjectOutputStream and assigns it to the
	 * outputStream
	 * 
	 * @return ObjectOutputStream
	 * @throws IOException
	 */
	public ObjectOutputStream createObjectOStreams() throws IOException {
		outputStream = new ObjectOutputStream(socket.getOutputStream());
		return outputStream;
	}

	/**
	 * creates both input and output stream instances
	 * 
	 * @throws IOException
	 */
	public void createStreams() throws IOException {
		try {
			createObjectOStreams();
			createObjectIStreams();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * tries to close all connection if stream and socket are not null
	 * 
	 * @throws IOException
	 */
	public void closeConnection() throws IOException {

		if (outputStream != null)
			outputStream.close();
		if (inputStream != null)
			inputStream.close();
		if (socket != null && !socket.isClosed())
			socket.close();
	}
	
}
