package fr.ensisa.hassenforder.chatrooms.server;


import java.io.InputStream;

import fr.ensisa.hassenforder.chatrooms.server.model.ChannelType;
import fr.ensisa.hassenforder.network.BasicAbstractReader;
import fr.ensisa.hassenforder.network.Protocol;

public class CommandReader extends BasicAbstractReader {

	private String name, channelName, messageText;
	private int channelType, messageID;
	private boolean subscription, approbation;
	
	public CommandReader(InputStream inputStream) {
		super (inputStream);
	}

	public void receive() {
		type = readInt ();
		System.out.println("CommandReader : receive type " + type);
		switch (type) {
		case Protocol.RQ_CONNECT :
			name = readString();
			boolean connectBoolean = readBoolean();
			break;
		case Protocol.RQ_DISCONNECT :
			this.name = readString();
			boolean disconnectBoolean = readBoolean();
			break;
		case Protocol.RQ_CHANNEL :
			this.name = readString();
			this.channelName = readString();
			this.channelType = readInt();
			break;
		case Protocol.RQ_UNSUSCRIBE :
			this.name = readString();
			this.channelName = readString();
			this.subscription = readBoolean();
			break;
		case Protocol.RQ_APPROBATION :
			this.name = readString();
			this.messageID = readInt();
			this.approbation = readBoolean();
			break;
		case Protocol.RQ_SEND_MESSAGE :
			this.name = readString();
			this.channelName = readString();
			this.messageText = readString();
			break;
		case Protocol.RQ_LOAD :
			this.name = readString();
			break;
		}
	}

	public String getName() {
		return name;
	}

	public String getChannelName() {
		return channelName;
	}

	public ChannelType getChannelType() {
		System.out.println("Channel Type in commande reader server : " + channelType);
		if(channelType == Protocol.FREE) return ChannelType.FREE;
		return ChannelType.MODERATED;
	}
	
	public boolean getSubscription() {
		return subscription;
	}

	public int getMessageID() {
		return messageID;
	}

	public boolean getApprobation() {
		return approbation;
	}
	
	public String getMessageText() {
		return this.messageText;
	}
}
