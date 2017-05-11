package org.alexdev.icarus.game.room.entity;

import java.util.HashMap;
import java.util.LinkedList;

import org.alexdev.icarus.game.entity.IEntity;
import org.alexdev.icarus.game.player.Player;
import org.alexdev.icarus.game.room.Room;
import org.alexdev.icarus.game.room.model.Point;
import org.alexdev.icarus.game.room.model.RoomModel;
import org.alexdev.icarus.log.DateTime;
import org.alexdev.icarus.messages.outgoing.room.notify.FloodFilterMessageComposer;
import org.alexdev.icarus.messages.outgoing.room.user.TalkMessageComposer;
import org.alexdev.icarus.messages.outgoing.room.user.UserStatusMessageComposer;
import org.alexdev.icarus.util.GameSettings;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public abstract class RoomEntity {

	private int virtualId;
	private int lastChatId;
	private int danceId;

	private Point position;
	private Point goal;

	private int rotation;
	private int headRotation;

	private boolean isWalking;
	private boolean needsUpdate;

	private long chatFloodTimer;
	private int chatCount;
	
	private HashMap<String, String> statuses;
	private LinkedList<Point> path;
	private IEntity entity;
	private Room room;
	
	public RoomEntity(IEntity entity) {
		this.dispose();
		this.entity = entity;
	}
	
	public void chat(String message, int bubble, int count, boolean shout, boolean spamCheck) {

		boolean isStaff = false;
		Player player = null;

		if (this.entity instanceof Player) {

			player = (Player)this.entity;
			isStaff = player.getDetails().hasFuse("moderator");
		}

		// if current time less than the chat flood timer (last chat time + seconds to check)
		// say that they still need to wait before shouting again
		if (spamCheck) {
			if (DateTime.getTimeSeconds() < this.chatFloodTimer && this.chatCount >= GameSettings.MAX_CHAT_BEFORE_FLOOD) {

				if (!isStaff) {
					if (player != null) {
						player.send(new FloodFilterMessageComposer(GameSettings.CHAT_FLOOD_WAIT));
					}
					return;
				}
			}
		}

		// TODO: Check if not bot
		// The below function validates the chat bubbles
		if (bubble == 2 || (bubble == 23 && !player.getDetails().hasFuse("moderator")) || bubble < 0 || bubble > 29) {
			bubble = this.lastChatId;
		}

		this.room.send(new TalkMessageComposer(this, shout, message, count, bubble));

		// if the users timestamp has passed the check but the chat count is still high
		// the chat count is reset then

		if (spamCheck) {
			if (!player.getDetails().hasFuse("moderator")) {

				if (DateTime.getTimeSeconds() > this.chatFloodTimer && this.chatCount >= GameSettings.MAX_CHAT_BEFORE_FLOOD) {
					this.chatCount = 0;
				} else {
					this.chatCount = this.chatCount + 1;
				}

				this.chatFloodTimer = (DateTime.getTimeSeconds() + GameSettings.CHAT_FLOOD_SECONDS);

			}
		}
	}

	public void dispose() {

		if (this.statuses != null) {
			this.statuses.clear();
		}

		if (this.path != null) {
			this.path.clear();
		}
		
		this.statuses = null;
		this.path = null;

		this.statuses = Maps.newHashMap();
		this.path = Lists.newLinkedList();

		this.position = null;
		this.goal = null;
		
		this.position = new Point(0, 0, 0);
		this.goal = new Point(0, 0, 0);

		this.lastChatId = 0;
		this.virtualId = -1;
		this.danceId = 0;

	}

	public Point getPosition() {
		return position;
	}

	public void setPosition(Point position) {
		this.position = position;
	}

	public Point getGoal() {
		return goal;
	}

	public void setGoal(Point goal) {
		this.goal = goal;
	}

	public void updateStatus() {
		this.room.send(new UserStatusMessageComposer(this.entity));
	}

	public boolean isDancing() {
		return this.danceId != 0;
	}
	public int getVirtualId() {
		return virtualId;
	}

	public void setVirtualId(int virtualId) {
		this.virtualId = virtualId;
	}

	public int getLastChatId() {
		return lastChatId;
	}

	public void setLastChatId(int lastChatId) {
		this.lastChatId = lastChatId;
	}

	public int getDanceId() {
		return danceId;
	}

	public void setDanceId(int danceId) {
		this.danceId = danceId;
	}

	public int getRotation() {
		return rotation;
	}

	public void setRotation(int rotation) {
		this.rotation = rotation;
	}

	public int getHeadRotation() {
		return headRotation;
	}

	public void setRotation(int rotation, boolean headOnly) {

		this.headRotation = rotation;

		if (!headOnly) {
			this.rotation = rotation;
		}
	}

	public HashMap<String, String> getStatuses() {
		return statuses;
	}

	public LinkedList<Point> getPath() {
		return path;
	}


	public void setPath(LinkedList<Point> path) {

		if (this.path != null) {
			this.path.clear();
		}

		this.path = path;
	}

	public boolean needsUpdate() {
		return needsUpdate;
	}

	public void setNeedUpdate(boolean needsWalkUpdate) {
		this.needsUpdate = needsWalkUpdate;

		if (!this.needsUpdate) {
			this.goal.setX(-1);
			this.goal.setY(-1);
		}
	}

	public Room getRoom() {
		return room;
	}

	public int getRoomId() {
		return (room == null ? 0 : room.getData().getId());
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public RoomModel getModel() {
		return room.getData().getModel();
	}
	public boolean isWalking() {
		return isWalking;
	}

	public void setWalking(boolean isWalking) {
		this.isWalking = isWalking;
	}

	public IEntity getEntity() {
		return entity;
	}
}