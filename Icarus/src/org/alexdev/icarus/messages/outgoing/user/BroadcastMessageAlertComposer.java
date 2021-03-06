package org.alexdev.icarus.messages.outgoing.user;

import org.alexdev.icarus.messages.MessageComposer;
import org.alexdev.icarus.messages.headers.Outgoing;

public class BroadcastMessageAlertComposer extends MessageComposer {

    private String message;

    public BroadcastMessageAlertComposer(String message) {
        this.message = message;
    }

    @Override
    public void write() {
        this.response.init(Outgoing.BroadcastMessageAlertComposer);
        this.response.writeString(this.message);
        this.response.writeString(""); // TODO: URL
    }

}
