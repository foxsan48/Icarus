package org.alexdev.icarus.game.inventory;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.alexdev.icarus.dao.mysql.item.InventoryDao;
import org.alexdev.icarus.game.item.Item;
import org.alexdev.icarus.game.item.ItemType;
import org.alexdev.icarus.game.pets.Pet;
import org.alexdev.icarus.game.player.Player;
import org.alexdev.icarus.messages.outgoing.item.UnseenItemsNotificationComposer;
import org.alexdev.icarus.messages.outgoing.item.InventoryLoadMessageComposer;
import org.alexdev.icarus.messages.outgoing.item.RemoveInventoryItemComposer;
import org.alexdev.icarus.messages.outgoing.item.UpdateInventoryMessageComposer;
import org.alexdev.icarus.messages.outgoing.pets.PetInventoryMessageComposer;

public class Inventory {

    private Player player;
    private Map<Integer, Pet> pets;
    private Map<Integer, Item> items;

    public Inventory(Player player) {
        this.player = player;
    }

    /**
     * Initiates the inventory items
     */
    public void init() {
        this.items = InventoryDao.getInventoryItems(this.player.getDetails().getId());
        this.pets = InventoryDao.getInventoryPets(this.player.getDetails().getId());
    }

    /**
     * Adds the item.
     *
     * @param item the item
     */
    public void addItem(Item item) {
        this.items.put(item.getId(), item);
        this.player.send(new UnseenItemsNotificationComposer(item.getId(), 1));
    }

    /**
     * Removes the.
     *
     * @param item the item
     */
    public void remove(Item item) {
        this.items.remove(item.getId());
        this.player.send(new RemoveInventoryItemComposer(item.getId()));
    }

    /**
     * Adds the pet.
     *
     * @param pet the pet
     */
    public void addPet(Pet pet) {
        this.pets.put(pet.getId(), pet);
        this.player.send(new UnseenItemsNotificationComposer(pet.getId(), 3));
    }

    /**
     * Removes the.
     *
     * @param pet the pet
     */
    public void remove(Pet pet) {
        this.pets.remove(pet.getId());
        this.player.send(new RemoveInventoryItemComposer(pet.getId()));
    }

    /**
     * Update items.
     */
    public void updateItems() {
        this.player.send(new UpdateInventoryMessageComposer());
        this.player.send(new InventoryLoadMessageComposer(this.getWallItems(), this.getFloorItems()));
    }

    /**
     * Update pets.
     */
    public void updatePets() {
        this.player.send(new UpdateInventoryMessageComposer());
        this.player.send(new PetInventoryMessageComposer(this.pets));
    }

    /**
     * Gets the item.
     *
     * @param id the id
     * @return the item
     */
    public Item getItem(int id) {

        if (this.items.containsKey(id)) {
            return this.items.get(id);
        }

        return null;
    }

    /**
     * Gets the items.
     *
     * @return the items
     */
    public Map<Integer, Item> getItems() {
        return items;
    }

    /**
     * Gets the floor items.
     *
     * @return the floor items
     */
    public List<Item> getFloorItems() {
        return items.values().stream().filter(item -> item != null && item.getType() == ItemType.FLOOR).collect(Collectors.toList());
    }

    /**
     * Gets the pets.
     *
     * @return the pets
     */
    public Map<Integer, Pet> getPets() {
        return pets;
    }

    /**
     * Gets the wall items.
     *
     * @return the wall items
     */
    public List<Item> getWallItems() {
        return items.values().stream().filter(item -> item != null && item.getType() == ItemType.WALL).collect(Collectors.toList());
    }

    /**
     * Gets the pet.
     *
     * @param id the id
     * @return the pet
     */
    public Pet getPet(int id) {
        return this.pets.get(id);
    }
}
