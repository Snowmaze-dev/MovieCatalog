package ru.snowmaze.moviecatalog

import android.app.Application
import dagger.Component
import dagger.hilt.android.HiltAndroidApp
import ru.snowmaze.moviecatalog.data.di.DataModule

@Component(modules = [DataModule::class])
interface ApplicationComponent

@HiltAndroidApp
class App : Application() {

    val appComponent = DaggerApplicationComponent.create()
}