package org.alexdev.icarus.game.room;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.alexdev.icarus.dao.mysql.room.RoomDao;
import org.alexdev.icarus.dao.mysql.room.RoomModelDao;
import org.alexdev.icarus.game.groups.Group;
import org.alexdev.icarus.game.groups.GroupManager;
import org.alexdev.icarus.game.player.Player;
import org.alexdev.icarus.game.player.PlayerDetails;
import org.alexdev.icarus.game.player.PlayerManager;
import org.alexdev.icarus.game.room.enums.RoomType;
import org.alexdev.icarus.game.room.managers.RoomEntityManager;
import org.alexdev.icarus.game.room.managers.RoomItemManager;
import org.alexdev.icarus.game.room.model.RoomMapping;
import org.alexdev.icarus.game.room.model.RoomModel;
import org.alexdev.icarus.game.room.scheduler.RoomScheduler;
import org.alexdev.icarus.game.room.tasks.CarryItemTask;
import org.alexdev.icarus.game.room.tasks.PetTask;
import org.alexdev.icarus.game.room.tasks.RollerTask;
import org.alexdev.icarus.messages.MessageComposer;

public class Room {

    private AtomicInteger virtualTicketCounter = new AtomicInteger(-1);
    private RoomData data;
    private RoomModel model;
    private RoomScheduler scheduler;
    private RoomMapping mapping;
    private RoomPromotion promotion;
    private RoomItemManager itemManager;
    private RoomEntityManager entityManager;
    private Group group;
    private List<Integer> rights;

    public Room(RoomData data) {
        this.data = data;
        this.mapping = new RoomMapping(this);
        this.scheduler = new RoomScheduler(this);
        this.itemManager = new RoomItemManager(this);
        this.entityManager = new RoomEntityManager(this);
        this.rights = RoomDao.getRoomRights(this.data.getId());
    }

    /**
     * Register the tasks required for room functionality
     */
    public void scheduleEvents() {
        this.scheduler.addScheduleEvent(1, TimeUnit.SECONDS, new CarryItemTask(this));
        this.scheduler.addScheduleEvent(4, TimeUnit.SECONDS, new RollerTask(this));
        this.scheduler.addScheduleEvent(5, TimeUnit.SECONDS, new PetTask(this));
    }

    /**
     * Checks for rights.
     *
     * @param userId the user id
     * @param ownerCheckOnly the owner check only
     * @return true, if successful
     */
    public boolean hasRights(int userId, boolean ownerCheckOnly) {

        PlayerDetails details = PlayerManager.getPlayerData(userId);

        if (details != null) {

            if (details.hasPermission("room_all_rights")) {
                return true;
            }
        }

        if (this.data.getOwnerId() == userId) {

            return true;
        } else {

            if (!ownerCheckOnly) {
                return this.rights.contains(Integer.valueOf(userId));
            }
        }

        return false;
    }

    /**
     * Send with rights.
     *
     * @param response the response
     */
    public void sendWithRights(MessageComposer response) {

        for (Player player : this.getEntityManager().getPlayers()) {

            if (this.hasRights(player.getDetails().getId(), false)) {
                player.send(response);
            }
        }
    }

    /**
     * Send.
     *
     * @param response the response
     */
    public void send(MessageComposer response) {

        for (Player player : this.getEntityManager().getPlayers()) {
            player.send(response);
        }
    }

    /**
     * Gets the model.
     *
     * @return the model
     */
    public RoomModel getModel() {

        if (this.data.getModel().startsWith("dynamic_model")) {

            if (this.model == null) {
                this.model = RoomModelDao.getCustomModel(this.data.getId());
            }

            return model;
        }

        return RoomModelDao.getModel(this.data.getModel());
    }

    /**
     * Save.
     */
    public void save() {
        RoomDao.updateRoom(this);
    }

    /**
     * Sets the model.
     *
     * @param model the new model
     */
    public void setModel(RoomModel model) {
        this.model = model;
    }

