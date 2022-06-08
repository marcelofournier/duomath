package br.com.amazonbots.duomath01.dao;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import br.com.amazonbots.duomath01.model.Aluno;
import br.com.amazonbots.duomath01.model.Classificacao;

public class ClassificacaoDAO {

        private final DatabaseReference referencia = FirebaseDatabase.getInstance().getReference();
        //private final FirebaseAuth autenticacao = FirebaseAuth.getInstance();

        //****************************************************************************

        public ClassificacaoDAO() {
        }

         public boolean salvar(Classificacao classificacao) {

            try{
                DatabaseReference classificacaoRef = referencia.child( "classificacao" );
                classificacaoRef.push().setValue( classificacao );

            }catch (Exception e){
                Log.e("INFO", "Erro ao salvar dados da classificacao " + e.getMessage() );
                return false;
            }
            return true;
        }


    //****************************************************************************

    public void cadastrar(Classificacao classificacao){

            DatabaseReference c = referencia.child( "classificacao" );
            c.child(classificacao.getEmail())
                    .setValue( classificacao );
            //push()
        }

        //****************************************************************************

        public void cadastrar(Aluno aluno, String chave, String semana){

            DatabaseReference alunos = referencia.child( "alunos" );
            alunos.child(semana)
                    .child(chave)
                    .setValue( aluno );
            //push()
        }


    //****************************************************************************

    }
