package fr.ensisa.hassenforder.chatrooms.server;

import java.net.ServerSocket;
import java.net.Socket;

import fr.ensisa.hassenforder.network.Protocol;


public class CommandServer extends Thread {

	private ServerSocket server = null;
	private NetworkListener listener = null;

	public CommandServer(NetworkListener listener) {
		super();
		this.listener = listener;
	}

	public void run () {
		try {
			server = new ServerSocket (Protocol.CHATROOMS_CMD_PORT_ID);
			while (true) {
				Socket connection = server.accept();
				CommandSession session = new CommandSession (connection, listener);
				session.start ();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
