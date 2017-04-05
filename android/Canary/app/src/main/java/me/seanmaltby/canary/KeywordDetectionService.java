package me.seanmaltby.canary;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;

import edu.cmu.pocketsphinx.*;

public class KeywordDetectionService extends Service implements RecognitionListener
{
    private static final String TAG = "KD_Service";

    private static final String KWS_SEARCH = "keyword";
    static final int MSG_RECOGNIZER_START_LISTENING = 1;
    static final int MSG_RECOGNIZER_CANCEL = 2;

    private SpeechRecognizer mSpeechRecognizer;
    private Messenger mActivityMessenger;
    private final Messenger mServiceMessenger = new Messenger(new IncomingHandler(this));

    private boolean mIsListening;

    @Override
    public void onCreate()
    {
        super.onCreate();
        Log.d(TAG, "Created KeywordDetectionService");
        try
        {
            Assets assets = new Assets(KeywordDetectionService.this);
            File assetsDir = assets.syncAssets();
            mSpeechRecognizer = SpeechRecognizerSetup.defaultSetup()
                    .setAcousticModel(new File(assetsDir, "en-us-ptm"))
                    .setDictionary(new File(assetsDir, "cmudict-en-us.dict"))
                    .setKeywordThreshold(1e-45f)
                    .getRecognizer();
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        mSpeechRecognizer.addListener(this);
        mSpeechRecognizer.addKeyphraseSearch(KWS_SEARCH, "ok canary");
        Log.d(TAG, "Initialized speech recognizer");
    }

    private static class IncomingHandler extends Handler
    {
        private WeakReference<KeywordDetectionService> mTarget;

        IncomingHandler(KeywordDetectionService target)
        {
            mTarget = new WeakReference<>(target);
        }

        @Override
        public void handleMessage(Message msg)
        {
            final KeywordDetectionService target = mTarget.get();

			/* The first message to start listening should be from the MainActivity and contain a replyTo.
				Subsequent messages should not contain a replyTo, and in case they do, they are ignored */
            if(msg.replyTo != null && target.mActivityMessenger == null)
                target.mActivityMessenger = msg.replyTo;

            switch (msg.what)
            {
                case MSG_RECOGNIZER_START_LISTENING:
                    if (!target.mIsListening)
                    {
                        target.mSpeechRecognizer.startListening(KWS_SEARCH);
                        target.mIsListening = true;
                        Log.d(TAG, "Message started recognizer");
                    }
                    break;

                case MSG_RECOGNIZER_CANCEL:
                    if (target.mIsListening)
                    {
                        target.mSpeechRecognizer.cancel();
                        target.mIsListening = false;
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
            mSpeechRecognizer.cancel();
            mSpeechRecognizer.shutdown();
        }
    }

    @Override
    public void onBeginningOfSpeech()
    {
        Log.d(TAG, "onBeginingOfSpeech");
    }

    @Override
    public void onEndOfSpeech()
    {
        Log.d(TAG, "onEndOfSpeech");
        mSpeechRecognizer.startListening(KWS_SEARCH);
    }

    @Override
    public void onPartialResult(Hypothesis hypothesis)
    {
        if (hypothesis != null)
        {
            mSpeechRecognizer.stop();
        }
    }

    @Override
    public void onResult(Hypothesis hypothesis)
    {
        mIsListening = false;
        Message message = Message.obtain(null, MainActivity.MSG_KEYWORD_DETECTED);
        try
        {
            mActivityMessenger.send(message);
        } catch (RemoteException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(Exception e)
    {
        mIsListening = false;
        Log.d(TAG, "error = " + e.getMessage());
    }

    @Override
    public void onTimeout()
    {
        mSpeechRecognizer.startListening(KWS_SEARCH);
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return mServiceMessenger.getBinder();
    }
}
