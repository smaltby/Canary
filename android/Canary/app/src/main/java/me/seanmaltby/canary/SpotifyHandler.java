package me.seanmaltby.canary;

import android.util.Log;

import com.spotify.sdk.android.player.Error;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.SpotifyPlayer;

public class SpotifyHandler
{
    private static final String TAG = "SpotifyHandler";
    private static SpotifyPlayer mPlayer;
    private final Player.OperationCallback operationCallback = new Player.OperationCallback()
    {
        @Override
        public void onSuccess() {}

        @Override
        public void onError(Error error) {}
    };

    public SpotifyHandler(SpotifyPlayer player)
    {
        mPlayer = player;
    }

    public void handleInput(String input)
    {
        String[] commands = input.split(" ");
        switch (commands[0]) {
            case "playuri":
                playUri(commands[1]);
                break;
            case "pause":
                pause();
                break;
            case "resume":
                resume();
                break;
            case "next":
                next();
                break;
            case "shuffle":
                toggleShuffle(true);    // TODO parse commands[1] as boolean
                break;
            case "repeat":
                toggleRepeat(true);     // TODO parse commands[1] as boolean
                break;
            case "error":
                Log.d(TAG, input);      // TODO handle error gracefully
                break;
        }
    }

    private void playUri(String uri)
    {
        Log.d(TAG, "PLAYING URI: " + uri);
        mPlayer.playUri(operationCallback, uri, 0, 0);
    }

    private void pause()
    {
        mPlayer.pause(operationCallback);
    }

    private void resume()
    {
        mPlayer.resume(operationCallback);
    }

    private void next()
    {
        mPlayer.skipToNext(operationCallback);
    }

    private void toggleShuffle(boolean shuffle)
    {
        mPlayer.setShuffle(operationCallback, shuffle);
    }

    private void toggleRepeat(boolean repeat)
    {
        mPlayer.setRepeat(operationCallback, repeat);
    }
}
