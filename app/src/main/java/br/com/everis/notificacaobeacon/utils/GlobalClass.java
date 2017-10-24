package br.com.everis.notificacaobeacon.utils;

import android.app.Application;

import br.com.everis.notificacaobeacon.model.ReuniaoVO;
import io.branch.referral.Branch;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by wgoncalv on 19/09/2017.
 */

public class GlobalClass extends Application {

    private ReuniaoVO reuniaoVO = null;
    private Boolean isReuniaoAcontecera = false;
    private Boolean isReuniaoAcontecendo = false;

    public ReuniaoVO getReuniaoVO() {
        return reuniaoVO;
    }

    public void setReuniaoVO(ReuniaoVO reuniaoVO) {
        this.reuniaoVO = reuniaoVO;
    }

    public Boolean getReuniaoAcontecera() {
        return isReuniaoAcontecera;
    }

    public void setReuniaoAcontecera(Boolean reuniaoAcontecera) {
        isReuniaoAcontecera = reuniaoAcontecera;
    }

    public Boolean getReuniaoAcontecendo() {
        return isReuniaoAcontecendo;
    }

    public void setReuniaoAcontecendo(Boolean reuniaoAcontecendo) {
        isReuniaoAcontecendo = reuniaoAcontecendo;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        RealmConfiguration config = new RealmConfiguration.Builder(this).deleteRealmIfMigrationNeeded().build();
        Realm.setDefaultConfiguration(config);

        // Branch logging for debugging
        Branch.enableLogging();

        // Branch object initialization
        Branch.getAutoInstance(this);

    }

}
