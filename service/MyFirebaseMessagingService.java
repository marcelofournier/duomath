package br.com.amazonbots.duomath01.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import br.com.amazonbots.duomath01.R;
import br.com.amazonbots.duomath01.view.LoginActivity;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage notificacao) {
        super.onMessageReceived(notificacao);

        if ( notificacao != null ) {
            String titulo = notificacao.getNotification().getTitle();
            String corpo = notificacao.getNotification().getBody();

            enviarNotificacao(titulo, corpo);

        }


    }

    @Override
    public void onNewToken(@NonNull String s) {

        super.onNewToken(s);


    }

    private void enviarNotificacao(String titulo, String corpo){

        Uri uriSon = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        String canal = getString(R.string.default_notification_channel_id);

        Intent intent = new Intent (this, LoginActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT );

        NotificationCompat.Builder notificacao = new NotificationCompat.Builder(this, canal)
                .setContentTitle(titulo)
                .setContentText(corpo)
                .setSmallIcon(R.drawable.ic_trofeu_verde_24)
                .setSound(uriSon)
                .setAutoCancel(true)
                .setContentIntent( pendingIntent );


        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel = new NotificationChannel(canal, "canal", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel( channel );

        }

        notificationManager.notify(0, notificacao.build());

    }


}
