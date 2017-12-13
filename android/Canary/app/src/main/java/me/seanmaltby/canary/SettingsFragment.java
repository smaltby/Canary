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
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.Spotify;
import com.spotify.sdk.android.player.SpotifyPlayer;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Locale;

import static com.spotify.sdk.android.authentication.LoginActivity.REQUEST_CODE;

public class MainActivity extends Activity
{
    // Activity initialization
    private static final String TAG = "SettingsActivity";

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
    private SpotifyHandler mHandler;
    private String mAccessToken;
    private TextToSpeech mTextToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mTextToSpeech=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener()
        {
            @Override
            public void onInit(int status)
            {
                mTextToSpeech.setLanguage(Locale.US);
            }
        });
    }

    // Initialize speech service for recording keyword for keyword activitation setting
    // Initialize shared preferences for saving all settings
    private void initialize(Settings setting)
    {
        bindService(new Intent(this, SpeechRecognitionService.class), mSpeechRecognitionConnection, Context.BIND_AUTO_CREATE);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        pref.setApplicationSettings(setting);
        Editor editor = pref.edit();
        Log.d(TAG, "Binded service");
    }

    public void openSettings()
    {
      Settings setting = new Settings();
      setting = getApplicationSettings();
      initialize(setting);
      Log.d(TAG, "Settings initialized");
    }

    @Override
    protected void onDestroy()
    {
        unbindService(mSpeechRecognitionConnection);
        super.onDestroy();
    }

    // Listen to for voice keyword activation setting
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

    // On all settings changes, update settings with values from the fragment
    public void updateSettings(boolean feedback, int sensitivity, boolean keywordOrTouch, String keyword)
    {
      // Set key-value pairs for settings editor
      editor.putBoolean("feedback", feedback);
      editor.putInt("sensitivity", sensitivity);
      editor.putBoolean("keywordOrTouch", keywordOrTouch);
      editor.putString("keyword", keyword);
      editor.putLong("key_name", "long value");

      editor.commit(); // commit changes
    }


    // Speaking for setting keyword activation
    public void speak(String text)
    {
        mTextToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }
}
