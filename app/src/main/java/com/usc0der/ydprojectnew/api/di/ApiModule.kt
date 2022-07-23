package com.usc0der.ydprojectnew.api.di

import com.usc0der.ydprojectnew.api.service.LoginService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create


@[Module InstallIn(SingletonComponent::class)]
object ApiModule {
    @Provides
    fun providerLoginSerivce(retrofit: Retrofit): LoginService = retrofit.create()


}