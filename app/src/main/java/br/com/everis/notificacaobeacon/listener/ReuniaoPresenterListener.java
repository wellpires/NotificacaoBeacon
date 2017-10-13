package br.com.everis.notificacaobeacon.listener;

import java.util.List;

import br.com.everis.notificacaobeacon.model.ReuniaoVO;

/**
 * Created by wgoncalv on 12/10/2017.
 */

public interface ReuniaoPresenterListener {
    void reunioesReady(List<ReuniaoVO> lstReunioes);
    void reuniaoReady(ReuniaoVO reuniaoVO);
}
