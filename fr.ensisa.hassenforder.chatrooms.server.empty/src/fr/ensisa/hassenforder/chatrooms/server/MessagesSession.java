package fr.ensisa.hassenforder.chatrooms.server;

import java.io.IOException;
import java.net.Socket;
import java.util.List;

import fr.ensisa.hassenforder.chatrooms.server.model.Message;
import fr.ensisa.hassenforder.network.Protocol;

public class MessagesSession {

	private Socket connection;
	private NetworkListener listener;
	
	public MessagesSession(Socket connection, NetworkListener listener) {
		this.connection = connection;
		this.listener = listener;
		if( listener == null) throw new RuntimeException("listener cannot be null");
	}

	public void close () {
		try {
			if (connection != null)
				connection.close();
		} catch (IOException e) {
		}
		connection = null;
	}

	public boolean processConnection () {
		try {
			MessagesReader reader = new MessagesReader(connection.getInputStream());
			reader.receive();
			switch (reader.getType()) {
			case Protocol.RQ_CONNECT:
				//System.out.println("trying to connect a User : ");
				OperationStatus os = listener.connectMessagesUser(reader.getUserName(), this);
				if (os == OperationStatus.NOW_CONNECTED) {
					return true;
				}
			default: return false;
			}
		}
		catch (Exception e) {
		}
		return false;
	}

	public boolean dispatchIncomingMessages (List<Message> messages) {
		try {
			MessagesWriter writer = new MessagesWriter (connection.getOutputStream());
			writer.dispatchIncomingMessages(messages);
			return true;
		}
		catch (Exception e) {
		}
		return false;
	}

	public boolean dispatchPendingMessages (List<Message> messages) {
		try {
			MessagesWriter writer = new MessagesWriter (connection.getOutputStream());
			writer.dispatchPendingMessages(messages);
			return true;
		}
		catch (Exception e) {
		}
		return false;
	}

}
