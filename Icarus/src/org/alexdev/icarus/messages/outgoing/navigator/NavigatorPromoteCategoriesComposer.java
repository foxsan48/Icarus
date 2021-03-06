package org.alexdev.icarus.messages.outgoing.navigator;

import org.alexdev.icarus.messages.MessageComposer;
import org.alexdev.icarus.messages.headers.Outgoing;

public class NavigatorPromoteCategoriesComposer extends MessageComposer {

    @Override
    public void write() {
        this.response.init(Outgoing.NavigatorPromoteCategoriesComposer);
        this.response.writeInt(1);
        this.response.writeInt(1);
        this.response.writeString("Promoted Rooms");
        this.response.writeBool(true);
    }
}
