package fr.ensisa.hassenforder.chatrooms.server;

public class MessagesPusher extends Thread {

	private NetworkListener listener;
	
	public MessagesPusher(NetworkListener listener) {
		this.listener = listener;
		if( listener == null) throw new RuntimeException("listener cannot be null");
	}

	public void run() {
		try {
			int count = 0;
			while (true) {
				if ((count % 2) == 0) listener.processIncomingMessages();
				else listener.processPendingMessages();
				Thread.sleep(5000);
				++count;
			}
		} catch (Exception e) {
		}
	}

}
