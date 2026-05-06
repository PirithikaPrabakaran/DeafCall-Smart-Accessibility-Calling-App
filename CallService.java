package com.example.deafcall;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;

import androidx.core.app.NotificationCompat;

import java.util.ArrayList;
import java.util.Locale;

public class CallService extends Service {

    SpeechRecognizer recognizer;
    Intent speechIntent;

    @Override
    public void onCreate() {
        super.onCreate();

        createNotification();

        recognizer = SpeechRecognizer.createSpeechRecognizer(this);

        speechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        recognizer.setRecognitionListener(new RecognitionListener() {

            @Override
            public void onResults(Bundle results) {
                ArrayList<String> data =
                        results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

                if (data != null && data.size() > 0) {
                    String text = data.get(0);
                    System.out.println("Live Text: " + text);
                }

                restart();
            }

            @Override
            public void onError(int error) {
                restart();
            }

            private void restart() {
                recognizer.cancel();
                recognizer.startListening(speechIntent);
            }

            // Required empty methods
            public void onReadyForSpeech(Bundle params) {}
            public void onBeginningOfSpeech() {}
            public void onRmsChanged(float rmsdB) {}
            public void onBufferReceived(byte[] buffer) {}
            public void onEndOfSpeech() {}
            public void onPartialResults(Bundle partialResults) {}
            public void onEvent(int eventType, Bundle params) {}
        });

        recognizer.startListening(speechIntent);
    }

    private void createNotification() {
        String channelId = "call_service";

        NotificationChannel channel = new NotificationChannel(
                channelId, "Call Service", NotificationManager.IMPORTANCE_LOW);

        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.createNotificationChannel(channel);

        Notification notification = new NotificationCompat.Builder(this, channelId)
                .setContentTitle("DeafCall Running")
                .setContentText("Converting call to text...")
                .setSmallIcon(android.R.drawable.ic_btn_speak_now)
                .build();

        startForeground(1, notification);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
