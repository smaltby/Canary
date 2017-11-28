package me.seanmaltby.canary;

import android.util.Log;
import android.widget.TextView;

import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Error;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerEvent;
import com.spotify.sdk.android.player.SpotifyPlayer;

public class SpotifyHandler implements SpotifyPlayer.NotificationCallback, ConnectionStateCallback
{
    private static final String TAG = "SpotifyHandler";
    private MainActivity mActivity;
    private SpotifyPlayer mPlayer;
    private boolean mLocalShuffle = false;
    private boolean mSpotifyShuffle = false;
    private boolean mLocalRepeat = false;
    private boolean mSpotifyRepeat = false;
    private final Player.OperationCallback mOperationCallback = new Player.OperationCallback()
    {
        @Override
        public void onSuccess()
        {
            Log.d(TAG, "Operation sucessful");
        }

        @Override
        public void onError(Error error)
        {
            Log.e(TAG, "Error on operation : " + error.toString());
            switch (error)
            {
                case kSpErrorNotActiveDevice:
                    break;
            }
        }
    };

    public SpotifyHandler(MainActivity activity, final SpotifyPlayer player)
    {
        mActivity = activity;
        mPlayer = player;
        mPlayer.addNotificationCallback(this);
        mPlayer.addConnectionStateCallback(this);
    }

    public void handleInput(String input)
    {
        String[] commands = input.split(" ");
        switch (commands[0])
        {
            case "playuri":
                if(commands.length > 2 && commands[2].equals("true"))
                    toggleShuffle(true);
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
        if(mSpotifyShuffle != mLocalShuffle)
        {
            mPlayer.playUri(mOperationCallback, uri, 0, 0);
            mPlayer.setShuffle(mOperationCallback, mLocalShuffle);
        }
        if(mSpotifyRepeat != mLocalRepeat)
        {
            mPlayer.playUri(mOperationCallback, uri, 0, 0);
            mPlayer.setRepeat(mOperationCallback, mLocalRepeat);
        }
        Log.d(TAG, "PLAYING URI: " + uri);
        mPlayer.playUri(mOperationCallback, uri, 0, 0);
    }

    private void pause()
    {
        mPlayer.pause(mOperationCallback);
    }

    private void resume()
    {
        mPlayer.resume(mOperationCallback);
    }

    private void next()
    {
        mPlayer.skipToNext(mOperationCallback);
    }

    private void toggleShuffle(boolean shuffle)
    {
        mLocalShuffle = shuffle;
        mPlayer.setShuffle(mOperationCallback, shuffle);
    }

    private void toggleRepeat(boolean repeat)
    {
        mLocalRepeat = repeat;
        mPlayer.setRepeat(mOperationCallback, repeat);
    }

    @Override
    public void onLoggedIn()
    {
        Log.d(TAG, "Logged into Spotify sucessfully");
    }

    @Override
    public void onLoggedOut()
    {

    }

    @Override
    public void onLoginFailed(Error error)
    {

    }

    @Override
    public void onTemporaryError()
    {

    }

    @Override
    public void onConnectionMessage(String s)
    {

    }

    @Override
    public void onPlaybackEvent(PlayerEvent playerEvent)
    {
        switch (playerEvent)
        {
            case kSpPlaybackNotifyTrackChanged:
                Log.d(TAG, "Track changed, updating album cover");
                mActivity.updateAlbumCover(mPlayer.getMetadata().currentTrack.albumCoverWebUrl);
                mActivity.updateSong(mPlayer.getMetadata().currentTrack.name, mPlayer.getMetadata().currentTrack.artistName);
                mActivity.speak("Playing " + mPlayer.getMetadata().currentTrack.name + " by " + mPlayer.getMetadata().currentTrack.artistName);
                break;
            case kSpPlaybackNotifyShuffleOn:
                Log.d(TAG, "Enabled shuffle");
                ((TextView) mActivity.findViewById(R.id.shuffle)).setText("Shuffle: True");
                mSpotifyShuffle = true;
                break;
            case kSpPlaybackNotifyShuffleOff:
                Log.d(TAG, "Disabled shuffle");
                ((TextView) mActivity.findViewById(R.id.shuffle)).setText("Shuffle: False");
                mSpotifyShuffle = false;
                break;
            case kSpPlaybackNotifyRepeatOn:
                Log.d(TAG, "Enabled repeat");
                ((TextView) mActivity.findViewById(R.id.repeat)).setText("Repeat: True");
                mSpotifyRepeat = true;
                break;
            case kSpPlaybackNotifyRepeatOff:
                Log.d(TAG, "Disabled repeat");
                ((TextView) mActivity.findViewById(R.id.repeat)).setText("Repeat: False");
                mSpotifyRepeat = false;
                break;

        }
    }

    @Override
    public void onPlaybackError(Error error)
    {
        // TODO handle error gracefully
    }
}
