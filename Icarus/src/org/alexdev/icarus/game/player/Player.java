package org.alexdev.icarus.game.player;

import java.util.List;
import org.alexdev.icarus.game.entity.EntityType;
import org.alexdev.icarus.game.entity.Entity;
import org.alexdev.icarus.game.inventory.Inventory;
import org.alexdev.icarus.game.messenger.Messenger;
import org.alexdev.icarus.game.player.club.ClubSubscription;
import org.alexdev.icarus.game.plugins.PluginEvent;
import org.alexdev.icarus.game.plugins.PluginManager;
import org.alexdev.icarus.game.room.Room;
import org.alexdev.icarus.game.room.RoomManager;
import org.alexdev.icarus.game.room.enums.RoomAction;
import org.alexdev.icarus.game.room.user.RoomUser;
import org.alexdev.icarus.messages.MessageComposer;
import org.alexdev.icarus.messages.outgoing.room.user.HotelViewMessageComposer;
import org.alexdev.icarus.messages.outgoing.room.user.RoomForwardComposer;
import org.alexdev.icarus.messages.outgoing.user.BroadcastMessageAlertComposer;
import org.alexdev.icarus.server.api.IPlayerNetwork;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

public class Player extends Entity {
    
    private String machineId;
    private PlayerDetails details;
    private IPlayerNetwork network;
    private RoomUser roomUser;
    private Messenger messenger;
    private Inventory inventory;
    private ClubSubscription subscription;
    private boolean loggedIn;

    public Player(IPlayerNetwork network) {
        this.network = network;
        this.details = new PlayerDetails(this);
        this.roomUser = new RoomUser(this);
        this.messenger = new Messenger(this);
        this.inventory = new Inventory(this);
        this.subscription = new ClubSubscription(this);
    }

    /**
     * Perform room action.
     *
     * @param action the action
     * @param value the value
     */
    public void performRoomAction(RoomAction action, Object value) {

        switch (action) {
        case LEAVE_ROOM: {

            Room room = this.roomUser.getRoom();
            boolean goHotelView = (boolean)value;

            if (room == null) {
                return;
            }

            PluginManager.callEvent(PluginEvent.ROOM_LEAVE_EVENT, new LuaValue[] { 
                    CoerceJavaToLua.coerce(this), 
                    CoerceJavaToLua.coerce(this.roomUser.getRoom()) 
            });

            if (goHotelView) {
                this.send(new HotelViewMessageComposer());
            }

            room.getEntityManager().removeEntity(this);
            room.cleanup();
            
            this.messenger.sendStatus(false);
            
            break;

        }

        case FORWARD_ROOM: {
            int roomId = (int)value;
            this.send(new RoomForwardComposer(roomId));
            break;
        }
        }
    }

    /* (non-Javadoc)
     * @see org.alexdev.icarus.game.entity.Entity#dispose()
     */
    @Override
    public void dispose() {

        if (!this.details.isAuthenticated()) {
            return;   
        }

        if (this.roomUser.getRoom() != null) {
            this.performRoomAction(RoomAction.LEAVE_ROOM, false);
        }

        PluginManager.callEvent(PluginEvent.PLAYER_DISCONNECT_EVENT, new LuaValue[] { CoerceJavaToLua.coerce(this) });
        PlayerManager.removePlayer(this);
        
for (Room room : RoomManager.getPlayerRooms(this.details.getId())) {
            room.cleanup(); 
        }

    this.destroyObjects();
    }

    /**
     * Destroy objects.
     */
    private void destroyObjects() {
        this.network = null;
        this.details = null;
        this.roomUser = null;
        this.messenger = null;
        this.inventory = null;
        this.subscription = null;
    }

    /* (non-Javadoc)
     * @see org.alexdev.icarus.game.entity.Entity#getType()
     */
    @Override
    public EntityType getType() {
        return EntityType.PLAYER;
    }

    /**
     * Gets the rooms.
     *
     * @return the rooms
     */
    public List<Room> getRooms() {
        return RoomManager.getPlayerRooms(this.details.getId());
    }

    /**
     * Sets the machine id.
     *
     * @param machineId the new machine id
     */
    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    /**
     * Gets the machine id.
     *
     * @return the machine id
     */
    public String getMachineId() {
        return machineId;
    }

    /* (non-Javadoc)
     * @see org.alexdev.icarus.game.entity.Entity#getDetails()
     */
    public PlayerDetails getDetails() {
        return details;
    }

    /* (non-Javadoc)
     * @see org.alexdev.icarus.game.entity.Entity#getRoomUser()
     */
    public RoomUser getRoomUser() {
        return roomUser;
    }

    /**
     * Gets the messenger.
     *
     * @return the messenger
     */
    public Messenger getMessenger() {
        return messenger;
    }

    /**
     * Gets the inventory.
     *
     * @return the inventory
     */
    public Inventory getInventory() {
        return inventory;
    }

    /**
     * Gets the subscription.
     *
     * @return the subscription
     */
    public ClubSubscription getSubscription() {
        return subscription;
    }

    /**
     * Gets the network.
     *
     * @return the network
     */
    public IPlayerNetwork getNetwork() {
        return network;
    }

    /**
     * Send message.
     *
     * @param message the message
     */
    public void sendMessage(String message) {
        this.send(new BroadcastMessageAlertComposer(message));
    }

    /**
     * Send.
     *
     * @param response the response
     */
    public void send(MessageComposer response) {
        this.network.send(response);
    }

    /**
     * Checks if is logged in.
     *
     * @return true, if is logged in
     */
    public boolean isLoggedIn() {
        return loggedIn;
    }

    /**
     * Sets the logged in.
     *
     * @param loggedIn the new logged in
     */
    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }
}
