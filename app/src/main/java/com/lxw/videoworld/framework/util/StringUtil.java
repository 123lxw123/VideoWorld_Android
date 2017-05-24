package com.lxw.videoworld.framework.util;

import android.content.Context;
import android.telephony.TelephonyManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lxw9047 on 2016/10/11.
 */
public class StringUtil {

	public static boolean isNull(String str) {
		boolean b = false;
		if (str == null || str.trim().length() == 0)
			b = true;

		return b;
	}

	public static boolean isNULL(String str) {
		boolean b = false;
		if (str == null)
			b = true;

		return b;
	}
	
	public static boolean isSpace(String str) {
		for(int i=0;i<str.length();i++){
			int chr = (int)str.charAt(i);
			if(chr!=32)
				return false;
		}
		return true;
	}

	public static boolean isNull(String str, boolean bValidNullString) {
		boolean b = false;
		if (str == null || str.trim().length() == 0)
			b = true;
		if (!b && bValidNullString) {
			if (str != null && str.equalsIgnoreCase("null"))
				b = true;
		}
		return b;
	}

	public static boolean isUrl(String str) {
		if (isNull(str))
			return false;
		return str
				.matches("^http://(\\w+(-\\w+)*)(\\.(\\w+(-\\w+)*))*(\\?\\S*)?$");
	}

	public static boolean str2Boolean(String s, boolean defaultV) {
		if (StringUtil.isNull(s))
			return defaultV;
		if (s != null && s.equalsIgnoreCase("true")) {
			return true;
		} else {
			return false;
		}
	}

	public static int str2Int(String s, int defaultV) {
		if (s != null && !s.equals("")) {
			int num = defaultV;
			try {
				num = Integer.parseInt(s);
			} catch (Exception ignored) {
			}
			return num;
		} else {
			return defaultV;
		}
	}

	public static long str2Long(String s, long defaultV) {
		if (s != null && !s.equals("")) {
			long num = defaultV;
			try {
				num = Long.parseLong(s);
			} catch (Exception ignored) {
			}
			return num;
		} else {
			return defaultV;
		}
	}

	public static double str2Double(String s, double defaultV) {
		if (s != null && !s.equals("")) {
			double num = defaultV;
			try {
				num = Double.parseDouble(s);
			} catch (Exception ignored) {
			}
			return num;
		} else {
			return defaultV;
		}
	}

	public static float str2Float(String s, float defaultV) {
		if (s != null && !s.equals("")) {
			float num = defaultV;
			try {
				num = Float.parseFloat(s);
			} catch (Exception ignored) {
			}
			return num;
		} else {
			return defaultV;
		}
	}

	public static boolean IsChinese(char c) {
		return (int) c >= 0x4E00 && (int) c <= 0x9FA5;
	}
	/**
	 * 判断是否全为数字和字母
	 * @param s
	 * @return
	 */
	public static boolean IsNumberOrCharacter(String s) {
		boolean isValid = false;
		String expression = "[a-zA-Z0-9]+";
		CharSequence inputStr = s;
	    /*创建Pattern*/
	    Pattern pattern = Pattern.compile(expression);
	    /*将Pattern 以参数传入Matcher作Regular expression*/ 
	    Matcher matcher = pattern.matcher(inputStr);
	    if(matcher.matches())
	    {
	      isValid = true;
	    }
		return isValid;
	}
	
	public static boolean isNumber(String number) {
		boolean isValid = false;
//		String regExp = "^[1][0-9]{10}$";
		String regExp = "[0-9]+";
		Pattern p = Pattern.compile(regExp);
		Matcher m = p.matcher(number);
	    if(m.matches())
	    {
	      isValid = true;
	    }
		return isValid;
	}
	
