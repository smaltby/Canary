package me.seanmaltby.canary;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import edu.cmu.pocketsphinx.Config;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.media.audiofx.AcousticEchoCanceler;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import edu.cmu.pocketsphinx.Config;
import edu.cmu.pocketsphinx.Decoder;
import edu.cmu.pocketsphinx.FsgModel;
import edu.cmu.pocketsphinx.Hypothesis;
import edu.cmu.pocketsphinx.RecognitionListener;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;

public class SpeechRecognizerNoEcho {
    protected static final String TAG = SpeechRecognizerNoEcho.class.getSimpleName();
    private final Decoder decoder;
    private final int sampleRate;
    private static final float BUFFER_SIZE_SECONDS = 0.4F;
    private int bufferSize;
    private final AudioRecord recorder;
    private Thread recognizerThread;
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private final Collection<RecognitionListener> listeners = new HashSet();

    protected SpeechRecognizerNoEcho(Config config) throws IOException {
        this.decoder = new Decoder(config);
        this.sampleRate = (int)this.decoder.getConfig().getFloat("-samprate");
        this.bufferSize = AudioRecord.getMinBufferSize(this.sampleRate, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT) * 2;
        this.recorder = new AudioRecord(MediaRecorder.AudioSource.VOICE_COMMUNICATION, this.sampleRate, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, this.bufferSize);
        if(this.recorder.getState() == 0) {
            this.recorder.release();
            throw new IOException("Failed to initialize recorder. Microphone might be already in use.");
        }
    }

    public void addListener(RecognitionListener listener) {
        Collection var2 = this.listeners;
        synchronized(this.listeners) {
            this.listeners.add(listener);
        }
    }

    public void removeListener(RecognitionListener listener) {
        Collection var2 = this.listeners;
        synchronized(this.listeners) {
            this.listeners.remove(listener);
        }
    }

    public boolean startListening(String searchName) {
        if(null != this.recognizerThread) {
            return false;
        } else {
            Log.i(TAG, String.format("Start recognition \"%s\"", new Object[]{searchName}));
            this.decoder.setSearch(searchName);
            this.recognizerThread = new SpeechRecognizerNoEcho.RecognizerThread();
            this.recognizerThread.start();
            return true;
        }
    }

    public boolean startListening(String searchName, int timeout) {
        if(null != this.recognizerThread) {
            return false;
        } else {
            Log.i(TAG, String.format("Start recognition \"%s\"", new Object[]{searchName}));
            this.decoder.setSearch(searchName);
            this.recognizerThread = new SpeechRecognizerNoEcho.RecognizerThread(timeout);
            this.recognizerThread.start();
            return true;
        }
    }

    private boolean stopRecognizerThread() {
        if(null == this.recognizerThread) {
            return false;
        } else {
            try {
                this.recognizerThread.interrupt();
                this.recognizerThread.join();
            } catch (InterruptedException var2) {
                Thread.currentThread().interrupt();
            }

            this.recognizerThread = null;
            return true;
        }
    }

    public boolean stop() {
        boolean result = this.stopRecognizerThread();
        if(result) {
            Log.i(TAG, "Stop recognition");
            Hypothesis hypothesis = this.decoder.hyp();
            this.mainHandler.post(new SpeechRecognizerNoEcho.ResultEvent(hypothesis, true));
        }

        return result;
    }

    public boolean cancel() {
        boolean result = this.stopRecognizerThread();
        if(result) {
            Log.i(TAG, "Cancel recognition");
        }

        return result;
    }

    public Decoder getDecoder() {
        return this.decoder;
    }

    public void shutdown() {
        this.recorder.release();
    }

    public String getSearchName() {
        return this.decoder.getSearch();
    }

    public void addFsgSearch(String searchName, FsgModel fsgModel) {
        this.decoder.setFsg(searchName, fsgModel);
    }

    public void addGrammarSearch(String name, File file) {
        Log.i(TAG, String.format("Load JSGF %s", new Object[]{file}));
        this.decoder.setJsgfFile(name, file.getPath());
    }

    public void addGrammarSearch(String name, String jsgfString) {
        this.decoder.setJsgfString(name, jsgfString);
    }

    public void addNgramSearch(String name, File file) {
        Log.i(TAG, String.format("Load N-gram model %s", new Object[]{file}));
        this.decoder.setLmFile(name, file.getPath());
    }

    public void addKeyphraseSearch(String name, String phrase) {
        this.decoder.setKeyphrase(name, phrase);
    }

    public void addKeywordSearch(String name, File file) {
        this.decoder.setKws(name, file.getPath());
    }

    public void addAllphoneSearch(String name, File file) {
        this.decoder.setAllphoneFile(name, file.getPath());
    }

    private class TimeoutEvent extends SpeechRecognizerNoEcho.RecognitionEvent {
        private TimeoutEvent() {
            super();
        }

        protected void execute(RecognitionListener listener) {
            listener.onTimeout();
        }
    }

