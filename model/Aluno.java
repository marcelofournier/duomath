package br.com.amazonbots.duomath01.model;


import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.Exclude;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.io.Serializable;

import br.com.amazonbots.duomath01.dao.AlunoDAO;
import br.com.amazonbots.duomath01.tools.Base64Custom;
import br.com.amazonbots.duomath01.tools.DataFormatada;

public class Aluno implements Serializable {

        private int     id;
        private String  idUsuario;
        private String  nome;
        private String  email;
        //private String  senha;
        private int     meta;
        private int     pontos;
        private int     pontosSemana;
        private int     xp;
        private int     energia;
        private int     progresso;
        private int     nivel;
        private boolean bateuMetaPontos;
        private boolean bateuMetaAvaliacao;
        private String  hoje;
        private int     ofensiva;
        private int     operacao;
        private int     diaSemana;
        private int     treinosHoje;
        private boolean domingo;
        private boolean segunda;
        private boolean terca;
        private boolean quarta;
        private boolean quinta;
        private boolean sexta;
        private boolean sabado;
        private int avaliacoesSemana;
        private boolean robo;
        private int semanaCompeticao;
        private String token;


    //*******************************************************************************************

    public Aluno() {
        }

    //*******************************************************************************************

    public Aluno(String idUsuario, String nome, String email) {
        super();
        this.nome                   = nome;
        this.idUsuario              = idUsuario;
        this.meta                   = 30;
        this.email                  = email;
        //this.pontos                 = 0;
        //this.pontosSemana           = 0;
        //this.xp                     = 0;
        this.energia                = 5;
        this.progresso              = 0;
        this.nivel                  = 2;
        this.bateuMetaPontos     = false;
        this.bateuMetaAvaliacao  = false;
        this.hoje                   = DataFormatada.dataAtual();
        this.ofensiva               = 0;
        this.setOperacao(1);
        this.treinosHoje            = 0;
        this.avaliacoesSemana       = 0;
        this.robo                   = false;
        this.semanaCompeticao       = DataFormatada.semanaAno();
        this.recuperarToken();

    }

    //*******************************************************************************************


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean getRobo() {
        return robo;
    }

    public void setRobo(boolean robo) {
        this.robo = robo;
    }

    public int getAvaliacoesSemana() {
        return avaliacoesSemana;
    }

    public void setAvaliacoesSemana(int avaliacoesSemana) {
        this.avaliacoesSemana = avaliacoesSemana;
    }

    public int getTreinosHoje() {
        return treinosHoje;
    }

    public void setTreinosHoje(int treinosHoje) {
        this.treinosHoje = treinosHoje;
    }

    public int getXp() { return xp;  }

        public void setXp(int xp) { this.xp = xp; }

        public boolean isSegunda() {
            return segunda;
        }

        public void setSegunda(boolean segunda) {
            this.segunda = segunda;
        }

        public boolean isTerca() {
            return terca;
        }

        public void setTerca(boolean terca) {
            this.terca = terca;
        }

        public boolean isQuarta() {
            return quarta;
        }

        public void setQuarta(boolean quarta) {
            this.quarta = quarta;
        }

        public boolean isQuinta() {
            return quinta;
        }

        public void setQuinta(boolean quinta) {
            this.quinta = quinta;
        }

        public boolean isSexta() {
            return sexta;
        }

        public void setSexta(boolean sexta) {
            this.sexta = sexta;
        }

        public boolean isSabado() {
            return sabado;
        }

        public void setSabado(boolean sabado) {
            this.sabado = sabado;
        }

        public int getDiaSemana() {
            return diaSemana;
        }

        public boolean isDomingo() {
            return domingo;
        }

        public void setDomingo(boolean domingo) {
            this.domingo = domingo;
        }

        public void setDiaSemana(int diaSemana) {
            this.diaSemana = diaSemana;
        }

        public int getPontosSemana() {
            return pontosSemana;
        }

        public void setPontosSemana(int pontosSemana) {
            this.pontosSemana = pontosSemana;
        }

        @Exclude
        public String getIdUsuario() {
            return idUsuario;
        }

        public void setIdUsuario(String idUsuario) {
            this.idUsuario = idUsuario;
        }

        public int getOperacao() {
            return operacao;
        }

        public void setOperacao(int operacao) {
            this.operacao = operacao;
        }

