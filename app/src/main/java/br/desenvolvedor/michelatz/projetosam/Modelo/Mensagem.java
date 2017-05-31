package br.desenvolvedor.michelatz.projetosam.Modelo;

public class Mensagem {

    private String id;
    private String texto;
    private String nome;
    //private String email;
    private String idUsuario;
    private String idServico;

    public Mensagem() {

    }

    public Mensagem(String id, String texto, String nome, String idUsuario, String idServico) {
        this.id = id;
        this.texto = texto;
        this.nome = nome;
        this.idUsuario = idUsuario;
        this.idServico = idServico;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getIdServico() {
        return idServico;
    }

    public void setIdServico(String idServico) {
        this.idServico = idServico;
    }
}
