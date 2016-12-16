package fr.ensisa.hassenforder.chatrooms.server;

import java.util.List;

import fr.ensisa.hassenforder.chatrooms.server.model.Channel;
import fr.ensisa.hassenforder.chatrooms.server.model.ChannelType;

public interface NetworkListener {

	OperationStatus connectCommandUser(String name, CommandSession commandSession);

	OperationStatus disconnectUser(String name);

	OperationStatus createChannel(String name, String channel, ChannelType type);

	List<Channel> loadChannels(String name);

	OperationStatus ChangeChannelSubscription(String name, String channel, boolean subscription);

	OperationStatus SetApprobation(String name, int messageId, boolean approved);

	OperationStatus sendMessage(String name, String channel, String text);

	OperationStatus connectMessagesUser(String name, MessagesSession messagesSession);

	void processIncomingMessages();

	void processPendingMessages();

}
