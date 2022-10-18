package com.chroneed.chroneedapp.utilities;

import android.content.Context;
import android.content.SharedPreferences;

public class MySharedPreferences {


    public static final String KEY_GET_IS_LOGIN = "isLogin";
    public static final String KEY_GET_IS_MANAGER = "isMan";
    public static final String KEY_GET_USER_TYPE = "userType";
    public static final String KEY_GET_USER_ID = "userID";
    public static final String KEY_GET_STATUS = "userToken";
    public static final String KEY_GET_NAME = "userName";
    public static final String KEY_GET_FULL_NAME = "FirstName";
    public static final String KEY_GET_LAST_NAME = "LastName";
    public static final String KEY_GET_Phone = "phone";
    public static final String KEY_Bazzar_token = "Bazzar";
    public static final String Key_App_Name = "Chroneed";

    public static void setIsLogin(boolean isLogin, Context context) {
        SharedPreferences mySharedPr = context.getSharedPreferences(Key_App_Name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPr.edit();
        editor.putBoolean(KEY_GET_IS_LOGIN, isLogin);
        editor.apply();
    }

    public static boolean isLogin(Context context) {
        SharedPreferences mySharedPr = context.getSharedPreferences(Key_App_Name, Context.MODE_PRIVATE);
        return mySharedPr.getBoolean(KEY_GET_IS_LOGIN, false);
    }


    public static void setManager(boolean isLogin, Context context) {
        SharedPreferences mySharedPr = context.getSharedPreferences(Key_App_Name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPr.edit();
        editor.putBoolean(KEY_GET_IS_MANAGER, isLogin);
        editor.apply();
    }

    public static boolean isManager(Context context) {
        SharedPreferences mySharedPr = context.getSharedPreferences(Key_App_Name, Context.MODE_PRIVATE);
        return mySharedPr.getBoolean(KEY_GET_IS_MANAGER, false);
    }


    //ذخیره آیدی وارد شده
    public static void saveUserPhone(String FirstName, Context context) {
        SharedPreferences mySharedPr = context.getSharedPreferences(Key_App_Name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPr.edit();
        editor.putString(KEY_GET_Phone, FirstName);
        editor.apply();
    }

    //دریافت آیدی کاربر وارد شده
    public static String getUserPhone(Context context) {
        SharedPreferences mySharedPr = context.getSharedPreferences(Key_App_Name, Context.MODE_PRIVATE);
        return mySharedPr.getString(KEY_GET_Phone, "");
    }

    //ذخیره آیدی وارد شده
    public static void saveUserId(String FirstName, Context context) {
        SharedPreferences mySharedPr = context.getSharedPreferences(Key_App_Name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPr.edit();
        editor.putString(KEY_GET_USER_ID, FirstName);
        editor.apply();
    }

    //دریافت آیدی کاربر وارد شده
    public static String getUserId(Context context) {
        SharedPreferences mySharedPr = context.getSharedPreferences(Key_App_Name, Context.MODE_PRIVATE);
        return mySharedPr.getString(KEY_GET_USER_ID, "");
    }

    //حذف  آیدی کاربر وارد شده
    public static void deleteUserID(Context context) {
        SharedPreferences mySharedPr = context.getSharedPreferences(Key_App_Name, Context.MODE_PRIVATE);
        mySharedPr.edit().remove(KEY_GET_USER_ID).apply();

    }

    //ذخیره نام کاربر وارد شده
    public static void saveUserFirstName(String FirstName, Context context) {
        SharedPreferences mySharedPr = context.getSharedPreferences(Key_App_Name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPr.edit();
        editor.putString(KEY_GET_FULL_NAME, FirstName);
        editor.apply();
    }

    //دریافت نام کاربر وارد شده
    public static String getUserFirstName(Context context) {
        SharedPreferences mySharedPr = context.getSharedPreferences(Key_App_Name, Context.MODE_PRIVATE);
        return mySharedPr.getString(KEY_GET_FULL_NAME, "");
    }

    //حذف نام کاربر کاربر وارد شده
    public static void deleteUserFirstName(Context context) {
        SharedPreferences mySharedPr = context.getSharedPreferences(Key_App_Name, Context.MODE_PRIVATE);
        mySharedPr.edit().remove(KEY_GET_FULL_NAME).apply();
    }


    //ذخیره تایپ کاربر وارد شده
    public static void saveUserType(String UserType, Context context) {
        SharedPreferences mySharedPr = context.getSharedPreferences(Key_App_Name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPr.edit();
        editor.putString(KEY_GET_USER_TYPE, UserType);
        editor.apply();
    }

    //دریافت تایپ کاربر وارد شده
    public static String getUserType(Context context) {
        SharedPreferences mySharedPr = context.getSharedPreferences(Key_App_Name, Context.MODE_PRIVATE);
        return mySharedPr.getString(KEY_GET_USER_TYPE, "0");
    }

    //حذف تایپ کاربر کاربر وارد شده
    public static void deleteUserType(Context context) {
        SharedPreferences mySharedPr = context.getSharedPreferences(Key_App_Name, Context.MODE_PRIVATE);
        mySharedPr.edit().remove(KEY_GET_USER_TYPE).apply();
    }


    //ذخیره نام کاربر وارد شده
    public static void saveUserLastName(String FirstName, Context context) {
        SharedPreferences mySharedPr = context.getSharedPreferences(Key_App_Name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPr.edit();
        editor.putString(KEY_GET_LAST_NAME, FirstName);
        editor.apply();
    }

    //دریافت نام کاربر وارد شده
    public static String getUserLastName(Context context) {
        SharedPreferences mySharedPr = context.getSharedPreferences(Key_App_Name, Context.MODE_PRIVATE);
        return mySharedPr.getString(KEY_GET_LAST_NAME, "");
    }

    //حذف نام کاربر کاربر وارد شده
    public static void deleteUserLastName(Context context) {
        SharedPreferences mySharedPr = context.getSharedPreferences(Key_App_Name, Context.MODE_PRIVATE);
        mySharedPr.edit().remove(KEY_GET_LAST_NAME).apply();

    }


    //ذخیره توکن وارد شده
    public static void saveUserToken(String token, Context context) {
        SharedPreferences mySharedPr = context.getSharedPreferences(Key_App_Name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPr.edit();
        editor.putString(KEY_GET_STATUS, token);
        editor.apply();
    }

    public static String getUserToken(Context context) {
        SharedPreferences mySharedPr = context.getSharedPreferences(Key_App_Name, Context.MODE_PRIVATE);
        return mySharedPr.getString(KEY_GET_STATUS, "");
    }

    public static void deleteUserToken(Context context) {
        SharedPreferences mySharedPr = context.getSharedPreferences(Key_App_Name, Context.MODE_PRIVATE);
        mySharedPr.edit().remove(KEY_GET_STATUS).apply();

    }

    public static void saveBazzarToken(String token, Context context) {
        SharedPreferences mySharedPr = context.getSharedPreferences(Key_App_Name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPr.edit();
        editor.putString(KEY_Bazzar_token, token);
        editor.apply();
    }

    public static String getBazzarToken(Context context) {
        SharedPreferences mySharedPr = context.getSharedPreferences(Key_App_Name, Context.MODE_PRIVATE);
        return mySharedPr.getString(KEY_Bazzar_token, "ATIp6nNUjGdagwB0evEiz3pVihHuCZ");
    }

    //ذخیره پروفایل وارد شده
    public static void saveUserPic(String userName, Context context) {
        SharedPreferences mySharedPr = context.getSharedPreferences(Key_App_Name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPr.edit();
        editor.putString(KEY_GET_NAME, userName);
        editor.apply();
    }

    //دریافت پروفایل کاربر وارد شده
    public static String getUserPic(Context context) {
        SharedPreferences mySharedPr = context.getSharedPreferences(Key_App_Name, Context.MODE_PRIVATE);
        return mySharedPr.getString(KEY_GET_NAME, "");
    }

    //حذف پروفایل کاربر وارد شده
    public static void deleteUserPic(Context context) {
        SharedPreferences mySharedPr = context.getSharedPreferences(Key_App_Name, Context.MODE_PRIVATE);
        mySharedPr.edit().remove(KEY_GET_NAME).apply();
    }

}

