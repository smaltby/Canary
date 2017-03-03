package me.seanmaltby.canary;

import android.util.Log;

import com.spotify.sdk.android.player.Error;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.SpotifyPlayer;

// TODO NativeBridge is no longer an appropriate name for this class, should be changed, and probably make the class no longer static
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

    public static void handleInput(String input)
    {
        String[] commands = input.split(" ");
        if(commands[0].equals("playuri"))
            playUri(commands[1]);
        else if(commands[0].equals("pause"))
            pause();
        else if(commands[0].equals("resume"))
            resume();
        else if(commands[0].equals("next"))
            next();
        else if(commands[0].equals("shuffle"))
            toggleShuffle(true);
        else if(commands[0].equals("repeat"))
            toggleRepeat(true);
        else if(commands[0].equals("error"))
            Log.d(TAG, input);  // TODO handle error gracefully
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
