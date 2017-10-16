package br.com.everis.notificacaobeacon.listener;

import java.util.List;

import br.com.everis.notificacaobeacon.model.PermissaoVO;

/**
 * Created by wgoncalv on 15/10/2017.
 */

public interface PermissaoPresenterListener {

    void permissaoReady(List<PermissaoVO> lstPermissao);

}
