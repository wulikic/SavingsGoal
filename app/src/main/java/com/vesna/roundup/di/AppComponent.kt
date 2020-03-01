package com.vesna.roundup.di

import com.vesna.roundup.weeks.MainActivity
import dagger.Component
import javax.inject.Singleton

@Component(modules = [ AppModule::class ])
@Singleton
interface AppComponent {

    fun inject(activity: MainActivity)

}