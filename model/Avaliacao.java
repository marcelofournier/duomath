package br.com.amazonbots.duomath01.model;

import br.com.amazonbots.duomath01.tools.DataFormatada;

public class Avaliacao {

    private String email;
    private String nome;
    private float nota;
    private String comentario;
    private String data;

    public Avaliacao() {
    }

    public Avaliacao(String nome, String email, float nota, String comentario){

        this.email = email;
        this.nome = nome;
        this.nota = nota;
        this.comentario = comentario;
        this.data = DataFormatada.dataAtual();
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public float getNota() {
        return nota;
    }

    public void setNota(float nota) {
        this.nota = nota;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Avaliacao{" +
                "email='" + email + '\'' +
                ", nome='" + nome + '\'' +
                ", nota=" + nota +
                ", comentario='" + comentario + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}
