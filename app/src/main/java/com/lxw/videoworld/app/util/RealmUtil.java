package com.lxw.videoworld.app.util;

import com.lxw.videoworld.app.model.SourceCollectModel;
import com.lxw.videoworld.app.model.SourceHistoryModel;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 *
 * Created by Zion on 2018/4/17.
 */

public class RealmUtil {

    private static Realm mRealm = Realm.getDefaultInstance();

    public static void copyToRealmOrUpdate(final RealmObject obj) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                if (obj != null) {
                    if (obj instanceof SourceCollectModel) ((SourceCollectModel) obj).setTime(System.currentTimeMillis());
                    if (obj instanceof SourceHistoryModel) ((SourceHistoryModel) obj).setTime(System.currentTimeMillis());
                    realm.copyToRealmOrUpdate(obj);
                }
            }
        });
    }

    public static void copyToRealmOrUpdate(final List<RealmObject> list) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                if (list != null && !list.isEmpty()){
                    for(int i = 0; i < list.size(); i++) {
                        if (list.get(i) instanceof SourceCollectModel) ((SourceCollectModel) list.get(i)).setTime(System.currentTimeMillis());
                        if (list.get(i) instanceof SourceHistoryModel) ((SourceHistoryModel) list.get(i)).setTime(System.currentTimeMillis());
                        realm.copyToRealmOrUpdate(list.get(i));
                    }
                }
            }
        });
    }

    public static RealmObject queryByUrl(String url, Class<RealmObject> clazz) {
        return mRealm.where(clazz).equalTo("url", url).findFirst();
    }

    public static RealmList<RealmObject> queryByStatus(String status, Class<RealmObject> clazz) {
        RealmResults<RealmObject> results;
    }
}
