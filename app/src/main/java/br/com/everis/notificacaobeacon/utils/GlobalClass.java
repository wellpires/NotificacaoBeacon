package br.com.everis.notificacaobeacon.utils;

import android.app.Application;

import br.com.everis.notificacaobeacon.bd.model.ReuniaoVO;

/**
 * Created by wgoncalv on 19/09/2017.
 */

public class GlobalClass extends Application {

    private ReuniaoVO reuniaoVO = null;

    public ReuniaoVO getReuniaoVO() {
        return reuniaoVO;
    }

    public void setReuniaoVO(ReuniaoVO reuniaoVO) {
        this.reuniaoVO = reuniaoVO;
    }
}
