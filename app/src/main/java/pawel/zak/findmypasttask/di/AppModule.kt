package pawel.zak.findmypasttask.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pawel.zak.findmypasttask.common.Constants.BASE_URL
import pawel.zak.findmypasttask.data.local.UserProfilesDatabase
import pawel.zak.findmypasttask.data.remote.ProfileApi
import pawel.zak.findmypasttask.data.repository.UserProfilesRepositoryImpl
import pawel.zak.findmypasttask.domain.repository.UserProfilesRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(app: Application): UserProfilesDatabase {
        return Room.databaseBuilder(
            app,
            UserProfilesDatabase::class.java,
            UserProfilesDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideProfileApi(): ProfileApi {
        return Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(
                GsonConverterFactory
                    .create()
            )
            .build()
            .create()
    }

    @Provides
    @Singleton
    fun provideProfileRepository(
        api: ProfileApi,
        database: UserProfilesDatabase
    ): UserProfilesRepository {
        return UserProfilesRepositoryImpl(api, database.userProfilesDao, database)
    }

}