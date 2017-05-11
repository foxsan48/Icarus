package org.alexdev.icarus.messages.outgoing.room.user;

import java.util.Arrays;
import java.util.List;

import org.alexdev.icarus.game.entity.EntityType;
import org.alexdev.icarus.game.entity.IEntity;
import org.alexdev.icarus.messages.headers.Outgoing;
import org.alexdev.icarus.messages.parsers.OutgoingMessageComposer;
import org.alexdev.icarus.server.api.messages.AbstractResponse;

public class UserDisplayMessageComposer implements OutgoingMessageComposer {

	private List<IEntity> entities;

	public UserDisplayMessageComposer(IEntity entity) {
		this(Arrays.asList(new IEntity[] { entity }));
	}

	public UserDisplayMessageComposer(List<IEntity> entities) {
		this.entities = entities;
	}

	@Override
	public void write(AbstractResponse response) {
		
		response.init(Outgoing.UserDisplayMessageComposer);
		synchronized (this.entities) {

			response.writeInt(this.entities.size());
			for (IEntity entity : this.entities) {

				if (entity.getType() == EntityType.PLAYER) {


					response.writeInt(entity.getDetails().getId());
					response.writeString(entity.getDetails().getUsername());
					response.writeString(entity.getDetails().getMotto());
					response.writeString(entity.getDetails().getFigure());
					response.writeInt(entity.getRoomUser().getVirtualId());
					response.writeInt(entity.getRoomUser().getPosition().getX());
					response.writeInt(entity.getRoomUser().getPosition().getY());
					response.writeString(Double.toString(entity.getRoomUser().getPosition().getZ()));
					response.writeInt(0);
					response.writeInt(1);
					response.writeString("m");
					response.writeInt(-1);
					response.writeInt(-1);
					response.writeInt(0);
					response.writeInt(1337); // achievement points
					response.writeBool(false);

				}

				if (entity.getType() == EntityType.BOT) {


					response.writeInt(entity.getDetails().getId());
					response.writeString(entity.getDetails().getUsername());
					response.writeString(entity.getDetails().getMotto());
					response.writeString(entity.getDetails().getFigure());
					response.writeInt(entity.getRoomUser().getVirtualId());
					response.writeInt(entity.getRoomUser().getPosition().getX());
					response.writeInt(entity.getRoomUser().getPosition().getY());
					response.writeString(Double.toString(entity.getRoomUser().getPosition().getZ()));
					response.writeInt(0);
					response.writeInt(4); // 2 if pet
					
					// TODO: pet shit here
					
					response.writeString("m");
		            response.writeInt(1);
		            response.writeString("Alex");
		            response.writeInt(5);
		            response.appendShort(1);
		            response.appendShort(2);
		            response.appendShort(3);
		            response.appendShort(4);
		            response.appendShort(5);

				}
			}
		}
	}

}