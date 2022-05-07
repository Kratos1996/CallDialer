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
import com.artixtise.richdialer.data.contact.contactRepository.ContactRepository
import com.artixtise.richdialer.database.datastore.DataStoreBase
import com.artixtise.richdialer.database.datastore.DataStoreCustom
import com.artixtise.richdialer.database.prefrence.SharedPre
import com.artixtise.richdialer.database.roomdatabase.AppDB
import com.artixtise.richdialer.data.contact.contactRepository.ContactsRepositoryImpl
import com.artixtise.richdialer.data.login.LoginRepository
import com.artixtise.richdialer.data.login.LoginRepositoryImpl
import com.artixtise.richdialer.database.roomdatabase.MyDao
import com.artixtise.richdialer.repositories.methods.MethodsRepo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
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
    fun providesLoginRepository(@ApplicationContext context: Context, db: AppDB,dataStore: DataStoreBase,sharedPre: SharedPre,onlineDatastore:CollectionReference): LoginRepositoryImpl = LoginRepositoryImpl(db, context, auth = getFirebaseAuth(),dataStore,
        onlineDatastore,sharedPre)

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

    @Module
    @InstallIn(ActivityComponent::class)
    class ActivityModule {
        @Provides
        fun fragmentManager(activity: Activity): FragmentManager {
            return (activity as AppCompatActivity).supportFragmentManager
        }


    }

}