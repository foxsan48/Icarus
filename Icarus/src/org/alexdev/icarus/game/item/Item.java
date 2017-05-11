package org.alexdev.icarus.game.item;

import java.util.List;

import org.alexdev.icarus.dao.mysql.ItemDao;
import org.alexdev.icarus.game.furniture.ItemDefinition;
import org.alexdev.icarus.game.furniture.FurnitureManager;
import org.alexdev.icarus.game.furniture.interactions.InteractionType;
import org.alexdev.icarus.game.pathfinder.AffectedTile;
import org.alexdev.icarus.game.pathfinder.Position;
import org.alexdev.icarus.game.player.PlayerDetails;
import org.alexdev.icarus.game.player.PlayerManager;
import org.alexdev.icarus.server.api.messages.Response;
import org.alexdev.icarus.util.GameSettings;

import com.google.common.collect.Lists;

public class Item {

    private int gameId;
    private long databaseId;
    private int userId;
    private int itemId;
    private int roomId;
    private Position position;

    private int rotation;
    private String extraData;
    private ItemType type;

    private int lengthX = 0;
    private int lengthY = 0;

    private char side = 0;
    private int widthX = 0;
    private int widthY = 0;

    public Item(long databaseId, int userId, int itemId, int roomId, String x, String y, double z, int rotation, String extraData) {
        this.databaseId = databaseId;
        this.gameId = GameSettings.ITEM_ID_COUNTER++;
        this.userId = userId;
        this.itemId = itemId;
        this.roomId = roomId;
        this.rotation = rotation;
        this.extraData = extraData;

        if (this.getDefinition().getType().equals("i")) {
            this.type = ItemType.WALL;
        } else if (this.getDefinition().getType().equals("s")) {
            this.type = ItemType.FLOOR;
        } else {
            this.type = ItemType.OTHER;
        }
        
        this.position = new Position();

        if (this.type == ItemType.FLOOR) {
            this.position.setX(Integer.parseInt(x));
            this.position.setY(Integer.parseInt(y));
            this.position.setZ(z);
        } else {
            try {
                String[] firstPosition = x.split(",");

                this.side = firstPosition[0].toCharArray()[0];
                this.widthX = Integer.parseInt(firstPosition[1]);
                this.widthY = Integer.parseInt(firstPosition[2]);

                String[] secondPosition = y.split(",");

                this.lengthX = Integer.parseInt(secondPosition[0]);
                this.lengthY = Integer.parseInt(secondPosition[1]);

            } catch (Exception e) {
                this.side = ' ';
                this.widthX = 0;
                this.widthY = 0;
                this.lengthX = 0;
                this.lengthY = 0;
            }
        }
    }

    public List<AffectedTile> getAffectedTiles() {

        if (this.type == ItemType.WALL) {
            return Lists.newArrayList();
        }

        return AffectedTile.getAffectedTilesAt(this.getDefinition().getLength(), this.getDefinition().getWidth(), this.position.getX(), this.position.getY(), this.rotation);
    }

    public boolean canWalk() {

        ItemDefinition definition = this.getDefinition();

        if (definition.isCanSit()) {
            return true;
        }

        if (definition.getInteractionType() == InteractionType.BED) {
            return true;
        }

        return false;
    }

