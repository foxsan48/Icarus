package org.alexdev.icarus.messages.outgoing.room.notify;

import org.alexdev.icarus.messages.MessageComposer;
import org.alexdev.icarus.messages.headers.Outgoing;

public class GenericErrorMessageComposer extends MessageComposer {

    private int errorCode;

    public GenericErrorMessageComposer(int errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public void write() {
        this.response.init(Outgoing.GenericErrorMessageComposer);
        this.response.writeInt(this.errorCode);
    }
}
