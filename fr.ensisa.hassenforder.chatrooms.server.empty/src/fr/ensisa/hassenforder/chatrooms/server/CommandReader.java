package fr.ensisa.hassenforder.chatrooms.server;


import java.io.InputStream;

import fr.ensisa.hassenforder.chatrooms.server.model.ChannelType;
import fr.ensisa.hassenforder.network.BasicAbstractReader;
import fr.ensisa.hassenforder.network.Protocol;

public class CommandReader extends BasicAbstractReader {

	private String name, channelName;
	private int channelType;
	
	public CommandReader(InputStream inputStream) {
		super (inputStream);
	}

	public void receive() {
		type = readInt ();
		switch (type) {
		case Protocol.RQ_CONNECT :
			name = readString();
			break;
		case Protocol.RQ_CHANNEL :
			this.channelName = readString();
			this.channelType = readInt();
		}
	}

	public String getName() {
		return name;
	}

	public String getChannelName() {
		return channelName;
	}

	public ChannelType getChannelType() {
		if(channelType == 0) return ChannelType.FREE;
		return ChannelType.MODERATED;
	}

}
