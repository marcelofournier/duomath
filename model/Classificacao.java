package br.com.amazonbots.duomath01.model;

public class Classificacao {

    //private int semana;
    private String chaveAluno;
    private String nome;
    private String email;
    private int pontosSemana;


    public Classificacao() {
    }

    public Classificacao(String chaveAluno) {
        this.chaveAluno = chaveAluno;
    }

    public Classificacao(String chaveAluno, String nome, String email, int pontosSemana) {
        this.chaveAluno = chaveAluno;
        this.nome = nome;
        this.email = email;
        this.pontosSemana = pontosSemana;
    }

    //***************************************************************************************


    public String getChaveAluno() {
        return chaveAluno;
    }

    public void setChaveAluno(String chaveAluno) {
        this.chaveAluno = chaveAluno;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getPontosSemana() {
        return pontosSemana;
    }

    public void setPontosSemana(int pontosSemana) {
        this.pontosSemana = pontosSemana;
    }

    //***************************************************************************************

    @Override
    public String toString() {
        return "Classificacao{" +
                "chaveAluno='" + chaveAluno + '\'' +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", pontosSemana=" + pontosSemana +
                '}';
    }
}
