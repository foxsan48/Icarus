package org.alexdev.icarus.messages.incoming.pets;

import org.alexdev.icarus.game.pets.Pet;
import org.alexdev.icarus.game.player.Player;
import org.alexdev.icarus.game.room.Room;
import org.alexdev.icarus.log.Log;
import org.alexdev.icarus.messages.MessageEvent;
import org.alexdev.icarus.messages.outgoing.pets.PetInformationComposer;
import org.alexdev.icarus.server.api.messages.ClientMessage;

public class PetInformationMessageEvent implements MessageEvent {

    @Override
    public void handle(Player player, ClientMessage reader) {
       
        Room room = player.getRoom();
        
        if (room == null) {
            return;
        }
        
        Pet pet = room.getEntityManager().getEntityById(reader.readInt(), Pet.class);
        
        if (pet == null) {
            Log.println("test?!");
            return;
        }
        
        player.send(new PetInformationComposer(pet));
    }

}
