package br.com.everis.notificacaobeacon.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by wgoncalv on 18/09/2017.
 */

public class ReuniaoVO extends RealmObject {

    @PrimaryKey
    private Integer idReuniao = null;
    private String assunto = null;
    private Date dtInicio = null;
    private Date dtTermino = null;
    private String endereco = null;
    private Double latitude = null;
    private Double longitude = null;
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

    public Date getDtInicio() {
        return dtInicio;
    }

    public void setDtInicio(Date dtInicio) {
        this.dtInicio = dtInicio;
    }

    public Date getDtTermino() {
        return dtTermino;
    }

    public void setDtTermino(Date dtTermino) {
        this.dtTermino = dtTermino;
    }

    public String getEndereco_OLD() {
        return endereco;
    }

    public void setEndereco_OLD(String endereco) {
        this.endereco = endereco;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
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
