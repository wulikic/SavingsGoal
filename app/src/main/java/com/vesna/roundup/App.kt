package com.vesna.roundup

import android.app.Application
import com.vesna.roundup.di.AppComponent
import com.vesna.roundup.di.AppModule
import com.vesna.roundup.di.DaggerAppComponent
import net.danlew.android.joda.JodaTimeAndroid

class App : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        JodaTimeAndroid.init(this)

        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
    }
}