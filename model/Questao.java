package br.com.amazonbots.duomath01.model;

import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class Questao  {

    private int     nivel;
    private int     operacao;
    private int     segundoMembro;
    private String  pergunta;
    private String  descricao;
    private int     respostaCorreta;
    private int     opcao2;
    private int     opcao3;


    public Questao(int nivel) {
        super();
        this.nivel = nivel;
        this.setSegundoMembro(this.geraAleatorio(11));
        this.setRespostaCorreta(this.getNivel() * this.getSegundoMembro());
        this.setPergunta(this.getNivel() + " X " + this.getSegundoMembro());
        this.setDescricao(this.getNivel() + " X " + this.getSegundoMembro() + " = " + this.getRespostaCorreta());
        //this.setOpcao2(this.getRespostaCorreta() + geraAleatorio(9));
        //this.setOpcao3(this.getRespostaCorreta() - geraAleatorio(9));
        this.geraOpcoes();
    }


    public Questao(int nivel, int operacao) {
        super();
        this.nivel = nivel;
        this.operacao = operacao;
        this.setSegundoMembro(this.geraAleatorio(11));
        this.trataOperador(operacao);

        //this.setRespostaCorreta(this.getNivel() + operacao +  this.getSegundoMembro());
        //this.setPergunta(this.getNivel() + " " + String.valueOf(operacao) + " " + this.getSegundoMembro());
        //this.setDescricao(this.getNivel() + " " + String.valueOf(operacao) + " " + this.getSegundoMembro() + " = " + this.getRespostaCorreta());
        //this.setOpcao2(this.getRespostaCorreta() + geraAleatorio(5));
        //this.setOpcao3(this.getRespostaCorreta() - geraAleatorio(9));

        /*
        this.setRespostaCorreta(this.getNivel() * this.getSegundoMembro());
        this.setPergunta(this.getNivel() + " X " + this.getSegundoMembro());
        this.setDescricao(this.getNivel() + " X " + this.getSegundoMembro() + " = " + this.getRespostaCorreta());
        //this.setOpcao2(this.getRespostaCorreta() + geraAleatorio(9));
        //this.setOpcao3(this.getRespostaCorreta() - geraAleatorio(9));
         */

        this.geraOpcoes();

    }



    public int getOperacao() {
        return operacao;
    }

    public void setOperacao(int operacao) {
        this.operacao = operacao;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public int getSegundoMembro() {
        return segundoMembro;
    }

    public void setSegundoMembro(int segundoMembro) {
        this.segundoMembro = segundoMembro;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getRespostaCorreta() {
        return respostaCorreta;
    }

    public void setRespostaCorreta(int respostaCorreta) {
        this.respostaCorreta = respostaCorreta;
    }

    public int getOpcao2() {
        return opcao2;
    }

    public void setOpcao2(int opcao2) {
        this.opcao2 = opcao2;
    }

    public int getOpcao3() {
        return opcao3;
    }

    public void setOpcao3(int opcao3) {
        this.opcao3 = opcao3;
    }

    public String getPergunta() {
        return pergunta;
    }

    public void setPergunta(String pergunta) {
        this.pergunta = pergunta;
    }


    @Override
    public String toString() {
        return "Questao [nivel=" + nivel + ", segundoMembro=" + segundoMembro + ", descricao=" + descricao
                + ", respostaCorreta=" + respostaCorreta + ", opcao2=" + opcao2 + ", opcao3=" + opcao3 + "]";
    }

    public String toStringSimplificado() {
        return descricao + "  [1] " + respostaCorreta + "  [2] " + opcao2 + "  [3] " + opcao3;
    }


    public int geraAleatorio(int valor) {

        int numero = (int)(Math.random() * valor);
        return numero;
    }

    public int geraAleatorio(int max, int min)  {

        int numero = (int)(Math.random() * max) + min;
        return numero;
    }


    /*



     */
    public void geraOpcoes() {

        do {
            this.setOpcao2(this.geraAleatorio(10) + this.getRespostaCorreta() + 1);
        } while ((this.getOpcao2() == this.getRespostaCorreta()) );

        do {
            this.setOpcao3(this.getRespostaCorreta() - this.geraAleatorio(10) + 1);
        } while ( this.getOpcao3() == this.getOpcao2() );


        if(this.getOpcao2()==this.getRespostaCorreta()){
            this.setOpcao2(this.getOpcao2() + 4);
        }

        if(this.getOpcao3()==this.getRespostaCorreta()){
            this.setOpcao3(this.getOpcao3() + 8);
        }


    }



    public void geraOpcoes(int operacao) {

        do {
            this.setOpcao2(this.geraAleatorio(10) + this.getRespostaCorreta() + 1);
        } while ((this.getOpcao2() == this.getRespostaCorreta()) );

        do {
            this.setOpcao3(this.getRespostaCorreta() - this.geraAleatorio(10) + 1);
        } while ( this.getOpcao3() == this.getOpcao2() );


        if(this.getOpcao2()==this.getRespostaCorreta()){
            this.setOpcao2(this.getOpcao2() + 4);
        }

        if(this.getOpcao3()==this.getRespostaCorreta()){
            this.setOpcao3(this.getOpcao3() + 8);
        }
    }



    public void trataOperador(int o){

        //this.setRespostaCorreta(this.getNivel() + operacao +  this.getSegundoMembro());
        //this.setPergunta(this.getNivel() + " " + String.valueOf(operacao) + " " + this.getSegundoMembro());
        //this.setDescricao(this.getNivel() + " " + String.valueOf(operacao) + " " + this.getSegundoMembro() + " = " + this.getRespostaCorreta());

        switch ( o ) {

            case 1:
                this.setRespostaCorreta(this.getNivel() + this.getSegundoMembro());
                this.setPergunta(this.getNivel() + " + " + this.getSegundoMembro());
                this.setDescricao(this.getNivel() + " + " +  this.getSegundoMembro() + " = " + this.getRespostaCorreta());

                break;

            case 2:
                questaoSubtracao();

                break;

            case 3:
                this.setRespostaCorreta(this.getNivel() * this.getSegundoMembro());
                this.setPergunta(this.getNivel() + " x " + this.getSegundoMembro());
                this.setDescricao(this.getNivel() + " x " +  this.getSegundoMembro() + " = " + this.getRespostaCorreta());

                break;

            case 4:
                questaoDivisao();
                break;
        }

    }


    public void questaoSubtracao(){

        int max = 15;
        this.setSegundoMembro(geraAleatorio(max, this.getNivel()));
        this.setRespostaCorreta(this.getSegundoMembro() - this.getNivel());
        this.setPergunta(this.getSegundoMembro() + " - " + this.getNivel() + "?");
        this.setDescricao( this.getSegundoMembro() + " - " + this.getNivel() + " = " + this.getRespostaCorreta() );

    }


    public void questaoDivisao(){

        int numero = geraAleatorio(10) + 1;
        int inverso = numero * this.getNivel();
        this.setSegundoMembro(inverso);
        this.setRespostaCorreta(inverso / getNivel());
        this.setPergunta(inverso + " / " + getNivel() );
        this.setDescricao(inverso + " / " + this.getNivel() + " = " + this.getRespostaCorreta() );

    }




}