    public void serialise(Response message) {
        if (this.type == ItemType.WALL) {

            message.writeString(this.gameId + "");
            message.writeInt(this.getDefinition().getSpriteId());
            message.writeString(this.getWallPosition());

            if (this.getDefinition().getInteractionType() == InteractionType.POSTIT) {
                message.writeString(this.extraData.split(" ")[0]);
            } else {
                message.writeString(this.extraData);
            }

            message.writeInt(-1);
            message.writeInt(this.getDefinition().getInteractionType() == InteractionType.DEFAULT ? 0 : 1);
            message.writeInt(this.userId);

        }

        if (this.type == ItemType.FLOOR) {

            message.writeInt(this.gameId);
            message.writeInt(this.getDefinition().getSpriteId());
            message.writeInt(this.position.getX());
            message.writeInt(this.position.getY());
            message.writeInt(this.rotation);
            message.writeString("" + this.position.getZ());
            message.writeString("" + this.position.getZ());

            if (this.getDefinition().getInteractionType() == InteractionType.YOUTUBETV) {

                message.writeInt(0);
                message.writeInt(1);
                message.writeInt(1);
                message.writeString("THUMBNAIL_URL");
                message.writeString("/deliver/" + "");
            } else if (this.getDefinition().getInteractionType() == InteractionType.BADGE_DISPLAY) {

                message.writeInt(0);
                message.writeInt(2);
                message.writeInt(4);

                if (this.extraData.length() > 0) {

                    message.writeString("0"); // extradata check

                    for (int i = 0; i <= this.extraData.split(Character.toString((char)9)).length - 1; i++)
                        message.writeString(this.extraData.split(Character.toString((char)9))[i]);
                } else {
                    message.writeInt(0);
                }

            } else if (this.getDefinition().getInteractionType() == InteractionType.BG_COLORBACKGROUND) {

                message.writeInt(1); // is ads
                message.writeInt(5); //type
                message.writeInt(4);

                message.writeInt(0); // online?
                message.writeInt(0);
                message.writeInt(0);
                message.writeInt(0);
            } else if (this.getDefinition().getInteractionType() == InteractionType.MANNEQUIN) {

                String[] Extradatas = this.extraData.split(";");

                if (this.extraData.contains(";") && Extradatas.length >= 3)
                {
                    message.writeInt(1);
                    message.writeInt(1);
                    message.writeInt(3);

                    message.writeString("GENDER");
                    message.writeString(Extradatas[0]);
                    message.writeString("FIGURE");
                    message.writeString(Extradatas[1]);
                    message.writeString("OUTFIT_NAME");
                    message.writeString(Extradatas[2]);
                }
                else
                {
                    message.writeInt(1);
                    message.writeInt(1);
                    message.writeInt(3);

                    message.writeString("GENDER");
                    message.writeString("m");
                    message.writeString("FIGURE");
                    message.writeString("");
                    message.writeString("OUTFIT_NAME");
                    message.writeString("");
                }
            } else {
                message.writeInt((this.getDefinition().getInteractionType() == InteractionType.DEFAULT) ? 0 : 1);
                message.writeInt(0);
                message.writeString(this.extraData);
            }

            message.writeInt(-1); // secondsToExpiration
            message.writeInt(this.getDefinition().getInteractionType() != InteractionType.DEFAULT ? 1 : 0);
            message.writeInt(this.getOwnerId());
        }
    }

    // Different ids so the database can support 64bit integer of items
    // When emulator is loaded, the item ids for the client are generated when client connects 
    // Credits to Leon to support 9,223,372,036,854,775,80 items :))

    public int getGameId() {
        return gameId;
    }

    public long getDatabaseId() {
        return databaseId;
    }

    public ItemDefinition getDefinition() {
        return FurnitureManager.getFurnitureById(this.itemId);
    }

    public void save() {
        ItemDao.saveItem(this);
    }

    public void delete() {
        ItemDao.deleteItem(this.databaseId);
    }

    public int getLengthX() {
        return lengthX;
    }

    public void setLengthX(int lengthX) {
        this.lengthX = lengthX;
    }

    public int getLengthY() {
        return lengthY;
    }

    public void setLengthY(int lengthY) {
        this.lengthY = lengthY;
    }

    public char getSide() {
        return side;
    }

    public void setSide(char side) {
        this.side = side;
    }

    public int getWidthX() {
        return widthX;
    }

    public void setWidthX(int widthX) {
        this.widthX = widthX;
    }

    public int getWidthY() {
        return widthY;
    }

    public void setWidthY(int widthY) {
        this.widthY = widthY;
    }

    public int getRotation() {
        return rotation;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;
    }

    public String getWallPosition() {

        if (this.type == ItemType.WALL) {
            return ":w=" + this.widthX + "," + this.widthY + " " + "l=" + this.lengthX + "," + this.lengthY + " " + this.side;
        }

        return null;
    }

    public int getOwnerId() {
        return userId;
    }

    public PlayerDetails getOwnerData() {
        return PlayerManager.getPlayerData(this.userId);
    }

    public int getItemId() {
        return itemId;
    }

    public void setRoomId(int id) {
        this.roomId = id;
    }

    public int getRoomId() {
        return roomId;
    }

    public String getExtraData() {
        return extraData;
    }

    public void setExtraData(String extraData) {
        this.extraData = extraData;
    }


    public ItemType getType() {
        return type;
    }

    public Position getPosition() {
        return this.position;
    }


}
