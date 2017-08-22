package org.alexdev.icarus.messages.incoming.room.user;

import org.alexdev.icarus.dao.mysql.RoomDao;
import org.alexdev.icarus.game.item.Item;
import org.alexdev.icarus.game.player.Player;
import org.alexdev.icarus.game.room.Room;
import org.alexdev.icarus.messages.MessageEvent;
import org.alexdev.icarus.server.api.messages.ClientMessage;

public class DeleteRoomMessageEvent implements MessageEvent {

    @Override
    public void handle(Player player, ClientMessage request) {
        
        Room room = player.getRoomUser().getRoom();

        if (room == null) {
            return;
        }
        
        if (!room.hasRights(player, true)) {
            return;
        }
        
        request.readInt(); // room id
        
        for (Player users : room.getPlayers()) {
            room.leaveRoom(users, true);
        }
        
        for (Item item : room.getItems().values()) {
        	
        	item.setRoomId(0);
        	player.getInventory().addItem(item);
        	item.save();
        }
        
        RoomDao.deleteRoom(room);
        room.dispose(true);
    }

}
