package com.mobss.islamic.namesofAllah.controller.pref;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.mobss.islamic.namesofAllah.di.annotations.ApplicationContext;

import javax.inject.Inject;
import javax.inject.Singleton;


/**
 * Created by iaktas on 10.03.2017.
 */

@Singleton
public class PreferenceHelper implements IPreferenceHelper {
    public static final String SHARED_PREF_DBDREATED = "DatabaseCreated";
    public static final String SHARED_PREF_APPLANGUAGE = "ApplicationLanguage";
    public static final String SHARED_PREF_DAILYNOTIFICATION = "DailyNotification";

    private final SharedPreferences mPrefs;
    
    @Inject
    public PreferenceHelper(@ApplicationContext Context context) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
    }
    
    @Override
    public boolean getDatabaseCreatedStatus() {
        return mPrefs.getBoolean(SHARED_PREF_DBDREATED, false);
    }

    @Override
    public void setDatabaseCreatedStatus(boolean isCreated) {
        mPrefs.edit().putBoolean(SHARED_PREF_DBDREATED, isCreated).apply();
    }
    
    @Override
    public String getPreferredLanguage() {
        return mPrefs.getString(SHARED_PREF_APPLANGUAGE, null);
    }
    
    @Override
    public void setPreferredLanguage(String language) {
        mPrefs.edit().putString(SHARED_PREF_APPLANGUAGE, language).apply();
    }

    @Override
    public void setDailyNotification(boolean isAccepted) {
        mPrefs.edit().putBoolean(SHARED_PREF_DAILYNOTIFICATION, isAccepted).apply();
    }

    @Override
    public boolean getDailyNotification() {
        return mPrefs.getBoolean(SHARED_PREF_DAILYNOTIFICATION, true);
    }

    @Override
    public boolean containsDailyNotification() {
        return mPrefs.contains(SHARED_PREF_DAILYNOTIFICATION);
    }


}
