package io.github.boldijar.cosasapp.data;

import com.google.gson.annotations.SerializedName;

/**
 * @author Paul
 * @since 2018.10.13
 */
public enum MessageType {

    @SerializedName("room_player_joined")
    ROOM_PLAYER_JOINED,
    @SerializedName("room_player_left")
    ROOM_PLAYER_LEFT,
    @SerializedName("room_player_update")
    ROOM_PLAYER_UPDATE,
    @SerializedName("room_game_over")
    ROOM_GAME_OVER,
    @SerializedName("room_destroyed")
    ROOM_GAME_DESTROYED
}