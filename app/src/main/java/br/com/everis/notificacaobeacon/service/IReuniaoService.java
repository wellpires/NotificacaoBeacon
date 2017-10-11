package br.com.everis.notificacaobeacon.service;

import java.io.IOException;

import javax.inject.Singleton;

import br.com.everis.notificacaobeacon.bd.model.ReuniaoVO;
import br.com.everis.notificacaobeacon.module.ReuniaoModule;
import dagger.Component;

/**
 * Created by wgoncalv on 11/10/2017.
 */


public interface IReuniaoService{
    void gravar(ReuniaoVO reuniao) throws IOException;
}
