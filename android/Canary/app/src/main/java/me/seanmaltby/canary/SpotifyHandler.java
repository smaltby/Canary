package me.seanmaltby.canary;

import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.TextView;

import com.spotify.sdk.android.player.Error;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerEvent;
import com.spotify.sdk.android.player.SpotifyPlayer;

import java.util.Locale;

public class SpotifyHandler
{
    private static final String TAG = "SpotifyHandler";
    private MainActivity mActivity;
    private SpotifyPlayer mPlayer;
    private final Player.OperationCallback operationCallback = new Player.OperationCallback()
    {
        @Override
        public void onSuccess() {}

        @Override
        public void onError(Error error) {}
    };

    public SpotifyHandler(MainActivity activity, final SpotifyPlayer player)
    {
        mActivity = activity;
        mPlayer = player;
        toggleShuffle(true);
        toggleRepeat(true);
        mPlayer.addNotificationCallback(new Player.NotificationCallback()
        {
            @Override
            public void onPlaybackEvent(PlayerEvent playerEvent)
            {
                switch (playerEvent)
                {
                    case kSpPlaybackNotifyTrackChanged:
                        Log.d(TAG, "Track changed, updating album cover");
                        mActivity.updateAlbumCover(mPlayer.getMetadata().currentTrack.albumCoverWebUrl);
                        mActivity.speak("Playing " + mPlayer.getMetadata().currentTrack.name + " by " + mPlayer.getMetadata().currentTrack.artistName);
                        break;
                    case kSpPlaybackNotifyShuffleOn:
                        ((TextView) mActivity.findViewById(R.id.shuffle)).setText("Shuffle: True");
                        break;
                    case kSpPlaybackNotifyShuffleOff:
                        ((TextView) mActivity.findViewById(R.id.shuffle)).setText("Shuffle: False");
                        break;
                    case kSpPlaybackNotifyRepeatOn:
                        ((TextView) mActivity.findViewById(R.id.repeat)).setText("Repeat: True");
                        break;
                    case kSpPlaybackNotifyRepeatOff:
                        ((TextView) mActivity.findViewById(R.id.repeat)).setText("Repeat: False");
                        break;

                }
            }

            @Override
            public void onPlaybackError(Error error)
            {

            }
        });
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
                toggleShuffle(commands[1].equals("true"));
                break;
            case "repeat":
                toggleRepeat(commands[1].equals("true"));
                break;
            case "error":
                mActivity.speak(input.substring(input.indexOf(' ')));
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