    /**
     * Creates the promotion.
     *
     * @param promotionName the promotion name
     * @param promotionDescription the promotion description
     */
    public void createPromotion(String promotionName, String promotionDescription) {
        this.promotion = new RoomPromotion(this, promotionName, promotionDescription);
        RoomManager.addPromotedRoom(this.data.getId(), this);
    }

    /**
     * End promotion.
     */
    public void endPromotion() {
        this.promotion = null;
        RoomManager.removePromotedRoom(this.data.getId());
    }

    /**
     * Checks for promotion.
     *
     * @return true, if successful
     */
    public boolean hasPromotion() {
        return this.promotion != null;
    }

    /**
     * Gets the group, if the room has no users, it will only load
     * group information straight from the database.
     *
     * @return the group
     */
    public Group getGroup() {
        
        if (this.group == null) {
            if (this.data.getGroupId() > 0) {
                return GroupManager.getGroup(this.data.getGroupId());
            } else {
                return null;
            }
        }

        return this.group;
    }

    /**
     * Sets the group.
     *
     * @param group the new group
     */
    public void setGroup(Group group) {
        this.group = group;
    }

    /**
     * Load the full group information, called when
     * the first player joins a room.
     * 
     * Calls GroupManager.loadGroup()
     */
    public void loadGroup() {
         this.group = GroupManager.loadGroup(this.data.getGroupId());
    }


    /**
     * Load the full group information, called when
     * the first player joins a room.
     * 
     * Calls GroupManager.unloadGroup()
     */
    public void unloadGroup() {
        if (this.group != null) {
            this.group = null;
            GroupManager.unloadGroup(this.data.getGroupId());
        }
    }

    /**
     * Will try to cleanup the room if the following variables are met.
     * 
     * The first being that there's no players in the room.
     * The second being that this is a private room.
     * 
     * The third being that the owner of the room 
     * is offline (if this parameter is met then the room will be unloaded).
     */
    public void cleanup() {

        if (this.entityManager.getPlayers().size() > 0) {
            return;
        }

        if (this.scheduler != null) {
            this.scheduler.cancelTasks();
        }

        this.entityManager.cleanupNonPlayableEntities();
        this.itemManager.getItems().clear();
        this.virtualTicketCounter.set(-1);
        this.unloadGroup();

        if (this.data.getRoomType() != RoomType.PRIVATE) {
            return;
        }

        if (PlayerManager.hasPlayer(this.data.getOwnerId())) {
            return;
        }

        RoomManager.removeRoom(this.data.getId());
        this.destroyObjects();
    }

    private void destroyObjects() {
        this.entityManager = null;
        this.scheduler = null;
        this.itemManager = null;
        this.virtualTicketCounter = null;
        this.promotion = null;
        this.data = null;
        this.mapping = null;
        this.model = null;
    }

    /**
     * Gets the data.
     *
     * @return the data
     */
    public RoomData getData() {
        return data;
    }

    /**
     * Gets the mapping.
     *
     * @return the mapping
     */
    public RoomMapping getMapping() {
        return mapping;
    }

    /**
     * Gets the promotion.
     *
     * @return the promotion
     */
    public RoomPromotion getPromotion() {
        return promotion;
    }

    /**
     * Gets the rights.
     *
     * @return the rights
     */
    public List<Integer> getRights() {
        return rights;
    }

    /**
     * Gets the entity manager.
     *
     * @return the entity manager
     */
    public RoomEntityManager getEntityManager() {
        return entityManager;
    }

    /**
     * Gets the item manager.
     *
     * @return the item manager
     */
    public RoomItemManager getItemManager() {
        return itemManager;
    }

    /**
     * Gets the virtual ticket counter.
     *
     * @return the virtual ticket counter
     */
    public AtomicInteger getVirtualTicketCounter() {
        return virtualTicketCounter;
    }

    /**
     * Gets the scheduler.
     *
     * @return the scheduler
     */
    public RoomScheduler getScheduler() {
        return scheduler;
    }
}