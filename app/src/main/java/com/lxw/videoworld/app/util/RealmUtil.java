package com.lxw.videoworld.app.util;

import com.lxw.videoworld.app.model.SourceCollectModel;
import com.lxw.videoworld.app.model.SourceDetailModel;
import com.lxw.videoworld.app.model.SourceHistoryModel;

import io.realm.Realm;
import io.realm.RealmModel;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 *
 * Created by Zion on 2018/4/17.
 */

public class RealmUtil {

    private static Realm mRealm = Realm.getDefaultInstance();

    public static Realm getInstance() {
        return mRealm;
    }

    public static void copyOrUpdateCollectModel(final RealmModel obj) {
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

    public static void copyOrUpdateDetailModel(final SourceDetailModel sourceDetailModel) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                if (sourceDetailModel != null) {
                    sourceDetailModel.setTime(System.currentTimeMillis());
                    realm.copyToRealmOrUpdate(sourceDetailModel);
                }
            }
        });
    }

    public static SourceCollectModel queryCollectModelByUrl(String url) {
        return mRealm.where(SourceCollectModel.class).equalTo("url", url).findFirst();
    }

    public static RealmResults<SourceCollectModel> queryCollectModelByStatus(String status) {
        return mRealm.where(SourceCollectModel.class).equalTo("status", status).sort("time", Sort.DESCENDING).findAll();
    }

    public static void modifyCollectStatusByUrl(String url, String status) {
        RealmResults<SourceCollectModel> results;
        if (url == null) results = mRealm.where(SourceCollectModel.class).findAll();
        else results = mRealm.where(SourceCollectModel.class).equalTo("url", url).findAll();
        for (int i = 0; i < results.size(); i++) {
            SourceCollectModel sourceCollectModel = mRealm.copyFromRealm(results.get(i));
            sourceCollectModel.setStatus(status);
            copyOrUpdateCollectModel(sourceCollectModel);
        }
    }

    public static SourceDetailModel queryDetailModelByUrl(String url) {
        return mRealm.where(SourceDetailModel.class).equalTo("url", url).findFirst();
    }

}
