package com.mobss.namesofAllah.di.modules;

import android.app.Activity;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.mobss.namesofAllah.R;
import com.mobss.namesofAllah.controller.IDataManager;
import com.mobss.namesofAllah.controller.db.crud.DatabaseManager;
import com.mobss.namesofAllah.controller.db.initializer.DatabaseCreator;
import com.mobss.namesofAllah.controller.db.initializer.file.AssetsReader;
import com.mobss.namesofAllah.controller.db.initializer.file.IFileReader;
import com.mobss.namesofAllah.controller.db.initializer.inflator.Inflator;
import com.mobss.namesofAllah.controller.db.initializer.inflator.IsimlerInflator;
import com.mobss.namesofAllah.controller.db.initializer.parser.IFileParser;
import com.mobss.namesofAllah.controller.db.initializer.parser.JsonIsimlerParser;
import com.mobss.namesofAllah.controller.services.MobssAsyncTask;
import com.mobss.namesofAllah.controller.strategy.Strategy;
import com.mobss.namesofAllah.di.annotations.ActivityContext;
import com.mobss.namesofAllah.di.annotations.PerActivity;
import com.mobss.namesofAllah.views.activities.home.MainMvpPresenter;
import com.mobss.namesofAllah.views.activities.home.MainMvpView;
import com.mobss.namesofAllah.views.activities.home.MainPresenter;
import com.mobss.namesofAllah.views.activities.another.AnotherMvpPresenter;
import com.mobss.namesofAllah.views.activities.another.AnotherMvpView;
import com.mobss.namesofAllah.views.activities.another.AnotherPresenter;
import com.mobss.namesofAllah.views.activities.splash.SplashScreenMvpPresenter;
import com.mobss.namesofAllah.views.activities.splash.SplashScreenMvpView;
import com.mobss.namesofAllah.views.activities.splash.SplashScreenPresenter;
import com.yalantis.jellytoolbar.listener.JellyListener;
import com.yalantis.jellytoolbar.widget.JellyToolbar;

import java.util.Locale;

import dagger.Module;
import dagger.Provides;

/**
 * Created by ilkay on 10/03/2017.
 */

@Module
public class ActivityModule {
    Activity activity;

    public ActivityModule(Activity activity) {
        this.activity = activity;
    }

    @Provides
    Activity provideActivity(){
        return activity;
    }
    
    @Provides
    @PerActivity
    Typeface provideTypeface(){
        return Typeface.createFromAsset(activity.getAssets(), "fonts/Roboto-Light.ttf");
    }
    
    @Provides
    @PerActivity
    @ActivityContext
    Typeface provideTypefaceForActivity(){
        return Typeface.createFromAsset(activity.getAssets(), "fonts/cardo.regular.ttf");
    }
    
    @Provides
    @PerActivity
    SplashScreenMvpPresenter<SplashScreenMvpView> provideSplashScreenPresenter(IDataManager IDataManager) {
        return new SplashScreenPresenter<>(IDataManager);
    }
    
    @Provides
    @PerActivity
    MainMvpPresenter<MainMvpView> providesMainPresenter(IDataManager IDataManager, DatabaseCreator databaseCreator){
        return new MainPresenter<>(IDataManager, databaseCreator);
    }
    
    @Provides
    @PerActivity
    AnotherMvpPresenter<AnotherMvpView> providesSlideUpPanelPresenter(IDataManager IDataManager){
        return new AnotherPresenter<>(IDataManager);
    }
    
    
    @Provides
    @PerActivity
    MobssAsyncTask providesMobssAsyncTask(Activity activity, Strategy strategy){
        return new MobssAsyncTask(activity, strategy);
    }
    
    @Provides
    @PerActivity
    DatabaseCreator provideDatabaseCreator(DatabaseManager databaseManager,
                                           Inflator isimInflator,
                                           IFileReader isimFileReader,
                                           IFileParser isimFileParser){
        
        return new DatabaseCreator(databaseManager, isimInflator, isimFileReader, isimFileParser);
        
    }
    
    @Provides
    @PerActivity
    IFileReader provideIFileReader(IDataManager dataManager){
        String preSetLanguage = dataManager.getPreferredLanguage();

        if(preSetLanguage == null){
            preSetLanguage = Locale.getDefault().getLanguage();
        }

        // device language is Turkish
        if (preSetLanguage.equals("tr")) {
            dataManager.setPreferredLanguage("tr");
            return new AssetsReader(activity, "json_tr");
        } else{
            dataManager.setPreferredLanguage("en");
            return new AssetsReader(activity, "json_en");
        }
        
    }
    
    
    @Provides
    @PerActivity
    IFileParser provideIFileParser(){
        return new JsonIsimlerParser();
    }
    
    
    @Provides
    @PerActivity
    Inflator provideInflator(){
        return new IsimlerInflator();
    }

    @Provides
    @PerActivity
    JellyToolbar provideJellyToolbar(String[] searchList){
        final JellyToolbar toolbar = (JellyToolbar) activity.findViewById(R.id.jellytoolbar_main_toolbar);;
        final AutoCompleteTextView editText = (AutoCompleteTextView) LayoutInflater.from(activity).inflate(R.layout.edittext_toolbar, null);

        JellyListener jellyListener = new JellyListener() {
            @Override
            public void onCancelIconClicked() {
                if (TextUtils.isEmpty(editText.getText())) {
                    toolbar.collapse();
                } else {
                    editText.getText().clear();
                }
            }
        };

        toolbar.getToolbar().setNavigationIcon(R.mipmap.ic_launcher);
        toolbar.setJellyListener(jellyListener);
        toolbar.getToolbar().setPadding(0, getStatusBarHeight(), 0, 0);

        editText.setBackgroundResource(R.color.colorTransparent);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity,
                android.R.layout.simple_dropdown_item_1line, searchList);
        editText.setAdapter(adapter);

        toolbar.setContentView(editText);

        activity.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        return toolbar;
    }

    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = activity.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @Provides
    @PerActivity
    String[] provideSearchList(){
        return new String[] {
                "Allah", "Rahman", "Rahim", "Gaffar", "Rauf"
        };
    }
}
