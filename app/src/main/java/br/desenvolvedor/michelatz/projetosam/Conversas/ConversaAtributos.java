package br.desenvolvedor.michelatz.projetosam.Conversas;

/**
 * Created by Michel Atz on 23/06/2017.
 */

public class ConversaAtributos {

    private String nomeServicoReceptor;
    private String ultimaMensagem;
    private String hora;
    private String idServico;
    private String idReceptor;
    private String idMensagem;

    public ConversaAtributos() {

    }

    public String getNomeServicoReceptor() {
        return nomeServicoReceptor;
    }

    public void setNomeServicoReceptor(String nomeServicoReceptor) {
        this.nomeServicoReceptor = nomeServicoReceptor;
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

    public String getIdServico() {
        return idServico;
    }

    public void setIdServico(String idServico) {
        this.idServico = idServico;
    }

    public String getIdReceptor() {
        return idReceptor;
    }

    public void setIdReceptor(String idReceptor) {
        this.idReceptor = idReceptor;
    }

    public String getIdMensagem() {
        return idMensagem;
    }

    public void setIdMensagem(String idMensagem) {
        this.idMensagem = idMensagem;
    }
}
