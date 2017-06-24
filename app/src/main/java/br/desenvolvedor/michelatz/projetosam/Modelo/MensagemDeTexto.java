package br.desenvolvedor.michelatz.projetosam.Modelo;

/**
 * Created by Michel Atz on 12/06/2017.
 */

public class MensagemDeTexto {

    private String id;
    private String mensagem;
    private int tipoMmensagem;
    private String horaDaMensagem;

    public MensagemDeTexto() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public int getTipoMmensagem() {
        return tipoMmensagem;
    }

    public void setTipoMmensagem(int tipoMmensagem) {
        this.tipoMmensagem = tipoMmensagem;
    }

    public String getHoraDaMensagem() {
        return horaDaMensagem;
    }

    public void setHoraDaMensagem(String horaDaMensagem) {
        this.horaDaMensagem = horaDaMensagem;
    }
}
