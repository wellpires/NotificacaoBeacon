package br.com.everis.notificacaobeacon.model;

/**
 * Created by wgoncalv on 15/10/2017.
 */

public class QualificacaoVO {

    private Long idQualificacao = null;
    private String qualificacao = null;
    private String instituicao = null;

    public Long getIdQualificacao() {
        return idQualificacao;
    }

    public void setIdQualificacao(Long idQualificacao) {
        this.idQualificacao = idQualificacao;
    }

    public String getQualificacao() {
        return qualificacao;
    }

    public void setQualificacao(String qualificacao) {
        this.qualificacao = qualificacao;
    }

    public String getInstituicao() {
        return instituicao;
    }

    public void setInstituicao(String instituicao) {
        this.instituicao = instituicao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        QualificacaoVO that = (QualificacaoVO) o;

        if (idQualificacao != null ? !idQualificacao.equals(that.idQualificacao) : that.idQualificacao != null)
            return false;
        if (qualificacao != null ? !qualificacao.equals(that.qualificacao) : that.qualificacao != null)
            return false;
        return instituicao != null ? instituicao.equals(that.instituicao) : that.instituicao == null;

    }

    @Override
    public int hashCode() {
        int result = idQualificacao != null ? idQualificacao.hashCode() : 0;
        result = 31 * result + (qualificacao != null ? qualificacao.hashCode() : 0);
        result = 31 * result + (instituicao != null ? instituicao.hashCode() : 0);
        return result;
    }
}
