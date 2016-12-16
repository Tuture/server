package fr.ensisa.hassenforder.chatrooms.server;

import java.io.InputStream;

import fr.ensisa.hassenforder.network.BasicAbstractReader;

public class MessagesReader extends BasicAbstractReader {

	private String userName;
	
	public MessagesReader(InputStream inputStream) {
		super (inputStream);
	}

	public void receive() {
		type = readInt ();
		switch (type) {
		}
	}

	public String getUserName() {
		return userName;
	}

}
