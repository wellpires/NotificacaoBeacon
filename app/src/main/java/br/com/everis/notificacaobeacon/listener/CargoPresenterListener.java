package br.com.everis.notificacaobeacon.listener;

import java.util.List;

import br.com.everis.notificacaobeacon.model.CargoVO;

/**
 * Created by wgoncalv on 15/10/2017.
 */

public interface CargoPresenterListener {

    void cargosReady(List<CargoVO> lstCargos);

}
