package br.com.amazonbots.duomath01.model;

import br.com.amazonbots.duomath01.dao.AlunoDAO;
import br.com.amazonbots.duomath01.dao.UsuarioDAO;

public class Usuario {


    private String chave;
    private String nome;
    private String email;
    private String telefone;
    private String loginData;


    public Usuario(){
    }


    public String getChave() {
        return chave;
    }

    public void setChave(String chave) {
        this.chave = chave;
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

    public String getTelefone() {
        return telefone;
    }

    public String getLoginData() {
        return loginData;
    }

    public void setLoginData(String loginData) {
        this.loginData = loginData;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }



    public void cadastrar(){
        UsuarioDAO u = new UsuarioDAO();
        u.cadastrar(this);
    }



}