        @Exclude
        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }


        public String getHoje() {
            return hoje;
        }

        public void setHoje(String hoje) {
            this.hoje = hoje;
        }

        public int getNivel() {
            return nivel;
        }

        public void setNivel(int nivel) {
            this.nivel = nivel;
        }

        public String getNome() {
            return nome;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }

        public int getMeta() {
            return meta;
        }

        public void setMeta(int meta) {
            this.meta = meta;
        }

        public int getOfensiva() {
            return ofensiva;
        }

        public void setOfensiva(int ofensiva) {
            this.ofensiva = ofensiva;
        }

        public int getPontos() {
            return pontos;
        }

        public void setPontos(int pontos) {
            this.pontos = pontos;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public int getProgresso() {
            return progresso;
        }

        public void setProgresso(int progresso) {
            this.progresso = progresso;
        }

        public int getEnergia() {
            return energia;
        }

        public void setEnergia(int energia) {
            this.energia = energia;
        }

        public boolean getBateuMetaAvaliacao() {
        return bateuMetaAvaliacao;
    }

    @Override
    public String toString() {
        return "Aluno{" +
                "nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", pontosSemana=" + pontosSemana +
                ", robo=" + robo +
                ", semanaCompeticao=" + semanaCompeticao +
                '}';
    }

    //*******************************************************************************************
    public void setBateuMetaAvaliacao(boolean bateuMetaAvaliacaoDia) {
        this.bateuMetaAvaliacao = bateuMetaAvaliacaoDia;
        this.setHoje(DataFormatada.dataAtual());
    }

    //*******************************************************************************************
    public boolean getBateuMetaPontos() {
        return bateuMetaPontos;
    }

    //*******************************************************************************************
    public void setBateuMetaPontos(boolean bateuMetaPontosDia) {
        this.bateuMetaPontos = bateuMetaPontosDia;
    }
    //*******************************************************************************************

        public boolean zerarDados(){

            try{
                this.setPontos(0);
                //this.setPontosSemana(0);
                this.setEnergia(5);
                this.setTreinosHoje(0);
                this.setProgresso(0);
                //this.setOfensiva(0);
                this.setHoje(DataFormatada.dataAtual());
                this.setBateuMetaPontos(false);
                this.setBateuMetaAvaliacao(false);
                this.setDiaSemana(DataFormatada.diaSemana());
                return true;
            }catch (Exception e){
                e.printStackTrace();
            }
                return false;
        }

    //*******************************************************************************************

        public void inicializarSemana(String nome, String email){

        /*
             private int     id;
        idUsuario;
        nome;
        email;
        meta;
        pontos;
        pontosSemana;
        xp;
        energia;
        progresso;
        nivel;
        bateuMetaPontos;
        bateuMetaAvaliacao;
        hoje;
        ofensiva;
        operacao;
        diaSemana;
        treinosHoje;
        domingo;
        segunda;
        terca;
        quarta;
        quinta;
        sexta;
        sabado;
        avaliacoesSemana;
        robo;
        semanaCompeticao;

         */


            try{
                this.nome = nome;
                this.email = email;
                this.meta = 30;
                this.setPontos(0);
                this.setPontosSemana(0);
                this.xp = this.xp + pontosSemana;
                this.setEnergia(5);
                this.setProgresso(0);
                this.nivel = 5;
                this.setBateuMetaPontos(false);
                this.setBateuMetaAvaliacao(false);
                this.setHoje(DataFormatada.dataAtual());
                this.setOfensiva(0);
                this.setOperacao(1);
                this.setDiaSemana(DataFormatada.diaSemana());
                this.setTreinosHoje(0);
                this.setDomingo(false);
                this.setSegunda(true);
                this.setTerca(false);
                this.setQuarta(false);
                this.setQuinta(false);
                this.setSexta(false);
                this.setSabado(false);
                this.setAvaliacoesSemana(0);
                this.robo = false;
                this.semanaCompeticao = DataFormatada.semanaAno();

                String chave = Base64Custom.codificarBase64(this.getEmail());
                int semana = DataFormatada.semanaAno();
                String s = String.valueOf(semana);
                this.cadastrar(chave, s);

            }catch (Exception e){
                e.printStackTrace();
            }
        }

    //*******************************************************************************************
            public void cadastrar(){
                AlunoDAO a = new AlunoDAO();
                this.getToken();
                a.cadastrar(this);
            }

            public void cadastrar(String chave, String semana){
                AlunoDAO a = new AlunoDAO();
                a.cadastrar(this, chave, semana);
            }

            public void remover(String chave, String semana){
                AlunoDAO dao = new AlunoDAO();
                dao.deletar(this);
            }

    //*******************************************************************************************

    public void inicializaDia(){

            int dia = DataFormatada.diaSemana();

            if (diaSemana != dia) {
                zerarDados();
                atualizar(DataFormatada.semanaAno());
                Log.i("INICIALIZAÇÃO", "Pontuação do dia zerada");

            }
    }

    //*******************************************************************************************
    public boolean atualizar(){
        try{
            AlunoDAO a = new AlunoDAO();
            a.atualizar(this);
            return true;

        }catch (Exception e)
        {
            return false;
        }
    }

    //*******************************************************************************************
    public boolean atualizar(int semana){

        try{
            AlunoDAO a = new AlunoDAO();
            a.atualizar(this, semana);
            Log.i("token", "aluno salvo: " + getToken());
            return true;

        }catch (Exception e)
        {
            return false;
        }
    }

    //*******************************************************************************************

    public void incrementaOfensiva(){
        this.setOfensiva(this.getOfensiva() + 1);
    }

//*******************************************************************************************

    public void recuperarToken(){


        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {

                String token = instanceIdResult.getToken();
                setToken(token);

                Log.i("token", "token gerado: " + token);
                Log.i("token", "token salvo no aluno: " + getToken());

            }
        });

    }



}

