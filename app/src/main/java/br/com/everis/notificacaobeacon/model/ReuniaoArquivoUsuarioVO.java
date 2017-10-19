package br.com.everis.notificacaobeacon.model;

import java.util.List;

public class ReuniaoArquivoUsuarioVO {

    private ReuniaoVO reuniao = null;
    private List<ArquivoVO> listArquivos = null;
    private List<UsuarioVO> listUsuarios = null;

    public ReuniaoVO getReuniao() {
        return reuniao;
    }

    public void setReuniao(ReuniaoVO reuniao) {
        this.reuniao = reuniao;
    }

    public List<ArquivoVO> getListArquivos() {
        return listArquivos;
    }

    public void setListArquivos(List<ArquivoVO> listArquivos) {
        this.listArquivos = listArquivos;
    }

    public List<UsuarioVO> getListUsuarios() {
        return listUsuarios;
    }

    public void setListUsuarios(List<UsuarioVO> listUsuarios) {
        this.listUsuarios = listUsuarios;
    }
}
