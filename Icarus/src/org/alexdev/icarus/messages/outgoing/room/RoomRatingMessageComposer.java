package org.alexdev.icarus.messages.outgoing.room;

import org.alexdev.icarus.messages.headers.Outgoing;
import org.alexdev.icarus.messages.parsers.OutgoingMessageComposer;
import org.alexdev.icarus.server.api.messages.AbstractResponse;

public class RoomRatingMessageComposer implements OutgoingMessageComposer {

	private int score;

	public RoomRatingMessageComposer(int score) {
		this.score = score;
	}

	@Override
	public void write(AbstractResponse response) {
		response.init(Outgoing.RoomRatingMessageComposer);
		response.writeInt(this.score);
		response.writeBool(false);
	}

}