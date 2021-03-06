package org.alexdev.icarus.messages;

import java.util.HashMap;
import java.util.List;

import org.alexdev.icarus.game.player.Player;
import org.alexdev.icarus.messages.headers.Incoming;
import org.alexdev.icarus.messages.incoming.catalogue.CatalogueMessageEvent;
import org.alexdev.icarus.messages.incoming.catalogue.CataloguePageMessageEvent;
import org.alexdev.icarus.messages.incoming.catalogue.GiftingSettingsMessageEvent;
import org.alexdev.icarus.messages.incoming.catalogue.PromotableRoomsMessageEvent;
import org.alexdev.icarus.messages.incoming.catalogue.PurchaseItemMessageEvent;
import org.alexdev.icarus.messages.incoming.catalogue.PurchasePresentMessageEvent;
import org.alexdev.icarus.messages.incoming.catalogue.PurchaseRoomPromotionMessageEvent;
import org.alexdev.icarus.messages.incoming.groups.DeleteGroupMessageEvent;
import org.alexdev.icarus.messages.incoming.groups.GroupBadgeDialogMessageEvent;
import org.alexdev.icarus.messages.incoming.groups.GroupInfoMessageEvent;
import org.alexdev.icarus.messages.incoming.groups.GroupCatalogueMessageEvent;
import org.alexdev.icarus.messages.incoming.groups.GroupPurchaseMessageEvent;
import org.alexdev.icarus.messages.incoming.groups.GroupManageDetailsMessageEvent;
import org.alexdev.icarus.messages.incoming.groups.edit.EditGroupAccessMessageEvent;
import org.alexdev.icarus.messages.incoming.groups.edit.EditGroupBadgeMessageEvent;
import org.alexdev.icarus.messages.incoming.groups.edit.EditGroupColoursMessageEvent;
import org.alexdev.icarus.messages.incoming.groups.edit.EditGroupTextMessageEvent;
import org.alexdev.icarus.messages.incoming.groups.members.GroupGiveAdminMessageEvent;
import org.alexdev.icarus.messages.incoming.groups.members.GroupManageMembersMessageEvent;
import org.alexdev.icarus.messages.incoming.groups.members.GroupMembershipAcceptMessageEvent;
import org.alexdev.icarus.messages.incoming.groups.members.GroupMembershipRejectMessageEvent;
import org.alexdev.icarus.messages.incoming.groups.members.GroupMembershipRequestMessageEvent;
import org.alexdev.icarus.messages.incoming.groups.members.GroupRemoveAdminMessageEvent;
import org.alexdev.icarus.messages.incoming.groups.members.GroupRemoveMemberMessageEvent;
import org.alexdev.icarus.messages.incoming.handshake.*;
import org.alexdev.icarus.messages.incoming.items.PurchaseOfferMessageEvent;
import org.alexdev.icarus.messages.incoming.items.SaveBrandingMessageEvent;
import org.alexdev.icarus.messages.incoming.messenger.*;
import org.alexdev.icarus.messages.incoming.misc.*;
import org.alexdev.icarus.messages.incoming.navigator.*;
import org.alexdev.icarus.messages.incoming.pets.PetInformationMessageEvent;
import org.alexdev.icarus.messages.incoming.pets.PetRacesMessageEvent;
import org.alexdev.icarus.messages.incoming.pets.PlacePetMessageEvent;
import org.alexdev.icarus.messages.incoming.pets.RemovePetMessageEvent;
import org.alexdev.icarus.messages.incoming.pets.VerifyPetNameMessageEvent;
import org.alexdev.icarus.messages.incoming.room.*;
import org.alexdev.icarus.messages.incoming.room.floorplan.FloorPlanPropertiesMessageEvent;
import org.alexdev.icarus.messages.incoming.room.floorplan.SaveFloorPlanMessageEvent;
import org.alexdev.icarus.messages.incoming.room.items.ApplyDecorationMessageEvent;
import org.alexdev.icarus.messages.incoming.room.items.InteractItemMessageEvent;
import org.alexdev.icarus.messages.incoming.room.items.InventoryMessageEvent;
import org.alexdev.icarus.messages.incoming.room.items.MoodlightInteractMessageEvent;
import org.alexdev.icarus.messages.incoming.room.items.MoveItemMessageEvent;
import org.alexdev.icarus.messages.incoming.room.items.PickupItemMessageEvent;
import org.alexdev.icarus.messages.incoming.room.items.PlaceItemMessageEvent;
import org.alexdev.icarus.messages.incoming.room.items.SaveMoodlightPresetMessageEvent;
import org.alexdev.icarus.messages.incoming.room.items.ToggleMoodlightMessageEvent;
import org.alexdev.icarus.messages.incoming.room.settings.DeleteRoomMessageEvent;
import org.alexdev.icarus.messages.incoming.room.settings.GiveRightsMessageEvent;
import org.alexdev.icarus.messages.incoming.room.settings.RemoveAllRightsMessageEvent;
import org.alexdev.icarus.messages.incoming.room.settings.RemoveRightsMessageEvent;
import org.alexdev.icarus.messages.incoming.room.settings.RoomInfoMessageEvent;
import org.alexdev.icarus.messages.incoming.room.settings.RoomRightsMessageEvent;
import org.alexdev.icarus.messages.incoming.room.settings.RoomEditMessageEvent;
import org.alexdev.icarus.messages.incoming.room.settings.SaveRoomMessageEvent;
import org.alexdev.icarus.messages.incoming.room.user.*;
import org.alexdev.icarus.messages.incoming.trading.StartTradingMessageEvent;
import org.alexdev.icarus.messages.incoming.user.*;
import org.alexdev.icarus.server.api.messages.ClientMessage;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class MessageHandler {

    private HashMap<Integer, List<MessageEvent>> messages;
    private List<String> composerPackages;

    public MessageHandler() {
        this.messages = Maps.newHashMap();
        this.composerPackages = Lists.newArrayList();
        this.register();
        this.registerComposerPackages();
    }
    public void register() {
        
        this.messages.clear();
        
        this.registerHandshakePackets();
        this.registerUserPackets();
        this.registerMiscPackets();
        this.registerMessenger();
        this.registerNavigatorPackets();
        this.registerRoomPackets();
        this.registerCataloguePackets();
        this.registerItemPackets();
        this.registerPetPackets();
        this.registerRoomSettingPackets();
        this.registerRoomFloorplanPackets();
        this.registerGroupPackets();
        this.registerTradePackets();
    }

    /**
     * Register handshake packets.
     */
    private void registerHandshakePackets() {
        this.registerEvent(Incoming.VersionCheckMessageEvent, new VersionCheckMessageEvent());
        this.registerEvent(Incoming.UniqueIDMessageEvent, new UniqueIDMessageEvent());
        this.registerEvent(Incoming.AuthenticateMessageEvent, new AuthenticateMessageEvent());
    }
    
    /**
     * Register user packets.
     */
    private void registerUserPackets() {
        this.registerEvent(Incoming.InfoRetrieveMessageEvent, new InfoRetrieveMessageEvent());
        this.registerEvent(Incoming.CurrencyBalanceMessageEvent, new CurrencyBalanceMessageEvent());
        this.registerEvent(Incoming.ChangeAppearanceMessageEvent, new ChangeAppearanceMessageEvent());
        this.registerEvent(Incoming.SubscriptionMessageEvent, new SubscriptionMessageEvent());
        this.registerEvent(Incoming.NavigatorPromoteRoomCategories, new WelcomeMessageEvent());
        this.registerEvent(Incoming.HabboClubCenterMessageEvent, new HabboClubCenterMessageEvent());
    }
    
    /**
     * Register messenger.
     */
    private void registerMessenger() {
        this.registerEvent(Incoming.MessengerInitMessageEvent, new MessengerInitMessageEvent());
        this.registerEvent(Incoming.MessengerSearchMessageEvent, new MessengerSearchMessageEvent());
        this.registerEvent(Incoming.MessengerRequestMessageEvent, new MessengerRequestMessageEvent());
        this.registerEvent(Incoming.MessengerAcceptMessageEvent, new MessengerAcceptMessageEvent());
        this.registerEvent(Incoming.MessengerDeclineMessageEvent, new MessengerDeclineMessageEvent());
        this.registerEvent(Incoming.MessengerDeleteFriendMessageEvent, new MessengerDeleteFriendMessageEvent());
        this.registerEvent(Incoming.MessengerTalkMessageEvent, new MessengerTalkMessageEvent());
        this.registerEvent(Incoming.MessengerUpdateMessageEvent, new MessengerUpdateMessageEvent());
        this.registerEvent(Incoming.FollowFriendMessageEvent, new FollowFriendMessageEvent());
    }
    
    /**
     * Register navigator packets.
     */
    private void registerNavigatorPackets() {
        this.registerEvent(Incoming.NewNavigatorMessageEvent, new NewNavigatorMessageEvent());
        this.registerEvent(Incoming.NavigatorPromoteRoomCategories, new NavigatorPromoteCategoriesMessageEvent());
        this.registerEvent(Incoming.SearchNewNavigatorEvent, new SearchNewNavigatorEvent());
        this.registerEvent(Incoming.CreateRoomMessageEvent, new CreateRoomMessageEvent());
        this.registerEvent(Incoming.CanCreateRoomMessageEvent, new CanCreateRoomMessageEvent());
    }

    /**
     * Register misc packets.
     */
    private void registerMiscPackets() {
        this.registerEvent(Incoming.EventLogMessageEvent, new EventLogMessageEvent());
        this.registerEvent(Incoming.LatencyTestMessageEvent, new LatencyTestMessageEvent());
    }
    
    /**
     * Register room packets.
     */
    private void registerRoomPackets() {
        this.registerEvent(Incoming.EnterRoomMessageEvent, new EnterRoomMessageEvent());
        this.registerEvent(Incoming.HeightMapMessageEvent, new HeightmapMessageEvent());
        this.registerEvent(Incoming.UserWalkMessageEvent, new UserWalkMessageEvent());
        this.registerEvent(Incoming.LeaveRoomMessageEvent, new LeaveRoomMessageEvent());
        this.registerEvent(Incoming.ChatMessageEvent, new ChatMessageEvent());
        this.registerEvent(Incoming.ShoutMessageEvent, new ShoutMessageEvent());
        this.registerEvent(Incoming.DanceMessageEvent, new DanceMessageEvent());
        this.registerEvent(Incoming.StartTypingMessageEvent, new TypingStatusMessageEvent());
        this.registerEvent(Incoming.StopTypingMessageEvent, new TypingStatusMessageEvent());
        this.registerEvent(Incoming.AnswerDoorbellMessageEvent, new DoorbellAnswerMessageEvent());
        this.registerEvent(Incoming.EnterDoorbellMessageEvent, new DoorbellEnterMessageEvent());
        this.registerEvent(Incoming.RoomThumbnailMessageEvent, new RoomThumbnailMessageEvent());
        this.registerEvent(Incoming.RoomCameraMessageEvent, new RoomThumbnailMessageEvent());
    }
    
    /**
     * Register room setting packets.
     */
    private void registerRoomSettingPackets() {
        this.registerEvent(Incoming.RoomInfoMessageEvent, new RoomInfoMessageEvent());
        this.registerEvent(Incoming.SaveRoomMessageEvent, new SaveRoomMessageEvent());
        this.registerEvent(Incoming.RoomEditInfoMessageEvent, new RoomEditMessageEvent());
        this.registerEvent(Incoming.RoomRightsMessageEvent, new RoomRightsMessageEvent());
        this.registerEvent(Incoming.RemoveRightsMessageEvent, new RemoveRightsMessageEvent());
        this.registerEvent(Incoming.GiveRightsMessageEvent, new GiveRightsMessageEvent());
        this.registerEvent(Incoming.DeleteRoomMessageEvent, new DeleteRoomMessageEvent());
        this.registerEvent(Incoming.RemoveAllRoomRightsMessageEvent, new RemoveAllRightsMessageEvent());
    }
    
    /**
     * Register room floorplan packets.
     */
    private void registerRoomFloorplanPackets() {
        this.registerEvent(Incoming.FloorPlanPropertiesMessageEvent, new FloorPlanPropertiesMessageEvent());
        this.registerEvent(Incoming.SaveFloorPlanMessageEvent, new SaveFloorPlanMessageEvent());
    }
    
    /**
     * Register catalogue packets.
     */
    private void registerCataloguePackets() {
        this.registerEvent(Incoming.CatalogueTabMessageEvent, new CatalogueMessageEvent());
        this.registerEvent(Incoming.CataloguePageMessageEvent, new CataloguePageMessageEvent());
        this.registerEvent(Incoming.PurchaseObjectMessageEvent, new PurchaseItemMessageEvent());
        this.registerEvent(Incoming.PurchasePresentMessageEvent, new PurchasePresentMessageEvent());
        this.registerEvent(Incoming.GiftingSettingsMessageEvent, new GiftingSettingsMessageEvent());
        this.registerEvent(Incoming.PromotableRoomsMessageEvent, new PromotableRoomsMessageEvent());
        this.registerEvent(Incoming.PurchaseRoomPromotionMessageEvent, new PurchaseRoomPromotionMessageEvent());
    }
    
    /**
     * Register pet packets.
     */
    private void registerPetPackets() {
        this.registerEvent(Incoming.PlacePetMessageEvent, new PlacePetMessageEvent());
        this.registerEvent(Incoming.PetRacesMessageEvent, new PetRacesMessageEvent());
        this.registerEvent(Incoming.VerifyPetNameMessageEvent, new VerifyPetNameMessageEvent());
        this.registerEvent(Incoming.PetInfoMessageEvent, new PetInformationMessageEvent());
        this.registerEvent(Incoming.RemovePetMessageEvent, new RemovePetMessageEvent());
    }
    
    /**
     * Register item packets.
     */
    private void registerItemPackets() {
        this.registerEvent(Incoming.InventoryMessageEvent, new InventoryMessageEvent());
        this.registerEvent(Incoming.PlaceItemMessageEvent, new PlaceItemMessageEvent());
        this.registerEvent(Incoming.MoveItemMessageEvent, new MoveItemMessageEvent());
        this.registerEvent(Incoming.PickupItemMessageEvent, new PickupItemMessageEvent());
        this.registerEvent(Incoming.ApplyDecorationMessageEvent, new ApplyDecorationMessageEvent());
        this.registerEvent(Incoming.MoveWallItemMessageEvent, new MoveItemMessageEvent());
        this.registerEvent(Incoming.InteractFloorItemMessageEvent, new InteractItemMessageEvent());
        this.registerEvent(Incoming.InteractWallItemMessageEvent, new InteractItemMessageEvent());
        this.registerEvent(Incoming.DropItemMessageEvent, new DropItemMessageEvent());
        this.registerEvent(Incoming.MoodlightInteractMessageEvent, new MoodlightInteractMessageEvent());
        this.registerEvent(Incoming.ToggleMoodlightMessageEvent, new ToggleMoodlightMessageEvent());
        this.registerEvent(Incoming.SaveMoodlightPresetMessageEvent, new SaveMoodlightPresetMessageEvent());
        this.registerEvent(Incoming.PurchaseOfferMessageEvent, new PurchaseOfferMessageEvent());
        this.registerEvent(Incoming.SaveBrandingMessageEvent, new SaveBrandingMessageEvent());
    }
    
    /*[17-09-2017 02:09:23] [ICARUS] >> Received: 3136 / [0][0][0][2][0][0][0][2] -- accept
[17-09-2017 02:09:25] [ICARUS] >> Received: 1308 / [0][0][0][2][0][0][0][2] -- reject

*/
    
    /**
     * Register group packets.
     */
    private void registerGroupPackets() {
        this.registerEvent(Incoming.GroupCatalogueMessageEvent, new GroupCatalogueMessageEvent());
        this.registerEvent(Incoming.GroupBadgeDialogMessageEvent, new GroupBadgeDialogMessageEvent());
        this.registerEvent(Incoming.GroupPurchaseMessageEvent, new GroupPurchaseMessageEvent());
        this.registerEvent(Incoming.GroupInfoMessageEvent, new GroupInfoMessageEvent());
        this.registerEvent(Incoming.DeleteGroupMessageEvent, new DeleteGroupMessageEvent());
        this.registerEvent(Incoming.GroupManageDetailsMessageEvent, new GroupManageDetailsMessageEvent());
        this.registerEvent(Incoming.GroupManageMembersMessageEvent, new GroupManageMembersMessageEvent());
        this.registerEvent(Incoming.GroupMembershipRequestMessageEvent, new GroupMembershipRequestMessageEvent());
        this.registerEvent(Incoming.GroupMembershipAcceptMessageEvent, new GroupMembershipAcceptMessageEvent());
        this.registerEvent(Incoming.GroupMembershipRejectMessageEvent, new GroupMembershipRejectMessageEvent());
        this.registerEvent(Incoming.GroupRemoveMemberMessageEvent, new GroupRemoveMemberMessageEvent());
        this.registerEvent(Incoming.GroupGiveAdminMessageEvent, new GroupGiveAdminMessageEvent());
        this.registerEvent(Incoming.GroupRemoveAdminMessageEvent, new GroupRemoveAdminMessageEvent());
        this.registerEvent(Incoming.EditGroupTextMessageEvent, new EditGroupTextMessageEvent());
        this.registerEvent(Incoming.EditGroupColoursMessageEvent, new EditGroupColoursMessageEvent());
        this.registerEvent(Incoming.EditGroupAccessMessageEvent, new EditGroupAccessMessageEvent());
        this.registerEvent(Incoming.EditGroupBadgeMessageEvent, new EditGroupBadgeMessageEvent());
    }
    
    /**
     * Register trade packets.
     */
    private void registerTradePackets() {
        this.registerEvent(Incoming.StartTradingMessageEvent, new StartTradingMessageEvent());
    }
    
    /**
     * Register composer packages.
     */
    private void registerComposerPackages() {
         this.composerPackages.add("org.alexdev.icarus.messages.outgoing.catalogue");
         this.composerPackages.add("org.alexdev.icarus.messages.outgoing.handshake");
         this.composerPackages.add("org.alexdev.icarus.messages.outgoing.item");
         this.composerPackages.add("org.alexdev.icarus.messages.outgoing.messenger");
         this.composerPackages.add("org.alexdev.icarus.messages.outgoing.navigator");
         this.composerPackages.add("org.alexdev.icarus.messages.outgoing.room");
         this.composerPackages.add("org.alexdev.icarus.messages.outgoing.room.floorplan");
         this.composerPackages.add("org.alexdev.icarus.messages.outgoing.room.settings");
         this.composerPackages.add("org.alexdev.icarus.messages.outgoing.groups");
         this.composerPackages.add("org.alexdev.icarus.messages.outgoing.room.items");
         this.composerPackages.add("org.alexdev.icarus.messages.outgoing.room.notify");
         this.composerPackages.add("org.alexdev.icarus.messages.outgoing.user");
     }

    /**
     * Register event.
     *
     * @param header the header
     * @param messageEvent the message event
     */
    private void registerEvent(Integer header, MessageEvent messageEvent) {
        
        if (!this.messages.containsKey(header)) {
            this.messages.put(header, Lists.newArrayList());
        }
        
        this.messages.get(header).add(messageEvent);
    }

    /**
     * Handle request.
     *
     * @param player the player
     * @param message the message
     */
    public void handleRequest(Player player, ClientMessage message) {
        
        if (this.messages.containsKey(message.getMessageId())) {

            for (MessageEvent event : this.messages.get(message.getMessageId())) {
                event.handle(player, message);
            }
        }
    }

    /**
     * Gets the messages.
     *
     * @return the messages
     */
    public HashMap<Integer, List<MessageEvent>> getMessages() {
        return messages;
    }

    /**
     * Gets the composer packages.
     *
     * @return the composer packages
     */
    public List<String> getComposerPackages() {
        return composerPackages;
    }
}
