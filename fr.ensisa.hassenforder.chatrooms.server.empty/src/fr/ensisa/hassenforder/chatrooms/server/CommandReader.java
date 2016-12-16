package fr.ensisa.hassenforder.chatrooms.server;


import java.io.InputStream;

import fr.ensisa.hassenforder.network.BasicAbstractReader;
import fr.ensisa.hassenforder.network.Protocol;

public class CommandReader extends BasicAbstractReader {

	private String name;
	
	public CommandReader(InputStream inputStream) {
		super (inputStream);
	}

	public void receive() {
		type = readInt ();
		switch (type) {
		case Protocol.RQ_CONNECT :
			name = readString();
			break;
		}
	}

	public String getName() {
		return name;
	}

}
