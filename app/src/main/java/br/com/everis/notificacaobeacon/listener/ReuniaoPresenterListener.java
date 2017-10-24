package br.com.everis.notificacaobeacon.listener;

import com.google.gson.JsonArray;

import org.json.JSONArray;

import java.util.List;

import br.com.everis.notificacaobeacon.exception.RestException;
import br.com.everis.notificacaobeacon.model.ReuniaoVO;

/**
 * Created by wgoncalv on 12/10/2017.
 */

public interface ReuniaoPresenterListener {
    void reunioesReady(List<ReuniaoVO> lstReunioes);
    void reuniaoReady(ReuniaoVO reuniaoVO);
    void reuniaoReady(JsonArray usuarios);
    void reuniaoReady();

    void reuniaoFailed(RestException exception);
}
