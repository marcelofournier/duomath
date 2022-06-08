package br.com.amazonbots.duomath01.tools;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Arquivo {

    public static void copiarArquivo(String arquivo1, String arquivo2) throws IOException {
        FileReader in = null;
        FileWriter out = null;

        try {
            in = new FileReader(arquivo1);
            out = new FileWriter(arquivo2);

            int c;
            while ((c = in.read()) != -1) {
                out.write(c);
            }
        }finally {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
        }
    }


    public static ArrayList<String> leArquivoLinhaLinha(String arquivo){

        ArrayList<String> lista = null;

        BufferedReader buffRead = null;
        try {
            buffRead = new BufferedReader(new FileReader(arquivo));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String linha = "";
                while (true) {
                    if (linha != null) {
                        //System.out.println(linha);
                        lista.add(linha);

                    } else
                        break;
                    try {
                        linha = buffRead.readLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        try {
            buffRead.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return lista;
    }



}