package org.alexdev.icarus.messages.outgoing.room;

import org.alexdev.icarus.game.room.Room;
import org.alexdev.icarus.game.room.RoomData;
import org.alexdev.icarus.messages.headers.Outgoing;
import org.alexdev.icarus.messages.parsers.OutgoingMessageComposer;
import org.alexdev.icarus.server.api.messages.AbstractResponse;

public class RoomSettingsDataMessageComposer implements OutgoingMessageComposer {

	private Room room;

	public RoomSettingsDataMessageComposer(Room room) {
		this.room = room;
	}

	@Override
	public void write(AbstractResponse response) {
		
		RoomData data = this.room.getData();
		
		response.init(Outgoing.RoomEditSettingsComposer);
        response.writeInt(data.getId());
        response.writeString(data.getName());
        response.writeString(data.getDescription());
        response.writeInt(data.getState().getStateCode());
        response.writeInt(data.getCategory());
        response.writeInt(data.getUsersMax());
        response.writeInt(data.getUsersMax());
        response.writeInt(0);
        /*foreach (var s in room.RoomData.Tags)
        {
            response.appendString(s);
        }*/
        response.writeInt(data.getTradeState());
        response.appendInt32(data.isAllowPets());
        response.appendInt32(data.isAllowPetsEat());
        response.appendInt32(data.isAllowWalkthrough());
        response.appendInt32(data.isHideWall());
        response.writeInt(data.getWallThickness());
        response.writeInt(data.getFloorThickness());
        response.writeInt(data.getChatType());//room.RoomData.ChatType);
        response.writeInt(data.getChatBalloon());//room.RoomData.ChatBalloon);
        response.writeInt(data.getChatSpeed());//room.RoomData.ChatSpeed);
        response.writeInt(data.getChatMaxDistance());//room.RoomData.ChatMaxDistance);
        response.writeInt(data.getChatFloodProtection());//room.RoomData.ChatFloodProtection > 2 ? 2 : room.RoomData.ChatFloodProtection);
        response.writeBool(false); //allow_dyncats_checkbox
        response.writeInt(data.getWhoCanMute());//room.RoomData.WhoCanMute);
        response.writeInt(data.getWhoCanKick());//room.RoomData.WhoCanKick);
        response.writeInt(data.getWhoCanBan());//room.RoomData.WhoCanBan);
	}

}