package org.alexdev.icarus.game.room.managers;

import java.util.List;
import org.alexdev.icarus.dao.mysql.pets.PetDao;
import org.alexdev.icarus.game.entity.Entity;
import org.alexdev.icarus.game.entity.EntityType;
import org.alexdev.icarus.game.pets.Pet;
import org.alexdev.icarus.game.player.Player;
import org.alexdev.icarus.game.room.Room;
import org.alexdev.icarus.game.room.user.RoomUser;
import org.alexdev.icarus.messages.outgoing.room.user.RemoveUserMessageComposer;
import org.alexdev.icarus.messages.outgoing.room.user.UserDisplayMessageComposer;
import org.alexdev.icarus.messages.outgoing.room.user.UserStatusMessageComposer;

import com.google.common.collect.Lists;

public class RoomEntityManager {

    private Room room;
    private List<Entity> entities; 

    public RoomEntityManager(Room room) {
        this.room = room;
        this.entities = Lists.newArrayList();
    }

    public void addEntity(Entity entity) {

        this.addEntity(entity, 
                this.room.getModel().getDoorLocation().getX(), 
                this.room.getModel().getDoorLocation().getY(), 
                this.room.getModel().getDoorLocation().getRotation());
    }
    
    public void addEntity(Entity entity, int x, int y, int rotation) {

        if (entity.getType() == EntityType.PLAYER) {
            return;
        }

        RoomUser roomUser = entity.getRoomUser();

        roomUser.setRoom(this.room);
        roomUser.setVirtualId(this.room.getPrivateId().incrementAndGet());
        roomUser.getPosition().setX(x);
        roomUser.getPosition().setY(y);
        roomUser.getPosition().setZ(this.room.getModel().getHeight(roomUser.getPosition().getX(), roomUser.getPosition().getY()));
        roomUser.getPosition().setRotation(rotation);

        this.room.send(new UserDisplayMessageComposer(entity));
        this.room.send(new UserStatusMessageComposer(entity));

        if (!this.entities.contains(entity)) {
            this.entities.add(entity);
        }

        this.room.getMapping().getTile(x, y).setEntity(entity);
    }
    
    public void addPets() {
        for (Pet pet : PetDao.getRoomPets(this.room.getData().getId())) {
            pet.getRoomUser().setRoom(this.room);
            pet.getRoomUser().setVirtualId(this.room.getPrivateId().incrementAndGet());
            pet.getRoomUser().getPosition().setX(pet.getX());
            pet.getRoomUser().getPosition().setY(pet.getY());
            pet.getRoomUser().getPosition().setZ(this.room.getModel().getHeight(pet.getRoomUser().getPosition().getX(), pet.getRoomUser().getPosition().getY()));
            pet.getRoomUser().getPosition().setRotation(0);
            this.entities.add(pet);
        }
    }

    public void removeEntity(Entity entity) {

        if (this.entities != null) {
            this.entities.remove(entity);
            this.room.getData().updateUsersNow();
        }

        if (this.getPlayers().size() > 0) {
            this.room.send(new RemoveUserMessageComposer(entity.getRoomUser().getVirtualId()));
        }

        if (entity.getType() != EntityType.PLAYER) {
            if (entity.getType() == EntityType.PET) {
                ((Pet)entity).savePosition();
            }

            entity.dispose();
        }

        entity.getRoomUser().dispose();
    }

    public void cleanupEntities() {
        
        if (this.entities != null) {

            for (int i = 0; i < this.entities.size(); i++) {
                Entity entity = this.entities.get(i);

                if (entity.getType() != EntityType.PLAYER) {
                    this.removeEntity(entity);
                }
            }

            this.entities.clear();
        }
    }
    
    public List<Player> getPlayers() {

        List<Player> sessions = Lists.newArrayList();

        for (Entity entity : this.getEntities(EntityType.PLAYER)) {
            Player player = (Player)entity;
            sessions.add(player);
        }

        return sessions;
    }

    public List<Entity> getEntities(EntityType type) {
        
        List<Entity> entities = Lists.newArrayList();

        for (Entity entity : this.entities) {
            if (entity.getType() == type) {
                entities.add(entity);
            }
        }

        return entities;
    }

    public Entity getEntityById(int id) {

        for (Entity entity : this.entities) {
            if (entity.getDetails().getId() == id) {
                return entity;
            }
        }

        return null;
    }

    public List<Entity> getEntities() {
        return entities;
    }
}
