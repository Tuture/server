package fr.ensisa.hassenforder.chatrooms.server;

import java.net.ServerSocket;
import java.net.Socket;

import fr.ensisa.hassenforder.network.Protocol;


public class MessagesServer extends Thread {

	private ServerSocket server = null;
	private NetworkListener listener = null;

	public MessagesServer(NetworkListener listener) {
		super();
		this.listener = listener;
	}

	public void run () {
		try {
			server = new ServerSocket (Protocol.CHATROOMS_MSG_PORT_ID);
			while (true) {
				Socket connection = server.accept();
				MessagesSession session = new MessagesSession (connection, listener);
				session.processConnection();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
