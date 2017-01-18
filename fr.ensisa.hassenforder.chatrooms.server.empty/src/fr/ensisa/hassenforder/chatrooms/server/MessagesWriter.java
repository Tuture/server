package fr.ensisa.hassenforder.chatrooms.server;

import java.io.OutputStream;
import java.util.List;

import fr.ensisa.hassenforder.chatrooms.server.model.Message;
import fr.ensisa.hassenforder.network.BasicAbstractWriter;
import fr.ensisa.hassenforder.network.Protocol;

public class MessagesWriter extends BasicAbstractWriter {

	public MessagesWriter(OutputStream outputStream) {
		super (outputStream);
	}
	
	public void dispatchIncomingMessages(List<Message> messages){
		writeInt(Protocol.RP_INCOMMING_MESSAGES);
		writeInt(messages.size());
		for (Message message : messages) {
			writeString(message.getChannel().getName());  
			writeInt(message.getId());
			writeString(message.getAuthor());
			writeString(message.getText());
		}
		this.send();
	}
	public void dispatchPendingMessages(List<Message> messages) {
		writeInt(Protocol.RP_PENDING_MESSAGES);
		writeInt(messages.size());
		for (Message message : messages) {
			writeString(message.getChannel().getName());  
			writeInt(message.getId());
			writeString(message.getAuthor());
			writeString(message.getText());
		}
		this.send();
	}

}
