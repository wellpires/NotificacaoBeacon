package br.com.everis.notificacaobeacon.model;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

/**
 * Created by wgoncalv on 16/10/2017.
 */

public class UsuarioVO extends RealmObject {

    @PrimaryKey
    private Long idUsuario = null;
    private String usuario = null;
    private String senha = null;
    private String nomeCompleto = null;
    private String email = null;
    private Long permissaoFK = null;
    private Long cargoFK = null;

    @Ignore
    private PermissaoVO permissao = null;

    @Ignore
    private CargoVO cargo = null;

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getPermissaoFK() {
        return permissaoFK;
    }

    public void setPermissaoFK(Long permissaoFK) {
        this.permissaoFK = permissaoFK;
    }

    public Long getCargoFK() {
        return cargoFK;
    }

    public void setCargoFK(Long cargoFK) {
        this.cargoFK = cargoFK;
    }

    public PermissaoVO getPermissao() {
        return permissao;
    }

    public void setPermissao(PermissaoVO permissao) {
        this.permissao = permissao;
    }

    public CargoVO getCargo() {
        return cargo;
    }

    public void setCargo(CargoVO cargo) {
        this.cargo = cargo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UsuarioVO usuarioVO = (UsuarioVO) o;

        if (idUsuario != null ? !idUsuario.equals(usuarioVO.idUsuario) : usuarioVO.idUsuario != null)
            return false;
        if (usuario != null ? !usuario.equals(usuarioVO.usuario) : usuarioVO.usuario != null)
            return false;
        if (senha != null ? !senha.equals(usuarioVO.senha) : usuarioVO.senha != null) return false;
        if (nomeCompleto != null ? !nomeCompleto.equals(usuarioVO.nomeCompleto) : usuarioVO.nomeCompleto != null)
            return false;
        if (email != null ? !email.equals(usuarioVO.email) : usuarioVO.email != null) return false;
        if (permissaoFK != null ? !permissaoFK.equals(usuarioVO.permissaoFK) : usuarioVO.permissaoFK != null)
            return false;
        if (cargoFK != null ? !cargoFK.equals(usuarioVO.cargoFK) : usuarioVO.cargoFK != null)
            return false;
        if (permissao != null ? !permissao.equals(usuarioVO.permissao) : usuarioVO.permissao != null)
            return false;
        return cargo != null ? cargo.equals(usuarioVO.cargo) : usuarioVO.cargo == null;

    }

    @Override
    public int hashCode() {
        int result = idUsuario != null ? idUsuario.hashCode() : 0;
        result = 31 * result + (usuario != null ? usuario.hashCode() : 0);
        result = 31 * result + (senha != null ? senha.hashCode() : 0);
        result = 31 * result + (nomeCompleto != null ? nomeCompleto.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (permissaoFK != null ? permissaoFK.hashCode() : 0);
        result = 31 * result + (cargoFK != null ? cargoFK.hashCode() : 0);
        result = 31 * result + (permissao != null ? permissao.hashCode() : 0);
        result = 31 * result + (cargo != null ? cargo.hashCode() : 0);
        return result;
    }
}
