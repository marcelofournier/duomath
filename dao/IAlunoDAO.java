package br.com.amazonbots.duomath01.dao;

import java.util.List;

import br.com.amazonbots.duomath01.model.Aluno;

public interface IAlunoDAO {


    boolean salvar(Aluno aluno);
    boolean atualizar(Aluno aluno);
    boolean deletar(Aluno aluno);
    List<Aluno> listar();



}
