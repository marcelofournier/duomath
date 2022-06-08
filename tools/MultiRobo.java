package br.com.amazonbots.duomath01.tools;

import android.util.Log;
import android.widget.Toast;

public class MultiRobo extends Thread{


    public void iniciarRobo(int robos){

        MultiRobo multiRobo = new MultiRobo();
        new Thread( multiRobo ).start();
        //multiRobo.start();

    }



    class MyRunnable implements Runnable{

        @Override
        public void run() {

            for (int i=1; i<=15; i++){

                Log.d("Thread", "Contador + " + i);

                try{

                    Thread.sleep(1000);
                }catch (Exception e){
                    e.printStackTrace();
                }

            }

        }
    }


}
