package br.com.amazonbots.duomath01.tools;

import android.icu.util.TimeUnit;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import static java.lang.Thread.sleep;

public class DataFormatada {

    public static String dataAtual(){

        long data = System.currentTimeMillis();
        SimpleDateFormat SimpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dataString = SimpleDateFormat.format(data);
        return dataString;
    }

    //*****************************************************************************


    public static String diaMesAnoDataEscolhida(String data){

        String[] retornoData = data.split("/");
        String dia = retornoData[0];
        String mes = retornoData[1];
        String ano = retornoData[2];
        String retorno = dia + mes + ano;
        return retorno;
    }

    //*****************************************************************************


    public static void aguarda(int segundos){

        try {
            Thread.sleep(segundos);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //*****************************************************************************


    public static  LocalDate transformaData(String dt){

        DateTimeFormatter formato = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            LocalDate data = LocalDate.parse(dt, formato);
            //System.out.println(data);
            return data;

        }
        return null;
    }

    //*****************************************************************************


        public static void Calendario() {
            GregorianCalendar gc = new GregorianCalendar();

            System.out.println("Ano: " + gc.get(Calendar.YEAR)); // Ano: 2016
            System.out.println("Mês: " + gc.get(Calendar.MONTH)); // Mês: 2
            System.out.println("Dia do mês: " + gc.get(Calendar.DAY_OF_MONTH)); // Dia do mês: 14
            System.out.println("Dia da semana: " + gc.get(Calendar.DAY_OF_WEEK)); // Dia da semana: 2
            System.out.println("Dia da ano: " + gc.get(Calendar.DAY_OF_YEAR)); // Dia da ano: 74
            System.out.println("Semana do ano: " + gc.get(Calendar.WEEK_OF_YEAR)); // Semana do ano: 12
            System.out.println("Semana do mês: " + gc.get(Calendar.WEEK_OF_MONTH)); // Semana do mês: 3
            System.out.println("Dia da semana do mês: " + gc.get(Calendar.DAY_OF_WEEK_IN_MONTH)); // Dia da semana do mês: 2
            System.out.println("Hora: " + gc.get(Calendar.HOUR)); // Hora: 1
            System.out.println("AM/PM? " + gc.get(Calendar.AM_PM)); // AM/PM? 1
            System.out.println("Hora do dia: " + gc.get(Calendar.HOUR_OF_DAY)); // Hora do dia: 13
            System.out.println("Minuto: " + gc.get(Calendar.MINUTE)); // Minuto: 19
            System.out.println("Segundo: " + gc.get(Calendar.SECOND)); // Segundo: 36
        }

    //*****************************************************************************


    public static int diaSemana(){
         GregorianCalendar gc = new GregorianCalendar();
         //gc.setFirstDayOfWeek(7);

         //Calendar currentCalendar = Calendar.getInstance(new Locale("en","UK"));
         return gc.get(Calendar.DAY_OF_WEEK);
     }

    //*****************************************************************************


    public static int semanaAno() {

        GregorianCalendar gc = new GregorianCalendar();
        return gc.get(Calendar.WEEK_OF_YEAR);

        /*
        System.out.println("Ano: " + gc.get(Calendar.YEAR)); // Ano: 2016
        System.out.println("Mês: " + gc.get(Calendar.MONTH)); // Mês: 2
        System.out.println("Dia do mês: " + gc.get(Calendar.DAY_OF_MONTH)); // Dia do mês: 14
        System.out.println("Dia da semana: " + gc.get(Calendar.DAY_OF_WEEK)); // Dia da semana: 2
        System.out.println("Dia da ano: " + gc.get(Calendar.DAY_OF_YEAR)); // Dia da ano: 74
        System.out.println("Semana do ano: " + gc.get(Calendar.WEEK_OF_YEAR)); // Semana do ano: 12
        System.out.println("Semana do mês: " + gc.get(Calendar.WEEK_OF_MONTH)); // Semana do mês: 3
        System.out.println("Dia da semana do mês: " + gc.get(Calendar.DAY_OF_WEEK_IN_MONTH)); // Dia da semana do mês: 2
        System.out.println("Hora: " + gc.get(Calendar.HOUR)); // Hora: 1
        System.out.println("AM/PM? " + gc.get(Calendar.AM_PM)); // AM/PM? 1
        System.out.println("Hora do dia: " + gc.get(Calendar.HOUR_OF_DAY)); // Hora do dia: 13
        System.out.println("Minuto: " + gc.get(Calendar.MINUTE)); // Minuto: 19
        System.out.println("Segundo: " + gc.get(Calendar.SECOND)); // Segundo: 36

         */
    }

    //*****************************************************************************

    public static int hora() {

        GregorianCalendar gc = new GregorianCalendar();
        return gc.get(Calendar.HOUR_OF_DAY);

    }

    //*****************************************************************************

    public static int minuto() {

        GregorianCalendar gc = new GregorianCalendar();
        return gc.get(Calendar.MINUTE);

    }

    //*****************************************************************************

    public static int semanaAnoAlterada(int iniciaSemana){

        GregorianCalendar gc = new GregorianCalendar();
        gc.setFirstDayOfWeek(iniciaSemana);
        return gc.get(Calendar.WEEK_OF_YEAR);

    }



}
