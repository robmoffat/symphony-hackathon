package com.db.symphonyp.tabs.common;

import com.db.symphonyp.tabs.BotBrain;
import com.db.symphonyp.tabs.WithBrain;
import listeners.RoomListener;
import model.InboundMessage;
import model.Stream;
import model.events.*;
import org.springframework.stereotype.Component;

@Component
public class RoomListenerImpl implements WithBrain, RoomListener {
    private BotBrain brain;

    public RoomListenerImpl withBrain(BotBrain brain) {
        this.brain = brain;
        return this;
    }

    public void onRoomMessage(InboundMessage message) {
        brain.onRoomMessage(message);
    }

    @Override
    public void onRoomCreated(RoomCreated roomCreated) {

    }

    @Override
    public void onRoomDeactivated(RoomDeactivated roomDeactivated) {

    }

    @Override
    public void onRoomMemberDemotedFromOwner(RoomMemberDemotedFromOwner roomMemberDemotedFromOwner) {

    }

    @Override
    public void onRoomMemberPromotedToOwner(RoomMemberPromotedToOwner roomMemberPromotedToOwner) {

    }

    @Override
    public void onRoomReactivated(Stream stream) {

    }

    @Override
    public void onRoomUpdated(RoomUpdated roomUpdated) {

    }

    @Override
    public void onUserJoinedRoom(UserJoinedRoom userJoinedRoom) {

    }

    @Override
    public void onUserLeftRoom(UserLeftRoom userLeftRoom) {

    }


}
