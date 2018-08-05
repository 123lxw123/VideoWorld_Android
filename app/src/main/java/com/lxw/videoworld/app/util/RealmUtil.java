package com.lxw.videoworld.app.util;

import com.lxw.videoworld.app.model.SourceCollectModel;
import com.lxw.videoworld.app.model.SourceDetailModel;
import com.lxw.videoworld.app.model.SourceHistoryModel;
import com.lxw.videoworld.framework.log.LoggerHelper;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmModel;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 *
 * Created by Zion on 2018/4/17.
 */

public class RealmUtil {

    private static Realm mRealm = Realm.getDefaultInstance();

    public static void copyOrUpdateModel(final RealmModel obj) {
        Realm realm = Realm.getInstance(new RealmConfiguration.Builder().build());
        try {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    if (obj != null) {
                        if (obj instanceof SourceCollectModel)
                            ((SourceCollectModel) obj).setTime(System.currentTimeMillis());
                        if (obj instanceof SourceHistoryModel){
                            SourceHistoryModel oldSourceHistoryModel = realm.where(SourceHistoryModel.class).equalTo("link", ((SourceHistoryModel) obj).getLink()).findFirst();
                            if (oldSourceHistoryModel != null) {
                                if (((SourceHistoryModel) obj).getLocalFilePath() == null && oldSourceHistoryModel.getLocalFilePath() != null) {
                                    ((SourceHistoryModel) obj).setLocalFilePath(oldSourceHistoryModel.getLocalFilePath());
                                }
                                if (((SourceHistoryModel) obj).getLocalUrl() == null && oldSourceHistoryModel.getLocalUrl() != null) {
                                    ((SourceHistoryModel) obj).setLocalUrl(oldSourceHistoryModel.getLocalUrl());
                                }
                            }
                            ((SourceHistoryModel) obj).setTime(System.currentTimeMillis());
                            LoggerHelper.debug("RealmUtil", "seek==" + ((SourceHistoryModel) obj).getSeek() / 1000);
                            LoggerHelper.debug("RealmUtil", "status==" + ((SourceHistoryModel) obj).getStatus());
                        }
                        if (obj instanceof SourceDetailModel)
                            ((SourceDetailModel) obj).setTime(System.currentTimeMillis());
                        realm.copyToRealmOrUpdate(obj);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            realm.close();
        }
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
            copyOrUpdateModel(sourceCollectModel);
        }
    }

    public static SourceDetailModel queryDetailModelByUrl(String url) {
        return mRealm.where(SourceDetailModel.class).equalTo("url", url).findFirst();
    }

    public static void copyOrUpdateHistoryModel(final SourceHistoryModel obj, final boolean isUpdateProgress) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                if (obj != null) {
                    SourceHistoryModel oldSourceHistoryModel = mRealm.where(SourceHistoryModel.class).equalTo("link", obj.getLink()).findFirst();
                    if (!isUpdateProgress && oldSourceHistoryModel != null) {
                        obj.setSeek(oldSourceHistoryModel.getSeek());
                        obj.setTotal(oldSourceHistoryModel.getTotal());
                    }
                    realm.copyToRealmOrUpdate(obj);
                }
            }
        });
    }

    public static RealmResults<SourceHistoryModel> queryHistoryModelByStatus(String status) {
        return mRealm.where(SourceHistoryModel.class).equalTo("status", status).sort("time", Sort.DESCENDING).findAll();
    }

    public static void modifyHistoryStatusByUrl(String link, String status) {
        RealmResults<SourceHistoryModel> results;
        if (link == null) results = mRealm.where(SourceHistoryModel.class).findAll();
        else results = mRealm.where(SourceHistoryModel.class).equalTo("link", link).findAll();
        for (int i = 0; i < results.size(); i++) {
            SourceHistoryModel sourceHistoryModel = mRealm.copyFromRealm(results.get(i));
            sourceHistoryModel.setStatus(status);
            copyOrUpdateModel(sourceHistoryModel);
        }
    }

    public static SourceHistoryModel queryHistoryModelByLink(String link) {
        Realm realm = Realm.getInstance(new RealmConfiguration.Builder().build());
        try {
            SourceHistoryModel sourceHistoryModel = realm.where(SourceHistoryModel.class).equalTo("link", link).findFirst();
            if (sourceHistoryModel != null) return realm.copyFromRealm(sourceHistoryModel);
            else return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            realm.close();
        }
    }

    public static SourceHistoryModel queryHistoryModelByLocalUrl(String localUrl) {
        Realm realm = Realm.getInstance(new RealmConfiguration.Builder().build());
        try {
            SourceHistoryModel sourceHistoryModel = realm.where(SourceHistoryModel.class).equalTo("localUrl", localUrl).findFirst();
            if (sourceHistoryModel != null) return realm.copyFromRealm(sourceHistoryModel);
            else return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            realm.close();
        }
    }
}
