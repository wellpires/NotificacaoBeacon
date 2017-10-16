package br.com.everis.notificacaobeacon.model;

/**
 * Created by wgoncalv on 15/10/2017.
 */

public class CargoVO {

    private Long idCargo = null;
    private String cargo = null;

    public Long getIdCargo() {
        return idCargo;
    }

    public void setIdCargo(Long idCargo) {
        this.idCargo = idCargo;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CargoVO cargoVO = (CargoVO) o;

        if (idCargo != null ? !idCargo.equals(cargoVO.idCargo) : cargoVO.idCargo != null)
            return false;
        return cargo != null ? cargo.equals(cargoVO.cargo) : cargoVO.cargo == null;

    }

    @Override
    public int hashCode() {
        int result = idCargo != null ? idCargo.hashCode() : 0;
        result = 31 * result + (cargo != null ? cargo.hashCode() : 0);
        return result;
    }
}
