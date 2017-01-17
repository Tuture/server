package fr.ensisa.hassenforder.chatrooms.server;

import java.io.OutputStream;
import java.util.List;

import fr.ensisa.hassenforder.chatrooms.server.model.Channel;
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

	public void createLoadChannels(List<Channel> channels) {
		writeInt(Protocol.RP_CHANNELS);
		// Envoyer tout les channels
		writeChannels(channels);
	}

	private void writeChannels(List<Channel> channels) {
		// Envoyé le nombre de channels
		writeInt(channels.size());
		for(Channel c : channels)
			writeChannel(c);
	}

	private void writeChannel(Channel channel) {
		// Envoyé le channel
		// le nom du channel
		writeString(channel.getName());
		// Le type du channel
		writeInt(channel.getType().ordinal());
		// Le nom du modérateur
		if(channel.getModerator()!= null)
			writeString(channel.getModerator().getName());
		// La souscription
		writeBoolean(channel.isSubscriptor(channel.getName()));
	}

}
