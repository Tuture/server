package fr.ensisa.hassenforder.chatrooms.server.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Hassenforder
 */
public class Model {

    private List<User> users;
    private List<Channel> channels;
    private List<Message> pendingMessages;
    private List<Message> incomingMessages;

    public List<User> getUsers() {
        if (users == null) users = new ArrayList<>();
		return users;
	}

	public List<Channel> getChannels() {
        if (channels == null) channels = new ArrayList<>();
        return channels;
    }

    public List<Message> getPendingMessages() {
        if (pendingMessages == null) pendingMessages = new ArrayList<>();
        return pendingMessages;
    }

    public List<Message> getIncomingMessages() {
        if (incomingMessages == null) incomingMessages = new ArrayList<>();
        return incomingMessages;
    }

    public void setPendingMessages(List<Message> pendingMessages) {
        this.pendingMessages = pendingMessages;
    }

    public User getUser (String name) {
    	for (User u : getUsers()) {
    		if (name.equals(u.getName()))
    			return u;
    	}
    	return null;
    }

	public Channel getChannel(String channel) {
		for (Channel c : getChannels())
			if (channel.equals(c.getName()))
				return c;
		return null;
	}

	public Message getPendingMessage(int messageId) {
		for (Message m : getPendingMessages()) {
			if (messageId == m.getId())
				return m;
		}
		return null;
	}

	
}
