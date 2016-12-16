package fr.ensisa.hassenforder.chatrooms.server.model;

import fr.ensisa.hassenforder.chatrooms.server.CommandSession;
import fr.ensisa.hassenforder.chatrooms.server.MessagesSession;

public class User {
	private String name;
	private CommandSession command;
	private MessagesSession messages;

	public User(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public boolean isConnected() {
		return command != null;
	}

	public CommandSession getCommand() {
		return command;
	}

	public MessagesSession getMessages() {
		return messages;
	}

	public void setCommandSession (CommandSession session) {
		if (command != null) command.close();
		this.command = session;
	}

	public void setMessagesSession (MessagesSession session) {
		if (messages != null) messages.close();
		this.messages = session;
	}
}
