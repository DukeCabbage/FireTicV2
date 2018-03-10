package com.cabbage.fireticv2.dagger.app

import com.cabbage.fireticv2.dagger.ApplicationScope
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
object FirebaseModule{

    @ApplicationScope @Provides @Named("default")
    fun provideFirebaseApp(): FirebaseApp {
        return FirebaseApp.getInstance(FirebaseApp.DEFAULT_APP_NAME)
    }

    @ApplicationScope @Provides
    fun provideFirestore(@Named("default") app: FirebaseApp): FirebaseFirestore {
        return FirebaseFirestore.getInstance(app)
    }

    @ApplicationScope @Provides
    fun provideFirebaseAuth(@Named("default") app: FirebaseApp): FirebaseAuth {
        return FirebaseAuth.getInstance(app)
    }
}