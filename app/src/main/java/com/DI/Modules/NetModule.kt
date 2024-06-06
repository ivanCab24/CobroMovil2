package com.DI.Modules

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides

@Module
class NetModule {

    @Provides
    fun providesMoshi(): Moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()

}