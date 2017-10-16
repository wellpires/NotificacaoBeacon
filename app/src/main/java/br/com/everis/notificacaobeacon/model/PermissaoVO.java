package br.com.everis.notificacaobeacon.model;

/**
 * Created by wgoncalv on 15/10/2017.
 */

public class PermissaoVO {

    private Long idPermissao = null;
    private String permissao = null;

    public Long getIdPermissao() {
        return idPermissao;
    }

    public void setIdPermissao(Long idPermissao) {
        this.idPermissao = idPermissao;
    }

    public String getPermissao() {
        return permissao;
    }

    public void setPermissao(String permissao) {
        this.permissao = permissao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PermissaoVO that = (PermissaoVO) o;
        if (idPermissao != null ? !idPermissao.equals(that.idPermissao) : that.idPermissao != null)
            return false;
        return permissao != null ? permissao.equals(that.permissao) : that.permissao == null;
    }

    @Override
    public int hashCode() {
        int result = idPermissao != null ? idPermissao.hashCode() : 0;
        result = 31 * result + (permissao != null ? permissao.hashCode() : 0);
        return result;
    }
}
