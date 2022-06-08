package br.com.amazonbots.duomath01.tools;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Relogio {


    public String mostraRelogio() throws IOException {

        String retorno = "";

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        scheduler.scheduleAtFixedRate(
                new Runnable() {
                    public void run() {

                        System.out.println(new SimpleDateFormat("HH:mm:ss").format(new Date()));
                        //String relogio = new SimpleDateFormat("HH:mm:ss").format(new Date());

                    }
                }, 1, 1, TimeUnit.SECONDS);

        return retorno;
    }
}