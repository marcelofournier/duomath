package br.com.amazonbots.duomath01.dao;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ConfiguracaoFirebase {

    private static DatabaseReference referenciaDatabase;
    private static FirebaseAuth referenciaAutenticacao;
    private static StorageReference referenciaStorage;
    private boolean firebaseOffline = true;



    //********************************************************************************
       //retorna a referencia do firebase
    public static DatabaseReference getFirebaseDatabase(){

        if ( referenciaDatabase == null  ) {
                referenciaDatabase = FirebaseDatabase.getInstance().getReference();
        }
        return referenciaDatabase;
    }

    //********************************************************************************
    //retorna a instancia do firebase auth
    public static FirebaseAuth getFirebaseAutenticacao(){

        if ( referenciaAutenticacao == null  ) {
            referenciaAutenticacao = FirebaseAuth.getInstance();
        }
        return referenciaAutenticacao;
    }
//********************************************************************************

    //retorna a instancia do firebase Storage
    public static StorageReference getFirebaseStorage(){

        if ( referenciaStorage == null  ) {
            referenciaStorage = FirebaseStorage.getInstance().getReference();
        }
        return referenciaStorage;
    }


    //********************************************************************************

    public void ativarFirebaseOffline(){

        try{

            if(!firebaseOffline){
                FirebaseDatabase.getInstance().setPersistenceEnabled(true);
                firebaseOffline = true;
            }

        }catch (Exception e){



        }

    }

    //********************************************************************************


}
