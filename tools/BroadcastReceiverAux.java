package br.com.amazonbots.duomath01.tools;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;
import android.view.View;

import androidx.core.app.NotificationCompat;

import java.util.Calendar;

import br.com.amazonbots.duomath01.R;
import br.com.amazonbots.duomath01.view.MainActivity;
import br.com.amazonbots.duomath01.view.NotificacaoActivity;

import static android.content.Context.NOTIFICATION_SERVICE;
import static androidx.core.content.ContextCompat.getSystemService;

public class BroadcastReceiverAux extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("Script", "-> Alarme");

        gerarNotificacao(context, new Intent(context, MainActivity.class ), "Nova mensagem", "Titulo", "Descricao");

    }



    public void gerarNotificacao(Context context, Intent intent, CharSequence ticker, CharSequence titulo, CharSequence descricao){

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setTicker(ticker);
        builder.setContentTitle(titulo);
        builder.setContentText(descricao);

        builder.setSmallIcon(R.drawable.ic_baseline_favorite_24);
        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_guri_pensando));
        builder.setContentIntent(pendingIntent);

        NotificationCompat.InboxStyle style = new NotificationCompat.InboxStyle();
        String[] descs = new String[] {"Acabei de lembrar que tá na hora ", "de praticar um pouquinho de matemática ",
                "e se transformar num verdadeiro campeão. ", "Vamos praticar?"};

        for (int i=0; i < descs.length; i++){
            style.addLine(descs[i]);

        }

        builder.setStyle(style);

        Notification n = builder.build();
        n.vibrate = new long[]{150, 300, 150, 600};
        n.flags = Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(R.drawable.ic_baseline_favorite_24, n);

        try{
            Uri som = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone toque = RingtoneManager.getRingtone(context, som);
            toque.play();

        }catch (Exception e){

        }

    }


    public void notificar(){

        // Set the alarm to start at approximately 2:00 p.m.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 22);

        // With setInexactRepeating(), you have to use one of the AlarmManager interval
        // constants--in this case, AlarmManager.INTERVAL_DAY.
        AlarmManager alarmMgr = null;
        PendingIntent alarmIntent = null;
        alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, alarmIntent);

    }




}
