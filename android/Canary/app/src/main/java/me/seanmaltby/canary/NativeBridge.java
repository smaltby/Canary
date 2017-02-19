package me.seanmaltby.canary;

import android.util.Log;

import com.spotify.sdk.android.player.Error;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.SpotifyPlayer;

public class NativeBridge
{
    private static final String TAG = "NativeBridge";
    private static SpotifyPlayer mPlayer;
    private static final Player.OperationCallback operationCallback = new Player.OperationCallback()
    {
        @Override
        public void onSuccess() {}

        @Override
        public void onError(Error error) {}
    };

    public static void init(SpotifyPlayer player)
    {
        mPlayer = player;
    }

    public static void playUri(String uri)
    {
        Log.d(TAG, "PLAYING URI: " + uri);
        mPlayer.playUri(operationCallback, uri, 0, 0);
    }

    public static void pause()
    {
        mPlayer.pause(operationCallback);
    }

    public static void resume()
    {
        mPlayer.resume(operationCallback);
    }

    public static void next()
    {
        mPlayer.skipToNext(operationCallback);
    }

    public static void toggleShuffle(boolean shuffle)
    {
        mPlayer.setShuffle(operationCallback, shuffle);
    }

    public static void toggleRepeat(boolean repeat)
    {
        mPlayer.setRepeat(operationCallback, repeat);
    }
}
