package br.com.amazonbots.duomath01.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.core.view.View;
import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide;
import com.heinrichreimersoftware.materialintro.slide.SimpleSlide;

import br.com.amazonbots.duomath01.R;

public class IntroducaoActivity extends IntroActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_introducao);

        setButtonBackVisible(false);
        setButtonNextVisible(false);

        addSlide(new SimpleSlide.Builder()
                        .title("Seja bem vindo!!!")
                        .description("Muito legal ter você aqui.")
                        .image(R.drawable.slider_um)
                        .background(android.R.color.holo_blue_light)
                        .build()
        );

/*
        addSlide(new SimpleSlide.Builder()
                .title("Vamos jogar?")
                .description("Descricao")
                .image(R.drawable.slider_dois)
                .background(android.R.color.holo_blue_light)
                .build()
        );

        addSlide(new SimpleSlide.Builder()
                .title("Chame seus amigos")
                .description("Descricao")
                .image(R.drawable.slider_tres)
                .background(android.R.color.holo_blue_light)
                .build()
        );

 */
        addSlide(new SimpleSlide.Builder()
                .title("Você vai ser um campeão")
                .description("Descricao")
                .image(R.drawable.slider_quatro)
                .background(android.R.color.white)
                .canGoForward(true)
                .build()
        );


        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.fragment_novo )
                .canGoForward(false)
                .build());
    }


    public void btCadastrar(View view){
        startActivity(new Intent(this, LoginActivity.class));

        //Intent intent = new Intent(this, CadastroActivity.class);
        //startActivity(intent);
        //startActivity(new Intent(this, CadastroActivity.class));
    }

    public void btEntrar(View view){
        startActivity(new Intent(this, LoginActivity.class));
    }





}