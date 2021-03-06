package org.alexdev.icarus.messages.incoming.room.user;

import org.alexdev.icarus.game.player.Player;
import org.alexdev.icarus.game.room.Room;
import org.alexdev.icarus.game.room.user.ChatType;
import org.alexdev.icarus.game.room.user.RoomUser;
import org.alexdev.icarus.messages.MessageEvent;
import org.alexdev.icarus.server.api.messages.ClientMessage;

public class ChatMessageEvent implements MessageEvent {

    @Override
    public void handle(Player player, ClientMessage request) {

        RoomUser roomUser = player.getRoomUser();

        if (roomUser == null) {
            return;
        }

        Room room = roomUser.getRoom();

        if (room == null) {
            return;
        }
        
        String message = request.readString();
        int bubble = request.readInt();
        
        roomUser.setChatColor(bubble);
        roomUser.chat(message, ChatType.CHAT, true);
    }
}
