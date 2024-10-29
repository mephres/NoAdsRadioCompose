package me.kdv.weatherapp.di

import android.app.Application
import android.content.Context
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.logging.store.LoggingStoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.kdv.noadsradio.presentation.MusicPlayer

@Module
@InstallIn(SingletonComponent::class)
class PresentationModule {

        @Provides
        fun provideStoreFactory(): StoreFactory = LoggingStoreFactory(DefaultStoreFactory())

        @Provides
        fun provideContext(application: Application): Context {
                return application.applicationContext
        }

        @[Provides]
        fun provideMusicPlayer(context: Context): MusicPlayer {
                return MusicPlayer(context)
        }
}