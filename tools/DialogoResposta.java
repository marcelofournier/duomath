package br.com.amazonbots.duomath01.tools;

import android.content.DialogInterface;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class DialogoResposta extends AppCompatActivity {

    //atributo da classe.
    private AlertDialog alertaX;
    private int resposta = 0;

    public int alerta(String titulo, String msg, boolean botaoPositivo, boolean botaoNegativo, boolean botaoOk) {
        //Cria o gerador do AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //define o titulo
        builder.setTitle(titulo);
        //define a mensagem
        builder.setMessage(msg);
        //define um botão como positivo
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                //Toast.makeText(getApplicationContext() , "positivo=" + arg1, Toast.LENGTH_SHORT).show();
                resposta = 1;
            }
        });
        //define um botão como negativo.
        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                //Toast.makeText(getApplicationContext(), "negativo=" + arg1, Toast.LENGTH_SHORT).show();
                resposta = 2;
            }
        });
        //cria o AlertDialog
        alertaX = builder.create();
        //Exibe
        alertaX.show();

        return resposta;
    }

}
