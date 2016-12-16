package fr.ensisa.hassenforder.chatrooms.server.model;

/**
 *
 * @author Hassenforder
 */
public interface ModelListener {
    
    public void updateStatus (String status);
    
    public void updateConnection (Model model, String status);
    
    public void updateChannelCreated (Model model, String status);

    public void updateChannelList (Model model, String status);

    public void updateSubscribtionChange (Model model, String status);

    public void updateModerationList (Model model, String status);

    public void updateModerationState(Model model, String subscribtion_changed);

    public void updateMessageList (Model model, String status);

}
