package fr.ensisa.hassenforder.chatrooms.server;

import java.io.OutputStream;

import fr.ensisa.hassenforder.network.BasicAbstractWriter;

public class MessagesWriter extends BasicAbstractWriter {

	public MessagesWriter(OutputStream outputStream) {
		super (outputStream);
	}

}
