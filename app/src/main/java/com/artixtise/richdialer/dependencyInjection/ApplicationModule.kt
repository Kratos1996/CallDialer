package com.artixtise.richdialer.dependencyInjection
/*
* © Copyright Ishant Sharma
* Android Developer
* JAVA/KOTLIN
* +91-7732993378
* ishant.sharma1947@gmail.com
* */

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.createDataStore
import androidx.fragment.app.FragmentManager
import androidx.room.Room
import com.artixtise.richdialer.R
import com.artixtise.richdialer.api.ApiInterface
import com.artixtise.richdialer.base.BASE_URL
import com.artixtise.richdialer.data.contact.contactRepository.ContactRepository
import com.artixtise.richdialer.database.datastore.DataStoreBase
import com.artixtise.richdialer.database.datastore.DataStoreCustom
import com.artixtise.richdialer.database.prefrence.SharedPre
import com.artixtise.richdialer.database.roomdatabase.AppDB
import com.artixtise.richdialer.data.contact.contactRepository.ContactsRepositoryImpl
import com.artixtise.richdialer.data.login.LoginRepository
import com.artixtise.richdialer.data.login.LoginRepositoryImpl
import com.artixtise.richdialer.data.profile.ProfileRepository
import com.artixtise.richdialer.data.profile.ProfileRepositoryImpl
import com.artixtise.richdialer.data.remote.richCallDataCloud.ApiRepository
import com.artixtise.richdialer.data.remote.richCallDataCloud.ApiRepositoryImpl
import com.artixtise.richdialer.data.richcall.RichCallRepository
import com.artixtise.richdialer.data.richcall.RichRepositoryImpl
import com.artixtise.richdialer.database.roomdatabase.MyDao
import com.artixtise.richdialer.domain.remote.apiUsecase.getData.GetRicCallDataUseCase
import com.artixtise.richdialer.domain.remote.apiUsecase.getData.UseCaseGetData
import com.artixtise.richdialer.domain.remote.apiUsecase.setData.SetRicCallDataUseCase
import com.artixtise.richdialer.domain.remote.apiUsecase.setData.UseCaseSetData
import com.artixtise.richdialer.domain.remote.apiUsecase.uplodeImage.UplodeImageUseCase
import com.artixtise.richdialer.domain.remote.apiUsecase.uplodeImage.UseCaseImage
import com.artixtise.richdialer.repositories.methods.MethodsRepo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {

    @Provides
    fun provideCustomRepository(dataStore: DataStore<Preferences>): DataStoreBase =DataStoreCustom(
        dataStore
    )

    @Provides
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> =context.createDataStore(
        context.getString(R.string.app_name)
    )

    @Provides
    fun getOnlineDatabase(@ApplicationContext context: Context): CollectionReference =Firebase.firestore.collection(context.getString(R.string.app_name))

    @Provides
    fun provideDatabase(@ApplicationContext context: Context): AppDB {
        return Room.databaseBuilder(context, AppDB::class.java, context.getString(R.string.app_name))
            .fallbackToDestructiveMigration().build()
    }
    @Provides
    fun provideSharedPrefrence(@ApplicationContext context: Context):SharedPre= SharedPre.getInstance(
        context
    )!!

    @Provides
    fun providesPostDao(db: AppDB): MyDao = db.getDao()

    @Provides
    fun providesdatabaseRepository(@ApplicationContext context: Context,
                                   db: AppDB,
                                   dataStore: DataStoreBase,
                                   sharedPre: SharedPre,
                                   onlineDatastore:CollectionReference):
            ContactRepository = ContactsRepositoryImpl(db, context, auth = getFirebaseAuth(),dataStore,
        onlineDatastore,sharedPre)


    @Provides
    fun providesRichCallRepository(@ApplicationContext context: Context,
                                   db: AppDB):
            RichCallRepository = RichRepositoryImpl(db, context)

    @Provides
    fun providesMyProfileRepository(@ApplicationContext context: Context,
                                   db: AppDB):
            ProfileRepository = ProfileRepositoryImpl(db, context)

    @Provides
    fun providesLoginRepository(@ApplicationContext context: Context, db: AppDB,dataStore: DataStoreBase,sharedPre: SharedPre,onlineDatastore:CollectionReference,profileRepo:ProfileRepository): LoginRepositoryImpl = LoginRepositoryImpl(db, context, auth = getFirebaseAuth(),dataStore,
        onlineDatastore,sharedPre,profileRepo)

    @Provides
    fun provideMethodsRepo(@ApplicationContext context: Context, dataStore: DataStoreBase): MethodsRepo = MethodsRepo(context, dataStore)

    @Provides
    fun getFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    fun getDeveloperName(): String = "© Copyright Ishant Sharma"


    @Provides
    fun ProvideDispatchers():DispatchersProviders=object : DispatchersProviders{
        override val main: CoroutineDispatcher
            get() = Dispatchers.Main
        override val io: CoroutineDispatcher
            get() = Dispatchers.IO
        override val default: CoroutineDispatcher
            get() = Dispatchers.Default
        override val unconfined: CoroutineDispatcher
            get() = Dispatchers.Unconfined

    }
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build()
    }

    @Provides
    @Singleton
    fun provideApi(okHttpClient: OkHttpClient): ApiInterface {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiInterface::class.java)
    }

    @Provides
    @Singleton
    fun provideRemoteRepository(api: ApiInterface): ApiRepository {
        return ApiRepositoryImpl(api)
    }
    @Provides
    @Singleton
    fun provideGetRichCallDataUseCase(repository: ApiRepository):UseCaseGetData {
         return GetRicCallDataUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideSetRichCallDataUseCase(repository: ApiRepository): UseCaseSetData {
       return SetRicCallDataUseCase(repository)
    }

    @Provides
    @Singleton
    fun UplodeImage(repository: ApiRepository): UseCaseImage {
       return UplodeImageUseCase(repository)
    }

    @Module
    @InstallIn(ActivityComponent::class)
    class ActivityModule {
        @Provides
        fun fragmentManager(activity: Activity): FragmentManager {
            return (activity as AppCompatActivity).supportFragmentManager
        }


    }

}