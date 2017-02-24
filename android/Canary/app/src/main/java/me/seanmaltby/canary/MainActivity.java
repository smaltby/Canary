package me.seanmaltby.canary;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;

import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.Spotify;
import com.spotify.sdk.android.player.SpotifyPlayer;

import java.lang.ref.WeakReference;
import java.util.List;

import static com.spotify.sdk.android.authentication.LoginActivity.REQUEST_CODE;

public class MainActivity extends Activity
{
    private static final String TAG = "MainActivity";

    static final int MSG_VOICE_INPUT = 1;
    static final int MSG_ERROR_ON_INPUT = 3;

    private Messenger mActivityMessenger = new Messenger(new IncomingHandler(this));
    private Messenger mSpeechRecognitionMessenger;
    private final ServiceConnection mSpeechRecognitionConnection = new ServiceConnection()
    {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service)
        {
            Log.d(TAG, "onServiceConnected");
            mSpeechRecognitionMessenger = new Messenger(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name)
        {
            Log.d(TAG, "onServiceDisconnected");
            mSpeechRecognitionMessenger = null;
        }
    };

    private static final String CLIENT_ID = "3e189d315fa64c97abdeaf9f855815e3";
    private static final String REDIRECT_URI = "canary://callback";
    private SpotifyPlayer mPlayer;
    private String mAccessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spotifyLogin();
    }

    private void spotifyLogin()
    {
        AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(CLIENT_ID,
                AuthenticationResponse.Type.TOKEN,
                REDIRECT_URI);
        builder.setScopes(new String[]{"user-read-private", "user-read-email", "playlist-read-private",
                "playlist-read-collaborative", "playlist-modify-public", "playlist-modify-private",
                "streaming", "user-follow-modify", "user-follow-read", "user-library-read",
                "user-library-modify", "user-read-private", "user-read-birthdate", "user-read-email",
                "user-top-read"});
        AuthenticationRequest request = builder.build();

        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        super.onActivityResult(requestCode, resultCode, intent);

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE)
        {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);
            mAccessToken = response.getAccessToken();

            switch (response.getType())
            {
                // Response was successful and contains auth token
                case TOKEN:
                    Log.d(TAG, "Response contains auth token");
                    Config playerConfig = new Config(this, response.getAccessToken(), CLIENT_ID);
                    Spotify.getPlayer(playerConfig, this, new SpotifyPlayer.InitializationObserver()
                    {
                        @Override
                        public void onInitialized(SpotifyPlayer player)
                        {
                            Log.d(TAG, "Initialized player");
                            mPlayer = player;
                            initialize();
                        }

                        @Override
                        public void onError(Throwable throwable)
                        {
                            Log.e(TAG, "Could not initialize player: " + throwable.getMessage());
                        }
                    });
                    break;
                case ERROR:
                    Log.d(TAG, "Auth flow returned an error");
                    break;
                default:
                    Log.d(TAG, "Auth flow likely cancelled");
            }
        }
    }

    private void initialize()
    {
        // Initialize native bridge
        NativeBridge.init(mPlayer);

        bindService(new Intent(this, SpeechRecognitionService.class), mSpeechRecognitionConnection, Context.BIND_AUTO_CREATE);
        Log.d(TAG, "Binded services");
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }

    public native void parse(String command, String accessToken);
    static
    {
        System.loadLibrary("main");
    }

    public void listen(View view)
    {
        Message message = Message.obtain(null, SpeechRecognitionService.MSG_RECOGNIZER_START_LISTENING);
        message.replyTo = mActivityMessenger;
        try
        {
            mSpeechRecognitionMessenger.send(message);
        } catch (RemoteException e)
        {
            e.printStackTrace();
        }
    }

    static class IncomingHandler extends Handler
    {
        private WeakReference<MainActivity> mTarget;

        IncomingHandler(MainActivity target)
        {
            mTarget = new WeakReference<>(target);
        }

        @Override
        public void handleMessage(Message msg)
        {
            final MainActivity target = mTarget.get();

            switch (msg.what)
            {
                case MSG_VOICE_INPUT:
                    List<String> data = msg.getData().getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                    assert data != null;
                    Log.d(TAG, "We got some voice input: " + data.get(0));
                    target.parse(data.get(0), target.mAccessToken);
                    break;
                case MSG_ERROR_ON_INPUT:
                    Log.d(TAG, "Error on voice input");
                    break;
            }
        }
    }
}
