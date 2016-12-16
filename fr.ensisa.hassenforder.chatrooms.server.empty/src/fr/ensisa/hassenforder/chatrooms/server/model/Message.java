package fr.ensisa.hassenforder.chatrooms.server.model;

/**
 *
 * @author Hassenforder
 */
public class Message {
    final private Channel channel;
    final private int id;
    final private String author;
    final private String text;
    
    public Message(Channel channel, int id, String author, String text) {
        this.channel = channel;
        this.id = id;
        this.author = author;
        this.text = text;
    }

    public Channel getChannel() {
        return channel;
    }

    public int getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return "Message{" + "channel=" + channel + ", id=" + id + ", author=" + author + ", text=" + text + '}';
    }
    
}
