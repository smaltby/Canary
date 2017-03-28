package me.seanmaltby.canary;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.*;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.List;

public class SpeechRecognitionService extends Service implements RecognitionListener
{
    private static final String TAG = "SR_Service";

    static final int MSG_RECOGNIZER_START_LISTENING = 1;
    static final int MSG_RECOGNIZER_CANCEL = 2;

    private SpeechRecognizer mSpeechRecognizer;
    private AudioManager mAudioManager;
    private Intent mSpeechRecognizerIntent;
    private Messenger mActivityMessenger;
    private final Messenger mServiceMessenger = new Messenger(new IncomingHandler(this));
    private Handler timeoutHandler = new Handler();

    private boolean mIsListening;
    private int mMusicVolume;

    @Override
    public void onCreate()
    {
        super.onCreate();
        Log.d(TAG, "Created SpeechRecognitionService");
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        mSpeechRecognizer.setRecognitionListener(this);
        mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,
                this.getPackageName());
    }

    static class IncomingHandler extends Handler
    {
        private WeakReference<SpeechRecognitionService> mTarget;

        IncomingHandler(SpeechRecognitionService target)
        {
            mTarget = new WeakReference<>(target);
        }

        @Override
        public void handleMessage(Message msg)
        {
            final SpeechRecognitionService target = mTarget.get();

			/* The first message to start listening should be from the MainActivity and contain a replyTo.
				Subsequent messages should not contain a replyTo, and in case they do, they are ignored */
            if(msg.replyTo != null && target.mActivityMessenger == null)
                target.mActivityMessenger = msg.replyTo;

            switch (msg.what)
            {
                case MSG_RECOGNIZER_START_LISTENING:
                    if (!target.mIsListening)
                    {
                        target.mSpeechRecognizer.startListening(target.mSpeechRecognizerIntent);
                        Log.d(TAG, "Message started recognizer");
                    }
                    break;

                case MSG_RECOGNIZER_CANCEL:
                    if (target.mIsListening)
                    {
                        target.mSpeechRecognizer.cancel();
                        target.mIsListening = false;
                        target.mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, target.mMusicVolume, 0);
                        Log.d(TAG, "Message cancelled recognizer");
                    }
                    break;
            }
        }
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        if (mSpeechRecognizer != null)
        {
            mSpeechRecognizer.destroy();
        }
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mMusicVolume, 0);
    }

    @Override
    public void onBeginningOfSpeech()
    {
        Log.d(TAG, "onBeginingOfSpeech");
        timeoutHandler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onBufferReceived(byte[] buffer)
    {

    }

    @Override
    public void onEndOfSpeech()
    {
        mIsListening = false;
        Log.d(TAG, "onEndOfSpeech");
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mMusicVolume, 0);
    }

    @Override
    public void onError(int error)
    {
        mIsListening = false;
        Message message = Message.obtain(null, MainActivity.MSG_ERROR_ON_INPUT);
        Bundle errorBundle = new Bundle();
        errorBundle.putString("error", "error I didn't quite catch that");
        message.setData(errorBundle);
        try
        {
            mActivityMessenger.send(message);
        } catch (RemoteException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onEvent(int eventType, Bundle params)
    {

    }

    @Override
    public void onPartialResults(Bundle partialResults)
    {

    }

    @Override
    public void onReadyForSpeech(Bundle params)
    {
        if(mIsListening)
            return;
        mIsListening = true;
        Log.d(TAG, "onReadyForSpeech");
        mMusicVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);

        timeoutHandler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                if(mIsListening)
                {
                    Log.d(TAG, "SpeechRecognizer timed out");
                    mSpeechRecognizer.cancel();
                    mIsListening = false;
                    mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mMusicVolume, 0);
                }
            }
        }, 2000);
    }

    @Override
    public void onResults(Bundle results)
    {
        Log.d(TAG, "onResults");
        List<String> data = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        assert data != null;

        Message message = Message.obtain(null, MainActivity.MSG_VOICE_INPUT);
        message.setData(results);
        try
        {
            mActivityMessenger.send(message);
        } catch (RemoteException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onRmsChanged(float rmsdB)
    {

    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return mServiceMessenger.getBinder();
    }
}
