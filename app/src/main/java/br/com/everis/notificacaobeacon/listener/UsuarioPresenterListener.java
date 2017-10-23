package br.com.everis.notificacaobeacon.listener;

import com.google.gson.JsonArray;

import br.com.everis.notificacaobeacon.exception.RestException;

/**
 * Created by wgoncalv on 22/10/2017.
 */

public interface UsuarioPresenterListener {

    void usuarioReady(JsonArray dados);
    void usuarioReady();
    void usuarioFailed(RestException e);

}
