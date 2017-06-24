package br.desenvolvedor.michelatz.projetosam.Conversas;

/**
 * Created by Michel Atz on 23/06/2017.
 */

public class ConversaAtributos {

    private String nome;
    private String ultimaMensagem;
    private String hora;
    private String id;

    public ConversaAtributos() {

    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getUltimaMensagem() {
        return ultimaMensagem;
    }

    public void setUltimaMensagem(String ultimaMensagem) {
        this.ultimaMensagem = ultimaMensagem;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
