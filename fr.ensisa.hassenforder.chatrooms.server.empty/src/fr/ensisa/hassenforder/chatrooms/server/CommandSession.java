package fr.ensisa.hassenforder.chatrooms.server;

import java.io.IOException;
import java.net.Socket;

import fr.ensisa.hassenforder.network.Protocol;

public class CommandSession extends Thread {

	private Socket connection;
	private NetworkListener listener;

	public CommandSession(Socket connection, NetworkListener listener) {
		this.connection = connection;
		this.listener = listener;
		if (listener == null)
			throw new RuntimeException("listener cannot be null");
	}

	public void close() {
		this.interrupt();
		try {
			if (connection != null)
				connection.close();
		} catch (IOException e) {
		}
		connection = null;
	}

	public boolean operate() {
		try {
			CommandWriter writer = new CommandWriter(connection.getOutputStream());
			CommandReader reader = new CommandReader(connection.getInputStream());
			reader.receive();
			switch (reader.getType()) {
			case Protocol.RQ_CONNECT:
				OperationStatus os = listener.connectCommandUser(reader.getName(), this);
				if (os == OperationStatus.NOW_CONNECTED)
					writer.createOK();
				else
					writer.createKO();
				break;
			case Protocol.RQ_CHANNEL:
				System.out.println("trying to create a channel : ");
				OperationStatus osCreateChannel = listener.createChannel(reader.getName(),reader.getChannelName(), reader.getChannelType());
				if (osCreateChannel == OperationStatus.CHANNEL_CREATED) {
					writer.createOK(); System.out.println("channel created OK");}
				else {
					writer.createKO(); System.out.println("Channl created KO");}
				break;
			case 0:
				return false; // socket closed
			case -1:
				break;
			default:
				return false; // connection jammed
			}
			writer.send();
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	public void run() {
		while (true) {
			if (!operate())
				break;
		}
		try {
			if (connection != null)
				connection.close();
		} catch (IOException e) {
		}
	}

}
