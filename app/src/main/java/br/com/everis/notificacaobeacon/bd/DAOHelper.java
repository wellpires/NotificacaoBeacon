package br.com.everis.notificacaobeacon.bd;

import io.realm.Realm;
import io.realm.RealmModel;
import io.realm.RealmObject;

/**
 * Created by wgoncalv on 17/10/2017.
 */

public class DAOHelper<E> {

    private Realm realm = null;

    public boolean open(){
        if(realm.isInTransaction()){
            close();
            return false;
        }
        realm.beginTransaction();
        return true;
    }

    public void close(){
        if(realm.isInTransaction()){
            realm.commitTransaction();
        }
    }

    public DAOHelper() {
        this.realm = Realm.getDefaultInstance();
    }

    public void insert(E entity) {
        if(!open()){
            return;
        }
        realm.copyToRealm((RealmObject) entity);
        close();
    }

    public E selectOne(Class<? extends RealmModel> clazz){
        E result = (E) realm.where(clazz).findFirst();
        return result;
    }
}
