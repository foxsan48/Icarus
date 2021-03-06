package org.alexdev.icarus.messages.outgoing.user;

import org.alexdev.icarus.messages.MessageComposer;
import org.alexdev.icarus.messages.headers.Outgoing;

public class CreditsMessageComposer extends MessageComposer {

    private int credits;

    public CreditsMessageComposer(int credits) {
        this.credits = credits;
    }

    @Override
    public void write() {
        this.response.init(Outgoing.CreditsMessageComposer);
        this.response.writeString(this.credits + ".0");
    }

}
