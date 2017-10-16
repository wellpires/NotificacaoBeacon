package br.com.everis.notificacaobeacon.model;

/**
 * Created by wgoncalv on 16/10/2017.
 */

public class UsuarioVO {

    private Long idUsuario = null;
    private String usuario = null;
    private String senha = null;
    private String nomeCompleto = null;
    private String email = null;
    private PermissaoVO permissao = null;
    private CargoVO cargoVO = null;

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

    public PermissaoVO getPermissao() {
        return permissao;
    }

    public void setPermissao(PermissaoVO permissao) {
        this.permissao = permissao;
    }

    public CargoVO getCargoVO() {
        return cargoVO;
    }

    public void setCargoVO(CargoVO cargoVO) {
        this.cargoVO = cargoVO;
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
        if (permissao != null ? !permissao.equals(usuarioVO.permissao) : usuarioVO.permissao != null)
            return false;
        return cargoVO != null ? cargoVO.equals(usuarioVO.cargoVO) : usuarioVO.cargoVO == null;

    }

    @Override
    public int hashCode() {
        int result = idUsuario != null ? idUsuario.hashCode() : 0;
        result = 31 * result + (usuario != null ? usuario.hashCode() : 0);
        result = 31 * result + (senha != null ? senha.hashCode() : 0);
        result = 31 * result + (nomeCompleto != null ? nomeCompleto.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (permissao != null ? permissao.hashCode() : 0);
        result = 31 * result + (cargoVO != null ? cargoVO.hashCode() : 0);
        return result;
    }
}
