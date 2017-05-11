package org.alexdev.icarus.messages.outgoing.handshake;

import org.alexdev.icarus.messages.headers.Outgoing;
import org.alexdev.icarus.messages.parsers.OutgoingMessageComposer;
import org.alexdev.icarus.server.api.messages.AbstractResponse;

public class GenerateSecretKeyComposer implements OutgoingMessageComposer {
	
	public String publicKey;
		
	public GenerateSecretKeyComposer(String encryptedPublicKey) {
		this.publicKey = encryptedPublicKey;
	}

	@Override
	public void write(AbstractResponse response) {
		response.init(Outgoing.SecretKeyMessageComposer);	
		response.writeString(this.publicKey);
		response.writeBool(false);
	}
}
