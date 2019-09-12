package com.db.symphonyp.tabs;

import clients.SymBotClient;
import listeners.RoomListener;
import model.InboundMessage;
import model.OutboundMessage;
import model.Stream;
import model.events.*;

public class RoomListenerImpl implements RoomListener {
    private SymBotClient bot;

    public RoomListenerImpl(SymBotClient botClient) {
        bot = botClient;
    }

    public void onRoomMessage(InboundMessage message) {
        String streamId = message.getStream().getStreamId();
        String firstName = message.getUser().getFirstName();
        String messageOut = String.format("Hello %s!", firstName);
        this.bot.getMessagesClient().sendMessage(streamId, new OutboundMessage(messageOut));
    }

    public void onUserJoinedRoom(UserJoinedRoom userJoinedRoom) {
        String streamId = userJoinedRoom.getStream().getStreamId();
        String firstName = userJoinedRoom.getAffectedUser().getFirstName();
        String messageOut = String.format("Welcome %s!", firstName);
        this.bot.getMessagesClient().sendMessage(streamId, new OutboundMessage(messageOut));
    }

    public void onRoomCreated(RoomCreated roomCreated) {
    }

    public void onRoomDeactivated(RoomDeactivated roomDeactivated) {
    }

    public void onRoomMemberDemotedFromOwner(RoomMemberDemotedFromOwner roomMemberDemotedFromOwner) {
    }

    public void onRoomMemberPromotedToOwner(RoomMemberPromotedToOwner roomMemberPromotedToOwner) {
    }

    public void onRoomReactivated(Stream stream) {
    }

    public void onRoomUpdated(RoomUpdated roomUpdated) {
    }

    public void onUserLeftRoom(UserLeftRoom userLeftRoom) {
    }
}
