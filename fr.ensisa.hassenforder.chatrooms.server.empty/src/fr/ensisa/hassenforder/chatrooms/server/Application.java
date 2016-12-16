package fr.ensisa.hassenforder.chatrooms.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.ensisa.hassenforder.chatrooms.server.model.Channel;
import fr.ensisa.hassenforder.chatrooms.server.model.ChannelType;
import fr.ensisa.hassenforder.chatrooms.server.model.Message;
import fr.ensisa.hassenforder.chatrooms.server.model.Model;
import fr.ensisa.hassenforder.chatrooms.server.model.User;


public class Application implements NetworkListener {

	private int count=0;
	private CommandServer commands = null;
	private MessagesServer messages = null;
	private MessagesPusher pusher = null;
	private Model model = null;
	
	public void start () {
		model = new Model ();
		model.getUsers().add(new User ("mimi"));
		model.getUsers().add(new User ("nini"));
		model.getUsers().add(new User ("titi"));
		model.getUsers().add(new User ("sisi"));
		commands = new CommandServer(this);
		commands.start();
		messages = new MessagesServer(this);
		messages.start();
		pusher = new MessagesPusher(this);
		pusher.start();
	}
	
	@Override
	public OperationStatus connectCommandUser(String name, CommandSession session) {
		User user = model.getUser(name);
		if (user == null) return OperationStatus.UNKNOWN_USER;
		if (user.isConnected()) return OperationStatus.ALLREADY_CONNECTED;
		user.setCommandSession(session);
		return OperationStatus.NOW_CONNECTED;
	}

	@Override
	public OperationStatus disconnectUser(String name) {
		User user = model.getUser(name);
		if (user == null) return OperationStatus.UNKNOWN_USER;
		if (! user.isConnected()) return OperationStatus.NOT_CONNECTED;
		user.setCommandSession(null);
		user.setMessagesSession(null);
		return OperationStatus.NOW_DISCONNECTED;
	}

	@Override
	public OperationStatus createChannel(String name, String channel, ChannelType type) {
		User user = model.getUser(name);
		if (user == null) return OperationStatus.UNKNOWN_USER;
		if (! user.isConnected()) return OperationStatus.NOT_CONNECTED;
		if (model.getChannel(channel) != null) return OperationStatus.CHANNEL_EXISTS;
		Channel c = null;
		switch (type)  {
		case FREE : c = new Channel(channel, type, null, user); break;
		case MODERATED: c = new Channel(channel, type, user, user); break;
		}
		if (c == null) return OperationStatus.CHANNEL_CREATION_FAILED;
		model.getChannels().add(c);
		model.getIncomingMessages().add(new Message(c, count++, "system", "welcome to channel "+c.getName()));
		if (type == ChannelType.MODERATED) {
			model.getPendingMessages().add(new Message(c, count++, "system", "welcome moderator to channel "+c.getName()));
		}
		return OperationStatus.CHANNEL_CREATED;
	}

	@Override
	public List<Channel> loadChannels(String name) {
		User user = model.getUser(name);
		if (user == null) return null;
		if (! user.isConnected()) return null;
		List<Channel> channels = model.getChannels();
		return channels;
	}

	@Override
	public OperationStatus ChangeChannelSubscription(String name, String channel, boolean subscription) {
		User user = model.getUser(name);
		if (user == null) return OperationStatus.UNKNOWN_USER;
		if (! user.isConnected()) return OperationStatus.NOT_CONNECTED;
		Channel c = model.getChannel(channel);
		if (c == null) return OperationStatus.UNKNOWN_CHANNEL;
		if (c.isSubscriptor(name)) {
			c.getSubscriptors().remove(user);
		} else {
			c.getSubscriptors().add(user);
		}
		return OperationStatus.SUBSCRIPTION_CHANGED;
	}

	@Override
	public OperationStatus SetApprobation(String name, int messageId, boolean approved) {
		User user = model.getUser(name);
		if (user == null) return OperationStatus.UNKNOWN_USER;
		if (! user.isConnected()) return OperationStatus.NOT_CONNECTED;
		Message message = model.getPendingMessage(messageId);
		if (message == null) return OperationStatus.UNKNOWN_MESSAGE;
		if (approved)
			model.getIncomingMessages().add(message);
		model.getPendingMessages().remove(message);
		return OperationStatus.APPROBATION_CHANGED;
	}

	@Override
	public OperationStatus sendMessage(String name, String channel, String text) {
		User user = model.getUser(name);
		if (user == null) return OperationStatus.UNKNOWN_USER;
		if (! user.isConnected()) return OperationStatus.NOT_CONNECTED;
		Channel c = model.getChannel(channel);
		if (c == null) return OperationStatus.UNKNOWN_CHANNEL;
		Message message = new Message(c, count++, name, text);
		switch (c.getType()) {
		case FREE :
			model.getIncomingMessages().add(message);
			break;
		case MODERATED :
			model.getPendingMessages().add(message);
			break;
		}
		return OperationStatus.MESSAGE_SENT;
	}

	@Override
	public OperationStatus connectMessagesUser(String name, MessagesSession session) {
		User user = model.getUser(name);
		if (user == null) return OperationStatus.UNKNOWN_USER;
		if (! user.isConnected()) return OperationStatus.NOT_CONNECTED;
		user.setMessagesSession(session);
		return OperationStatus.NOW_CONNECTED;
	}

	@Override
	public void processIncomingMessages() {
		Map<User, List<Message>> messages = new HashMap<User, List<Message>>();
		for (User u : model.getUsers()) {
			if (u.isConnected()) {
				messages.put(u, new ArrayList<Message>());
			}
		}
		for (Message m : model.getIncomingMessages()) {
			for (User u : m.getChannel().getSubscriptors()) {
				if (u.isConnected()) {
					messages.get(u).add(m);
				}
			}
		}
		for (Map.Entry<User, List<Message>> entry : messages.entrySet()) {
			entry.getKey().getMessages().dispatchIncomingMessages(entry.getValue());
		}
		model.getIncomingMessages().clear();
	}

	@Override
	public void processPendingMessages() {
		Map<User, List<Message>> messages = new HashMap<User, List<Message>>();
		for (Channel c : model.getChannels()) {
			if (c.getType() == ChannelType.MODERATED && c.getModerator().isConnected())
				messages.put(c.getModerator(), new ArrayList<Message>());					
		}
		for (Message m : model.getPendingMessages()) {
			User moderator = m.getChannel().getModerator();
			if (moderator.isConnected()) {
				messages.get(moderator).add(m);
			}
		}
		for (Map.Entry<User, List<Message>> entry : messages.entrySet()) {
			entry.getKey().getMessages().dispatchPendingMessages(entry.getValue());
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Application m = new Application ();
		m.start();
	}

}
