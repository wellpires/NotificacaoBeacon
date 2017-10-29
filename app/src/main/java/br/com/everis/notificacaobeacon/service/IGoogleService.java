package br.com.everis.notificacaobeacon.service;

import java.io.IOException;

/**
 * Created by wgoncalv on 27/10/2017.
 */

public interface IGoogleService {

    Integer buscarTempoDistancia(String origins, String destinations) throws IOException;

}
