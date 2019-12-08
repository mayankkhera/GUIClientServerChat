
/*
 * FileName : ClientChatUI.java
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
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
//import java.net.UnknownHostException;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
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
public class ClientChatUI extends JFrame implements Accessible {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7748147774884645479L;
	private JTextField message;
	private JTextArea display;
	private ObjectOutputStream outputStream;
	private Socket socket;
	private JButton sendButton;
	private ConnectionWrapper connection;
	private JTextField hostMessage;
	private JButton connect;
	private JComboBox<String> comboBox;

	/**
	 * Constructor
	 * 
	 * @param frameTitle
	 */
	public ClientChatUI(String frameTitle) {
		setTitle(frameTitle);
		runClient();
	}

	private void runClient() {
		setContentPane(createClientUI());
		addWindowListener(new WindowController());
	}

	/**
	 * This method is responsible for Client application UI, makes every thing fits
	 * it into mainPanel and returns it
	 * 
	 * @return JPanel mainPanel - panel with all the UI in it
	 */
	public JPanel createClientUI() {

		JPanel mainPanel = new JPanel();

		JPanel messageF = new JPanel();
		JPanel cDisplay = new JPanel();
		JPanel topPanel = new JPanel();
		JPanel connection = new JPanel();
		hostMessage = new JTextField(45);
		connect = new JButton("Connect");
		sendButton = new JButton("Send");
		JPanel hostPanel = new JPanel();
		JPanel portPanel = new JPanel();
		JLabel host = new JLabel("Host: ");
		JLabel port = new JLabel("Port: ");

		String[] ports = { "", "8089", "65000", "65535" };
		TitledBorder displayBorder;
		message = new JTextField(41);
		display = new JTextArea(30, 45);
		JScrollPane scrollBar = new JScrollPane(display);
		comboBox = new JComboBox<String>(ports);
		Controller controller = new Controller();

		host.setPreferredSize(new Dimension(35, 30));
		host.setDisplayedMnemonic('H');
		host.setLabelFor(hostMessage);

		port.setPreferredSize(new Dimension(35, 30));
		port.setDisplayedMnemonic('P');
		port.setLabelFor(comboBox);

		connection.setLayout(new BorderLayout());
		connection.add(hostPanel, BorderLayout.NORTH);
		connection.add(portPanel, BorderLayout.SOUTH);
		connection.setBorder(
				BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.RED, 10), "CONNECTION"));

		connect.setBackground(Color.RED);
		connect.setPreferredSize(new Dimension(100, 20));
		connect.setMnemonic('C');
		connect.addActionListener(controller);
		connect.setActionCommand("connect");

		hostPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
		hostPanel.add(host);
		hostPanel.add(hostMessage);

		hostMessage.setText("localhost");
		hostMessage.setCaretPosition(0);
		hostMessage.requestFocus();
		hostMessage.requestFocus();
		hostMessage.setBorder(BorderFactory.createCompoundBorder(hostMessage.getBorder(),
				BorderFactory.createEmptyBorder(0, 5, 0, 0)));

		portPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
		portPanel.add(port);
		portPanel.add(comboBox);
		portPanel.add(connect);

		comboBox.setBackground(Color.WHITE);
		comboBox.setPreferredSize(new Dimension(100, 20));
		comboBox.setEditable(true);
		comboBox.addActionListener(controller);

		topPanel.setLayout(new BorderLayout());
		topPanel.add(connection, BorderLayout.CENTER);
		topPanel.add(messageF, BorderLayout.SOUTH);

		messageF.setBorder(
				BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK, 10), "MESSAGE"));
		messageF.setLayout(new FlowLayout(FlowLayout.LEADING));
		messageF.add(message);
		messageF.add(sendButton);

		message.setText("Type message");

		sendButton.setPreferredSize(new Dimension(81, 19));
		sendButton.setMnemonic('S');
		sendButton.setEnabled(false);
		sendButton.addActionListener(controller);
		sendButton.setActionCommand("send");

		displayBorder = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLUE, 10),
				" CHAT DISPLAY");
		displayBorder.setTitleJustification(TitledBorder.CENTER);

		cDisplay.setLayout(new BorderLayout());
		cDisplay.setBorder(displayBorder);
		cDisplay.add(scrollBar);

		mainPanel.setLayout(new BorderLayout());
		mainPanel.setBackground(Color.GREEN);
		mainPanel.add(topPanel, BorderLayout.NORTH);
		mainPanel.add(cDisplay, BorderLayout.CENTER);

		display.setEditable(false);
		display.setBackground(Color.WHITE);

		return mainPanel;

	}

	/**
	 * getter for display
	 * 
	 * @return display - chat display
	 */
	public JTextArea getDisplay() {
		return display;
	}

	/**
	 * tries to close all connections if the socket is not closed
	 */
	public void closeChat() {
		try {
			if (!socket.isClosed())
				connection.closeConnection();
			enableConnectButton();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * getter for instance of this class(ClientChatUI)
	 * 
	 * @return an instance of this class
	 */
	private ClientChatUI getInstance() {
		return this;
	}

	/**
	 * enables connect button when called upon
	 */
	private void enableConnectButton() {

		connect.setEnabled(true);
		connect.setBackground(Color.RED);
		sendButton.setEnabled(false);
		hostMessage.requestFocus();

	}

	/**
	 * on closing windows writes the CHAT_TERMINATOR constant and exits
	 * 
	 * @author Mayank Khera & Johnathan Gonzalez
	 * @version1
	 * @see default package
	 * @since
	 */
	class WindowController extends WindowAdapter {

		@Override
		public void windowClosing(WindowEvent event) {
			super.windowClosing(event);
			
			try {
				if(!socket.isClosed())
					outputStream.writeObject(ChatProtocolConstants.CHAT_TERMINATOR);
			}catch(IOException e) {
				e.printStackTrace();
			}finally {
				System.exit(0);
			}
		}

	}

	/**
	 * This class implements the actoinListners for connect and send button
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
			boolean connected = false;

			String ac = event.getActionCommand();

			if (ac.equals("connect")) {
				String host = hostMessage.getText();
				int port = 65535;

				try {
					port = Integer.parseInt((String) comboBox.getSelectedItem());
					
				} catch (NumberFormatException num) {
					display.append("Please select a non-empty port");
					return;
				}

				
				connected = connect(host, port);

				if (connected) {
					connect.setEnabled(false);
					connect.setBackground(Color.BLUE);
					sendButton.setEnabled(true);
					message.requestFocus();
					Runnable runnable = new ChatRunnable<ClientChatUI>(getInstance(), connection);
					Thread thread = new Thread(runnable);
					thread.start();
				}

			}
			

			if (ac.equals("send"))
				send();

		}

		/**
		 * tries to connect to the passed socket on the passed port
		 * 
		 * @param host
		 * @param port
		 * @return boolean true if connected
		 */
		private boolean connect(String host, int port) {

			socket = new Socket();
			try {
				
				socket.connect(new InetSocketAddress(InetAddress.getByName(host), port), 60000);
				socket.setSoTimeout(60000);

				if (socket.getSoLinger() != -1)
					socket.setSoLinger(true, 5);
				if (!socket.getTcpNoDelay())
					socket.setTcpNoDelay(true);
			

			
			display.append("Connected to Socket[addr=" + socket.getInetAddress() + ", port=" + socket.getPort()
					+ ", localport=" + socket.getLocalPort() + "]\n");

			connection = new ConnectionWrapper(socket);
			connection.createStreams();
			
			outputStream = connection.getOutputStream();

			return true;
			}catch(IOException e) {
				e.printStackTrace();
			}
			return false;
		}

		/**
		 * writes the message on the screen using outputStream
		 */
		private void send() {
			String sendMessage = message.getText();
			display.append(sendMessage + ChatProtocolConstants.LINE_TERMINATOR);
			try {
				outputStream.writeObject(
						ChatProtocolConstants.DISPLACEMENT + sendMessage + ChatProtocolConstants.LINE_TERMINATOR);
			} catch (IOException e) {
				enableConnectButton();
				display.append(e.getMessage());
			}
		}

	}
}
