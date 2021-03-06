package org.alexdev.icarus.messages.outgoing.room;

import org.alexdev.icarus.messages.MessageComposer;
import org.alexdev.icarus.messages.headers.Outgoing;

public class RoomRatingMessageComposer extends MessageComposer {

    private int score;

    public RoomRatingMessageComposer(int score) {
        this.score = score;
    }

    @Override
    public void write() {
        this.response.init(Outgoing.RoomRatingMessageComposer);
        this.response.writeInt(this.score);
        this.response.writeBool(false);
    }
}
