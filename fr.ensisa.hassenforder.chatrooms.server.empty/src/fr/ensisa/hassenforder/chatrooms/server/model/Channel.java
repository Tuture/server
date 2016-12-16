package fr.ensisa.hassenforder.chatrooms.server.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Hassenforder
 */
public class Channel {
    final private String name;
    final private ChannelType type;
    final private User moderator;
    private List<User> subscriptors;
    
	public Channel(String name, ChannelType type, User moderator, User creator) {
		super();
		this.name = name;
		this.type = type;
		this.moderator = moderator;
		getSubscriptors().add(creator);
	}
	
	public String getName() {
		return name;
	}
	
	public ChannelType getType() {
		return type;
	}
	
	public User getModerator() {
		return moderator;
	}

	public List<User> getSubscriptors() {
		if (subscriptors == null) subscriptors = new ArrayList<User>();
		return subscriptors;
	}

	public boolean isSubscriptor(String name) {
		for (User u : getSubscriptors()) {
			if (name.equals(u.getName()))
				return true;
		}
		return false;
	}

}