	/**
	 * 判断是否全为数字和字母，并且字母开头
	 * @param s
	 * @return
	 */
	public static boolean IsNumberOrCharacterAndFC(String s) {
		boolean isValid = false;
		String expression = "[a-zA-Z]{1}[a-zA-Z0-9]+";
		CharSequence inputStr = s;
	    /*创建Pattern*/
	    Pattern pattern = Pattern.compile(expression);
	    /*将Pattern 以参数传入Matcher作Regular expression*/ 
	    Matcher matcher = pattern.matcher(inputStr);
	    if(matcher.matches())
	    {
	      isValid = true;
	    }
		return isValid;
	}
	/*
	 * Version format like x.x.x.x
	 * if oldVersion > newVersion return false
	 * else return true;(need update)
	 */
	public static boolean compareVersion(String oldVersion, String newVersion){
		boolean tempState = false;
		if(isNull(oldVersion)==true||oldVersion.indexOf(".")<0)tempState = true;
		if(isNull(newVersion)==true||newVersion.indexOf(".")<0)tempState = false;
		oldVersion = getRealVersion(oldVersion);
		newVersion = getRealVersion(newVersion);
		String[] oldVersions = oldVersion.split("\\.");
		String[] newVersions = newVersion.split("\\.");
		if(oldVersions.length==newVersions.length){
			for(int i = 0;i<oldVersions.length;i++){
				int oldNum = str2Int(oldVersions[i],-1);
				int newNum = str2Int(newVersions[i],-1);
				if(oldNum>newNum){
					tempState = false;
					break;
				}else if(oldNum<newNum){
					tempState = true;
					break;
				}
			}
		}else{
			if(oldVersions.length<newVersions.length){
				tempState = true;				
			}else{
				tempState = false;				
			}
		}
		return tempState;
	}

	public static String getRealVersion(String version){
		String rVersion = "";
		if(isNull(version)==true)return rVersion;
		rVersion = version;
		int indexChar = version.indexOf("_");
		if(indexChar>-1){
			rVersion = version.substring(0,indexChar);
		}
		return rVersion;
	}
	/**
	 * 获取文件名
	 */
	public static String getFileName(String url) {
		String filename =url.substring(url.lastIndexOf('/') + 1);
		if(filename==null || "".equals(filename.trim())){//如果获取不到文件名称
			filename = UUID.randomUUID()+ ".tmp";//默认取一个文件名
		}
		return filename;
	}
	private static boolean isExist(int iId, List<Integer> iList) {
		boolean result = false;
		for (int i : iList) {
			if (i == iId) {
				result = true;
			}
		}
		return result;
	}
    
	public static List<Integer> getList(int iTotalNumber, int iMaxNumber) {
		List<Integer> mList = new ArrayList<Integer>();
		Random rnd = new Random();
		while (mList.size() < iTotalNumber) {
			int id = rnd.nextInt(iMaxNumber);
			if (!isExist(id, mList)) {
				mList.add(id);
			}
		}
		return mList;
	}

    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    public static boolean isNotEmpty(String str) {
        return !StringUtil.isEmpty(str);
    }

    public static boolean isBlank(CharSequence str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if ((Character.isWhitespace(str.charAt(i)) == false)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNotBlank(String str) {
        return !StringUtil.isBlank(str);
    }

    public static boolean isNumeric(CharSequence cs) {
        if (cs == null || cs.length() == 0) {
            return false;
        }
        int sz = cs.length();
        for (int i = 0; i < sz; i++) {
            if (Character.isDigit(cs.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }

    public static String getValueByKey(String result, String key) {
        String value = null;
        if (!isEmpty(result)) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                String resultCode = jsonObject.getString("Result");
                if (resultCode != null && "0".equals(resultCode)) {
                    value = jsonObject.getString(key);
                } else {
                    System.out.println("result may be have some problem .");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return value;
    }

    public static boolean checkHpsTimeOut(String result) {
        boolean flag = false;
        if (!isEmpty(result)) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                String resultCode = jsonObject.getString("Result");
                if (resultCode != null && "-10242504".equals(resultCode)) {
                    flag = true;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return flag;
    }

    public static boolean checkCallBackStat(int code, String sessionId) {
        boolean stat = false;
        if (code == 0 && !isEmpty(sessionId)) {
            stat = true;
        }
        return stat;
    }

    public static String getMobileNum(Context ctx) {
        try {
            TelephonyManager mTelephonyMgr = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
            String tel = mTelephonyMgr.getLine1Number();
            if (tel == null) {
                tel = "";
            }
            return tel;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
	
}
