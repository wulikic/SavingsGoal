package com.vesna.roundup.di

import com.vesna.roundup.presentation.MainActivity
import dagger.Component
import javax.inject.Singleton

@Component(modules = [ AppModule::class ])
@Singleton
interface AppComponent {

    fun inject(activity: MainActivity)

}