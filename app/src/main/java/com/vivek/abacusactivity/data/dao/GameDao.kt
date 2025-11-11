package com.vivek.abacusactivity.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.vivek.abacusactivity.data.entity.GameEntity
import com.vivek.abacusactivity.data.entity.GameWithProblems
import com.vivek.abacusactivity.data.entity.ProblemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GameDao {

    // Inserts a single GameEntity and returns the new game's ID
    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertGame(game: GameEntity): Long // Returns the auto-generated ID

    // Inserts a list of problems
    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertProblems(problems: List<ProblemEntity>)

    // Gets all game sessions, ordered by most recent
    @Query("SELECT * FROM games ORDER BY timestamp DESC")
    fun getAllGames(): Flow<List<GameEntity>>

    // Transaction to get a single game with all its related problems
    @Transaction
    @Query("SELECT * FROM games WHERE gameId = :gameId")
    fun getGameWithProblems(gameId: Long): Flow<GameWithProblems?>


}