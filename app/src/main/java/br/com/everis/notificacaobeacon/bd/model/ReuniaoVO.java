package br.com.everis.notificacaobeacon.bd.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by wgoncalv on 18/09/2017.
 */

public class ReuniaoVO implements Serializable {

    private Integer id = null;
    private String assunto = null;
    private String horaInicio = null;
    private String horaTermino = null;
    private String local = null;
    private String sala = null;
    private String participantes = null;
    private String detalhes = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAssunto() {
        return assunto;
    }

    public void setAssunto(String assunto) {
        this.assunto = assunto;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getHoraTermino() {
        return horaTermino;
    }

    public void setHoraTermino(String horaTermino) {
        this.horaTermino = horaTermino;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getSala() {
        return sala;
    }

    public void setSala(String sala) {
        this.sala = sala;
    }

    public String getParticipantes() {
        return participantes;
    }

    public void setParticipantes(String participantes) {
        this.participantes = participantes;
    }

    public String getDetalhes() {
        return detalhes;
    }

    public void setDetalhes(String detalhes) {
        this.detalhes = detalhes;
    }

    @Override
    public String toString() {
        return "ReuniaoVO{" +
                "id=" + id +
                ", assunto='" + assunto + '\'' +
                ", horaInicio=" + horaInicio +
                ", horaTermino=" + horaTermino +
                ", local='" + local + '\'' +
                ", sala='" + sala + '\'' +
                ", participantes='" + participantes + '\'' +
                ", detalhes='" + detalhes + '\'' +
                '}';
    }
}
