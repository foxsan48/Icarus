package org.alexdev.icarus.messages.outgoing.room.items;

import org.alexdev.icarus.game.entity.Entity;
import org.alexdev.icarus.game.item.Item;
import org.alexdev.icarus.game.pathfinder.Position;
import org.alexdev.icarus.messages.MessageComposer;
import org.alexdev.icarus.messages.headers.Outgoing;
import org.alexdev.icarus.util.Util;

public class SlideObjectMessageComposer extends MessageComposer {

    private Item item;
    private Position next;
    private int rollerId;
    private double nextHeight;
    private Entity entity;

    public SlideObjectMessageComposer(Item item, Position next, int rollerId, double nextHeight) {
        this.item = item;
        this.next = next;
        this.rollerId = rollerId;
        this.nextHeight = nextHeight;
    }

    public SlideObjectMessageComposer(Entity entity, Position next, int rollerId, double nextHeight) {
        this.entity = entity;
        this.next = next;
        this.rollerId = rollerId;
        this.nextHeight = nextHeight;
    }

    @Override
    public void write() {
        this.response.init(Outgoing.SlideObjectMessageComposer);

        if (this.item != null) {
            this.response.writeInt(this.item.getPosition().getX());
            this.response.writeInt(this.item.getPosition().getY());
            this.response.writeInt(this.next.getX());
            this.response.writeInt(this.next.getY());
            this.response.writeInt(1);
            this.response.writeInt(this.item.getId());
            this.response.writeString(Util.getDecimalFormatter().format(this.item.getPosition().getZ()));
            this.response.writeString(Util.getDecimalFormatter().format(this.nextHeight));
            this.response.writeInt(this.rollerId);
        } else {
            this.response.writeInt(this.entity.getRoomUser().getPosition().getX());
            this.response.writeInt(this.entity.getRoomUser().getPosition().getY());
            this.response.writeInt(this.next.getX());
            this.response.writeInt(this.next.getY());
            this.response.writeInt(0);
            this.response.writeInt(this.rollerId);
            this.response.writeInt(2);
            this.response.writeInt(this.entity.getRoomUser().getVirtualId());
            this.response.writeString(Util.getDecimalFormatter().format(this.entity.getRoomUser().getPosition().getZ()));
            this.response.writeString(Util.getDecimalFormatter().format(this.nextHeight));
        }
    }
}
