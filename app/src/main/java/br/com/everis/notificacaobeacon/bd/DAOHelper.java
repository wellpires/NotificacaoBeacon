package br.com.everis.notificacaobeacon.bd;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmModel;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;

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

    public Long getNextId(Class<? extends RealmModel> clazz){
        String nomeCampo = "";
        Long nextId = 1L;
        for (Method method : clazz.getMethods()) {
            if(method.getName().startsWith("setId")){
                nomeCampo = method.getName().substring(3);
                nomeCampo = nomeCampo.substring(0,1).toLowerCase() + nomeCampo.substring(1);
            }
        }
        if(realm.where(clazz).max(nomeCampo) != null){
            nextId = realm.where(clazz).max(nomeCampo).longValue() + 1L;
        }
        return nextId;
    }

    public void insert(E entity) {
        if(!open()){
            return;
        }
        realm.copyToRealm((RealmObject) entity);
        close();
    }

    public void deleteAll(Class<? extends RealmModel> clazz){
        if(!open()){
            return;
        }
        realm.delete(clazz);
        close();
    }

    public void deleteAll(){
        if(!open()){
            return;
        }
        realm.deleteAll();
        close();
    }

    public E find(Class<? extends RealmModel> clazz){
        E result = (E) realm.where(clazz).findFirst();
        return result;
    }
    
    public List<E> findAll(Class<? extends RealmModel> clazz){
        List<E> results = (List<E>) realm.where(clazz).findAll();
        return results;
    }

    public List<E> detachFromRealm(List<E> entities){
        return (List<E>) realm.copyFromRealm((Iterable<? extends RealmModel>) entities);
    }

    public E detachFromRealm(E object){
        return (E) realm.copyFromRealm((RealmModel) object);
    }
}
