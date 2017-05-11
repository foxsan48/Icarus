package org.alexdev.icarus.messages.incoming.navigator;

import org.alexdev.icarus.dao.mysql.RoomDao;
import org.alexdev.icarus.game.player.Player;
import org.alexdev.icarus.game.room.Room;
import org.alexdev.icarus.messages.MessageEvent;
import org.alexdev.icarus.messages.outgoing.navigator.CreateRoomMessageComposer;
import org.alexdev.icarus.server.api.messages.AbstractReader;

public class CreateRoomMessageEvent implements MessageEvent {

	@Override
	public void handle(Player player, AbstractReader request) {
		
		String name = request.readString();
        String description = request.readString();
        String model = request.readString();
        int category = request.readInt();
        int usersMax = request.readInt();
        int tradeState = request.readInt();
        
        if (name == null || description == null || model == null) {
        	return;
        }
        
        Room room = RoomDao.createRoom(player, name, description, model, category, usersMax, tradeState);
        
        player.send(new CreateRoomMessageComposer(room.getData().getId(), room.getData().getName()));

	}

}