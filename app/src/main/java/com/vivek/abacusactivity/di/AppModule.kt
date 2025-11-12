package com.vivek.abacusactivity.di

import android.content.Context
import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.vivek.abacusactivity.data.dao.GameDao
import com.vivek.abacusactivity.data.database.AbacusDatabase
import com.vivek.abacusactivity.domain.repository.ProblemRepository
import com.vivek.abacusactivity.domain.usecase.SaveGameAndResultsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class) // This makes the dependencies live as long as the application
object AppModule {

    // --- Firebase Providers ---

    @Provides
    @Singleton // Ensures only one instance of FirebaseAuth is created
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton // Ensures only one instance of Firestore is created
    fun provideFirebaseFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    // --- Database Providers ---

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AbacusDatabase {
        return Room.databaseBuilder(
            context,
            AbacusDatabase::class.java,
            "abacus_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideGameDao(database: AbacusDatabase): GameDao = database.gameDao()

    // --- Repository Provider ---

    @Provides
    @Singleton
    fun provideProblemRepository(): ProblemRepository = ProblemRepository()

    // --- Use Case Provider ---

    @Provides
    @Singleton
    fun provideSaveGameAndResultsUseCase(
        gameDao: GameDao,
    ): SaveGameAndResultsUseCase {
        return SaveGameAndResultsUseCase(gameDao)
    }
}