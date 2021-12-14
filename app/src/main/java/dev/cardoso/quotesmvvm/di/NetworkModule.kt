package dev.cardoso.quotesmvvm.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.cardoso.quotesmvvm.core.BASE_URL
import dev.cardoso.quotesmvvm.data.remote.QuoteApi
import dev.cardoso.quotesmvvm.data.remote.UserApi
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun  provideQuoteApi(retrofit: Retrofit):QuoteApi{
        return retrofit.create(QuoteApi::class.java)
    }


    @Singleton
    @Provides
    fun  provideUserApi(retrofit: Retrofit): UserApi {
        return retrofit.create(UserApi::class.java)
    }
}