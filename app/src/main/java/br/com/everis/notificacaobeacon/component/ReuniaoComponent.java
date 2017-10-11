package br.com.everis.notificacaobeacon.component;

import javax.inject.Singleton;

import br.com.everis.notificacaobeacon.module.ReuniaoModule;
import br.com.everis.notificacaobeacon.service.impl.ReuniaoServiceImpl;
import dagger.Component;

/**
 * Created by wgoncalv on 11/10/2017.
 */

@Singleton
@Component(modules = {ReuniaoModule.class})
public interface ReuniaoComponent {

    ReuniaoServiceImpl provideReuniaoService();

}
