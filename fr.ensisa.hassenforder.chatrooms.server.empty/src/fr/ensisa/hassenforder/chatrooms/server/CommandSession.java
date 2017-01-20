package fr.ensisa.hassenforder.chatrooms.server;

import java.io.IOException;
import java.net.Socket;
import java.util.List;

import fr.ensisa.hassenforder.chatrooms.server.model.Channel;
import fr.ensisa.hassenforder.chatrooms.server.model.ChannelType;
import fr.ensisa.hassenforder.network.Protocol;

public class CommandSession extends Thread {

	private Socket connection;
	private NetworkListener listener;

	public CommandSession(Socket connection, NetworkListener listener) {
		this.connection = connection;
		this.listener = listener;
		if (listener == null)
			throw new RuntimeException("listener cannot be null");
	}

	public void close() {
		this.interrupt();
		try {
			if (connection != null)
				connection.close();
		} catch (IOException e) {
		}
		connection = null;
	}

	public boolean operate() {
		try {
			CommandWriter writer = new CommandWriter(connection.getOutputStream());
			CommandReader reader = new CommandReader(connection.getInputStream());
			reader.receive();
			switch (reader.getType()) {
			case Protocol.RQ_CONNECT:
				System.out.println("trying to connect a User : ");
				OperationStatus os = listener.connectCommandUser(reader.getName(), this);
				if (os == OperationStatus.NOW_CONNECTED) {
					writer.createOK();
				}
				else {
					writer.createKO();
					//listener.disconnectUser(reader.getName());
				}
				break;
			case Protocol.RQ_CHANNEL:
				System.out.println("trying to create a channel : ");
				String name2 = reader.getName();
				String channelName = reader.getChannelName();
				ChannelType cT = reader.getChannelType();
				System.out.println("Name : " + name2 + " channelName : " + channelName + " type : " + cT);
				OperationStatus osCreateChannel = listener.createChannel(name2, channelName, cT);
				if (osCreateChannel == OperationStatus.CHANNEL_CREATED) {
					writer.createOK();
					System.out.println("channel created OK");
				} else {
					writer.createKO();
					System.out.println("Channel created KO");
				}
			break;
			case Protocol.RQ_DISCONNECT:
				System.out.println("trying to disconnect a User : ");
				OperationStatus osDisconnect = listener.disconnectUser(reader.getName());
				if (osDisconnect == OperationStatus.NOW_DISCONNECTED) {
					writer.createOK();
					System.out.println("disconnection OK");
				} else {
					writer.createKO();
					System.out.println("disconnection KO");
				}
				break;
			case Protocol.RQ_LOAD:
				System.out.println("trying to load channels : ");
				String name = reader.getName();
				List<Channel> channels = listener.loadChannels(name);
				if(channels!=null){
					writer.createOK();
					writer.createLoadChannels(channels, name);
					writer.send();
				    return true;
			    }
			    writer.createKO();
			    break;
			case Protocol.RQ_UNSUSCRIBE:
				System.out.println("trying to change subscription : ");
				OperationStatus osUnsuscribe = listener.ChangeChannelSubscription(reader.getName(), reader.getChannelName(), reader.getSubscription());
				if (osUnsuscribe == OperationStatus.SUBSCRIPTION_CHANGED) {
					writer.createOK();
					System.out.println("suscribption changed OK");
				} else {
					writer.createKO();
					System.out.println("suscribption changed KO");
				}
				return true;
			case Protocol.RQ_APPROBATION:
				System.out.println("trying to approve a message : ");
				OperationStatus osApprobation = listener.SetApprobation(reader.getName(), reader.getMessageID(), reader.getApprobation());
				if (osApprobation == OperationStatus.APPROBATION_CHANGED) {
					writer.createOK();
					System.out.println("approbation changed OK");
				} else {
					writer.createKO();
					System.out.println("approbation changed KO");
				}
				break;
			case Protocol.RQ_SEND_MESSAGE:
				System.out.println("trying to send a message : ");
				OperationStatus osSendMessage = listener.sendMessage(reader.getName(), reader.getChannelName(), reader.getMessageText());
				if (osSendMessage == OperationStatus.MESSAGE_SENT) {
					writer.createOK();
					System.out.println("sending message OK");
				} else {
					writer.createKO();
					System.out.println("sending message KO");
				}
				break;
			case 0:
				return false; // socket closed
			case -1:
				break;
			default:
				return false; // connection jammed
			}
			writer.send();
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	public void run() {
		while (true) {
			if (!operate())
				break;
		}
		try {
			if (connection != null)
				connection.close();
		} catch (IOException e) {
		}
	}

}
