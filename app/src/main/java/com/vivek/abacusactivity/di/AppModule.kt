package com.vivek.abacusactivity.di

import android.content.Context
import androidx.room.Room
import androidx.work.WorkManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.vivek.abacusactivity.data.dao.GameDao
import com.vivek.abacusactivity.data.database.AbacusDatabase
import com.vivek.abacusactivity.data.database.AbacusDatabase.Companion.MIGRATION_1_2
import com.vivek.abacusactivity.data.repository.AuthRepositoryImpl
import com.vivek.abacusactivity.data.repository.DoubleDigitProblemRepositoryImpl
import com.vivek.abacusactivity.data.repository.GameRepositoryImpl
import com.vivek.abacusactivity.data.repository.ProblemRepositoryProvider
import com.vivek.abacusactivity.domain.repository.AuthRepository
import com.vivek.abacusactivity.domain.repository.GameRepository
import com.vivek.abacusactivity.domain.repository.ProblemRepository
import com.vivek.abacusactivity.data.repository.SingleDigitProblemRepositoryImpl
import com.vivek.abacusactivity.domain.usecase.FinalizeGameUseCase
import com.vivek.abacusactivity.domain.usecase.SaveProblemResultUseCase
import com.vivek.abacusactivity.domain.usecase.StartNewGameUseCase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class SingleDigitRepo

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DoubleDigitRepo
@Module
@InstallIn(SingletonComponent::class) // This makes the dependencies live as long as the application
abstract class AppModule {

    // --- Firebase Providers ---
    @Binds
    @Singleton
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindProblemRepository(impl: ProblemRepositoryProvider): ProblemRepository

    @Binds
    @Singleton
    abstract fun bindGameRepository(impl: GameRepositoryImpl): GameRepository

    companion object {
        @SingleDigitRepo
        @Provides
        @Singleton
        fun provideSingleDigitRepo(): ProblemRepository {
            return SingleDigitProblemRepositoryImpl()
        }

        // 3. Provide Double Digit Implementation
        @DoubleDigitRepo
        @Provides
        @Singleton
        fun provideDoubleDigitRepo(): ProblemRepository {
            return DoubleDigitProblemRepositoryImpl() // Assuming you created this class
        }

        // 4. Provide the Wrapper (Provider) itself
        @Provides
        @Singleton
        fun provideRepositoryProvider(
            @SingleDigitRepo single: ProblemRepository,
            @DoubleDigitRepo double: ProblemRepository
        ): ProblemRepositoryProvider {
            return ProblemRepositoryProvider(single, double)
        }

//        // 5. CRITICAL: Provide the Wrapper as the default ProblemRepository interface
//        // This ensures GameViewModel gets the Wrapper when it asks for ProblemRepository
//        @Provides
//        @Singleton
//        fun provideDefaultProblemRepository(
//            provider: ProblemRepositoryProvider
//        ): ProblemRepository {
//            return provider
//        }
        @Provides
        @Singleton // Ensures only one instance of FirebaseAuth is created
        fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

        @Provides
        @Singleton
        fun provideWorkManager(@ApplicationContext context: Context): WorkManager {
            return WorkManager.getInstance(context)
        }

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
            ).addMigrations(MIGRATION_1_2).build()
        }

        @Provides
        @Singleton
        fun provideGameDao(database: AbacusDatabase): GameDao = database.gameDao()

        // --- Use Case Provider ---
        @Provides
        @Singleton
        fun provideStartNewGameUseCase(gameDao: GameDao): StartNewGameUseCase {
            // Getting current user ID will be handled inside the ViewModel now
            return StartNewGameUseCase(gameDao)
        }

        @Provides
        @Singleton
        fun provideSaveProblemResultUseCase(gameDao: GameDao): SaveProblemResultUseCase {
            return SaveProblemResultUseCase(gameDao)
        }

        @Provides
        @Singleton
        fun provideFinalizeGameUseCase(
            gameDao: GameDao,
            workManager: WorkManager
        ): FinalizeGameUseCase {
            return FinalizeGameUseCase(gameDao,workManager)
        }
    }
}