package br.com.everis.notificacaobeacon.service;

import com.google.gson.JsonObject;

/**
 * Created by wgoncalv on 22/10/2017.
 */

public interface IUsuarioService {
    void buscarDadosUsuario(long idUsuario);
    void gravarUsuario(long idUsuario, long idReuniao, JsonObject dados);
}