    private class OnErrorEvent extends SpeechRecognizerNoEcho.RecognitionEvent {
        private final Exception exception;

        OnErrorEvent(Exception exception) {
            super();
            this.exception = exception;
        }

        protected void execute(RecognitionListener listener) {
            listener.onError(this.exception);
        }
    }

    private class ResultEvent extends SpeechRecognizerNoEcho.RecognitionEvent {
        protected final Hypothesis hypothesis;
        private final boolean finalResult;

        ResultEvent(Hypothesis hypothesis, boolean finalResult) {
            super();
            this.hypothesis = hypothesis;
            this.finalResult = finalResult;
        }

        protected void execute(RecognitionListener listener) {
            if(this.finalResult) {
                listener.onResult(this.hypothesis);
            } else {
                listener.onPartialResult(this.hypothesis);
            }

        }
    }

    private class InSpeechChangeEvent extends SpeechRecognizerNoEcho.RecognitionEvent {
        private final boolean state;

        InSpeechChangeEvent(boolean state) {
            super();
            this.state = state;
        }

        protected void execute(RecognitionListener listener) {
            if(this.state) {
                listener.onBeginningOfSpeech();
            } else {
                listener.onEndOfSpeech();
            }

        }
    }

    private abstract class RecognitionEvent implements Runnable {
        private RecognitionEvent() {
        }

        public void run() {
            RecognitionListener[] emptyArray = new RecognitionListener[0];
            RecognitionListener[] var2 = (RecognitionListener[]) SpeechRecognizerNoEcho.this.listeners.toArray(emptyArray);
            int var3 = var2.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                RecognitionListener listener = var2[var4];
                this.execute(listener);
            }

        }

        protected abstract void execute(RecognitionListener var1);
    }

    private final class RecognizerThread extends Thread {
        private int remainingSamples;
        private int timeoutSamples;
        private static final int NO_TIMEOUT = -1;

        public RecognizerThread(int timeout) {
            if(timeout != -1) {
                this.timeoutSamples = timeout * SpeechRecognizerNoEcho.this.sampleRate / 1000;
            } else {
                this.timeoutSamples = -1;
            }

            this.remainingSamples = this.timeoutSamples;
        }

        public RecognizerThread() {
            this(-1);
        }

        public void run() {
            SpeechRecognizerNoEcho.this.recorder.startRecording();
            if(SpeechRecognizerNoEcho.this.recorder.getRecordingState() == 1) {
                SpeechRecognizerNoEcho.this.recorder.stop();
                IOException buffer1 = new IOException("Failed to start recording. Microphone might be already in use.");
                SpeechRecognizerNoEcho.this.mainHandler.post(SpeechRecognizerNoEcho.this.new OnErrorEvent(buffer1));
            } else {
                Log.d(SpeechRecognizerNoEcho.TAG, "Starting decoding");
                SpeechRecognizerNoEcho.this.decoder.startUtt();
                short[] buffer = new short[SpeechRecognizerNoEcho.this.bufferSize];
                boolean inSpeech = SpeechRecognizerNoEcho.this.decoder.getInSpeech();
                SpeechRecognizerNoEcho.this.recorder.read(buffer, 0, buffer.length);

                while(!interrupted() && (this.timeoutSamples == -1 || this.remainingSamples > 0)) {
                    int nread = SpeechRecognizerNoEcho.this.recorder.read(buffer, 0, buffer.length);
                    if(-1 == nread) {
                        throw new RuntimeException("error reading audio buffer");
                    }

                    if(nread > 0) {
                        SpeechRecognizerNoEcho.this.decoder.processRaw(buffer, (long)nread, false, false);
                        if(SpeechRecognizerNoEcho.this.decoder.getInSpeech() != inSpeech) {
                            inSpeech = SpeechRecognizerNoEcho.this.decoder.getInSpeech();
                            SpeechRecognizerNoEcho.this.mainHandler.post(SpeechRecognizerNoEcho.this.new InSpeechChangeEvent(inSpeech));
                        }

                        if(inSpeech) {
                            this.remainingSamples = this.timeoutSamples;
                        }

                        Hypothesis hypothesis = SpeechRecognizerNoEcho.this.decoder.hyp();
                        SpeechRecognizerNoEcho.this.mainHandler.post(SpeechRecognizerNoEcho.this.new ResultEvent(hypothesis, false));
                    }

                    if(this.timeoutSamples != -1) {
                        this.remainingSamples -= nread;
                    }
                }

                SpeechRecognizerNoEcho.this.recorder.stop();
                SpeechRecognizerNoEcho.this.decoder.endUtt();
                SpeechRecognizerNoEcho.this.mainHandler.removeCallbacksAndMessages((Object)null);
                if(this.timeoutSamples != -1 && this.remainingSamples <= 0) {
                    SpeechRecognizerNoEcho.this.mainHandler.post(SpeechRecognizerNoEcho.this.new TimeoutEvent());
                }

            }
        }
    }
}