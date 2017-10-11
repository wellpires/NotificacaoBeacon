package br.com.everis.notificacaobeacon.module;

import javax.inject.Singleton;

import br.com.everis.notificacaobeacon.service.impl.ReuniaoServiceImpl;
import dagger.Module;
import dagger.Provides;

/**
 * Created by wgoncalv on 11/10/2017.
 */

@Module
public class ReuniaoModule {

    @Provides
    @Singleton
    ReuniaoServiceImpl provideReuniaoService(){
        return new ReuniaoServiceImpl();
    }
}
