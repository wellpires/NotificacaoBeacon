package br.com.everis.notificacaobeacon.model;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

/**
 * Created by wgoncalv on 16/10/2017.
 */

public class ArquivoVO extends RealmObject {

    @PrimaryKey
    private Long idArquivo = null;
    private String arquivo = null;
    private String caminhoArquivo = null;

    @Ignore
    private ReuniaoVO reuniao = null;

    public ArquivoVO(Long idArquivo, String caminhoArquivo) {
        this.idArquivo = idArquivo;
        this.caminhoArquivo = caminhoArquivo;
    }

    public ArquivoVO() {
    }

    public Long getIdArquivo() {
        return idArquivo;
    }

    public void setIdArquivo(Long idArquivo) {
        this.idArquivo = idArquivo;
    }

    public String getArquivo() {
        return arquivo;
    }

    public void setArquivo(String arquivo) {
        this.arquivo = arquivo;
    }

    public String getCaminhoArquivo() {
        return caminhoArquivo;
    }

    public void setCaminhoArquivo(String caminhoArquivo) {
        this.caminhoArquivo = caminhoArquivo;
    }

    public ReuniaoVO getReuniao() {
        return reuniao;
    }

    public void setReuniao(ReuniaoVO reuniao) {
        this.reuniao = reuniao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ArquivoVO arquivoVO = (ArquivoVO) o;

        if (idArquivo != null ? !idArquivo.equals(arquivoVO.idArquivo) : arquivoVO.idArquivo != null)
            return false;
        if (arquivo != null ? !arquivo.equals(arquivoVO.arquivo) : arquivoVO.arquivo != null)
            return false;
        return reuniao != null ? reuniao.equals(arquivoVO.reuniao) : arquivoVO.reuniao == null;
    }

    @Override
    public int hashCode() {
        int result = idArquivo != null ? idArquivo.hashCode() : 0;
        result = 31 * result + (arquivo != null ? arquivo.hashCode() : 0);
        result = 31 * result + (reuniao != null ? reuniao.hashCode() : 0);
        return result;
    }
}
