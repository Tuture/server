package fr.ensisa.hassenforder.chatrooms.server;

import java.io.OutputStream;
import java.util.List;

import fr.ensisa.hassenforder.chatrooms.server.model.Channel;
import fr.ensisa.hassenforder.chatrooms.server.model.ChannelType;
import fr.ensisa.hassenforder.network.BasicAbstractWriter;
import fr.ensisa.hassenforder.network.Protocol;

public class CommandWriter extends BasicAbstractWriter {

	public CommandWriter(OutputStream outputStream) {
		super (outputStream);
	}

	public void createOK() {
		writeInt(Protocol.RP_OK);
	}
	
	public void createKO() {
		writeInt(Protocol.RP_KO);
	}

	public void createLoadChannels(List<Channel> channels, String name) {
		writeInt(Protocol.RP_CHANNELS);
		// Envoyer tout les channels
		writeChannels(channels, name);
	}

	private void writeChannels(List<Channel> channels, String name) {
		// Envoyé le nombre de channels
		writeInt(channels.size());
		for(Channel c : channels)
			writeChannel(c, name);
	}

	private void writeChannel(Channel channel, String name) {
		// Envoyé le channel
		// le nom du channel
		writeString(channel.getName());
		// Le type du channel
		if(channel.getType().equals(ChannelType.FREE)) writeInt(Protocol.FREE);
		else {
			writeInt(Protocol.MODERATED);
			// Le nom du modérateur
			writeString(channel.getModerator().getName());
		}
		// La souscription
		writeBoolean(channel.isSubscriptor(name));
	}

}
