package br.com.amazonbots.duomath01.tools;

public class StringTools {


    //*********************************************************************
    public static String retornaNome(String email){
        return email.replace("@teste.com", "");
    }

    //*********************************************************************

    public static String retornaEmail(String nome){
        return nome + "@teste.com";
    }

    //*********************************************************************

    public static String retornaSobrenome(String email){

        int begin = email.indexOf(".");
        int end = email.lastIndexOf("@");

        return email.substring(begin, end);
    }

    //************************************************************************

    public static Object primeiraMaiuscula(String nome){

        char[] palavras = nome.toCharArray();

        for(int i = 1; i < palavras.length; i++) {
            //convertendo todas as letras para minúsculo para casos como tEsTe = teste
            if(Character.isAlphabetic(palavras[i])) {
                palavras[i] = Character.toLowerCase(palavras[i]);
            }
            //se o carácter anterior for espaço então o atual sera maiúsculo
            if(Character.isWhitespace(palavras[i - 1])) {
                palavras[i] = Character.toUpperCase(palavras[i]);
            }
        }
        //por fim a primeira letra de toda frase ou palavra será maiúscula
        palavras[0] = Character.toUpperCase(palavras[0]);

        //retorna o Array de char como String
        String nomeConvertido = new String(palavras);

        return nomeConvertido;


    }
//*********************************************************************

}
