package br.com.everis.notificacaobeacon.model;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by wgoncalv on 18/09/2017.
 */

public class ReuniaoVO extends RealmObject {

    @PrimaryKey
    private Integer idReuniao = null;
    private String assunto = null;
    private String dtInicio = null;
    private String dtTermino = null;
    private String endereco = null;
    private String sala = null;
    private String pauta = null;

    public ReuniaoVO(Integer idReuniao) {
        this.idReuniao = idReuniao;
    }

    public ReuniaoVO() {
    }

    public Integer getIdReuniao() {
        return idReuniao;
    }

    public void setIdReuniao(Integer idReuniao) {
        this.idReuniao = idReuniao;
    }

    public String getAssunto() {
        return assunto;
    }

    public void setAssunto(String assunto) {
        this.assunto = assunto;
    }

    public String getDtInicio() {
        return dtInicio;
    }

    public void setDtInicio(String dtInicio) {
        this.dtInicio = dtInicio;
    }

    public String getDtTermino() {
        return dtTermino;
    }

    public void setDtTermino(String dtTermino) {
        this.dtTermino = dtTermino;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getSala() {
        return sala;
    }

    public void setSala(String sala) {
        this.sala = sala;
    }

    public String getPauta() {
        return pauta;
    }

    public void setPauta(String pauta) {
        this.pauta = pauta;
    }

}
