package com.meliksahcakir.cointracker

import android.content.Context
import androidx.room.Room
import com.meliksahcakir.cointracker.data.ApiService
import com.meliksahcakir.cointracker.data.CoinDao
import com.meliksahcakir.cointracker.data.CoinDatabase
import com.meliksahcakir.cointracker.data.CoinDetailsDao
import com.meliksahcakir.cointracker.data.CoinRepository
import com.meliksahcakir.cointracker.details.DetailsViewModel
import com.meliksahcakir.cointracker.favorite.FavoriteViewModel
import com.meliksahcakir.cointracker.main.MainViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val mainViewModelModule = module {
    viewModel {
        MainViewModel(get())
    }
}

val detailsViewModelModule = module {
    viewModel {
        DetailsViewModel(get())
    }
}

val favoriteViewModelModule = module {
    viewModel {
        FavoriteViewModel(get())
    }
}

val repositoryModule = module {

    fun provideCoinDao(db: CoinDatabase): CoinDao {
        return db.coinDao()
    }

    fun provideCoinDetailsDao(db: CoinDatabase): CoinDetailsDao {
        return db.coinDetailsDao()
    }

    single { CoinRepository(provideCoinDao(get()), provideCoinDetailsDao(get()), get()) }
}

val apiModule = module {

    fun provideApi(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    single { provideApi(get()) }
}

val retrofitModule = module {

    fun provideHttpClient(): OkHttpClient {
        val logger = HttpLoggingInterceptor()
        logger.level = HttpLoggingInterceptor.Level.BASIC
        return OkHttpClient.Builder().addInterceptor(logger).build()
    }

    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(ApiService.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single { provideHttpClient() }
    single { provideRetrofit(get()) }
}

val databaseModule = module {

    fun provideDatabase(context: Context): CoinDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            CoinDatabase::class.java,
            "CoinTracker.db"
        ).fallbackToDestructiveMigration().build()
    }

    single { provideDatabase(androidContext()) }
}

val modules = listOf(
    mainViewModelModule,
    detailsViewModelModule,
    favoriteViewModelModule,
    repositoryModule,
    apiModule,
    retrofitModule,
    databaseModule
)
