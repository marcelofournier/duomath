package br.com.amazonbots.duomath01.dao;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import br.com.amazonbots.duomath01.model.Aluno;
import br.com.amazonbots.duomath01.model.Avaliacao;

public class AvaliacaoDAO {

    private final DatabaseReference referencia = FirebaseDatabase.getInstance().getReference();
    private final FirebaseAuth autenticacao = FirebaseAuth.getInstance();

    public boolean salvar(Avaliacao a) {

        try{
            DatabaseReference avaliacao = referencia.child( "avaliacoes" );
            avaliacao.push().setValue( a );

        }catch (Exception e){
            Log.e("INFO", "Erro ao salvar dados do avaliação " + e.getMessage() );
            return false;
        }
        return true;
    }




}
