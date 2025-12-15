package com.example.santaymahs.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface AppDao {
    @Insert
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM user_table WHERE email = :email AND password = :pass LIMIT 1")
    suspend fun loginUser(email: String, pass: String): User?

    @Query("SELECT * FROM user_table WHERE id = :uid")
    suspend fun getUserById(uid: Int): User?

    @Insert
    suspend fun insertHasil(hasil: HasilTes)

    @Query("SELECT * FROM hasil_tes_table WHERE userId = :uid ORDER BY tanggalTes DESC")
    fun getAllHistory(uid: Int): LiveData<List<HasilTes>>

    @Query("SELECT * FROM hasil_tes_table WHERE userId = :uid ORDER BY tanggalTes DESC LIMIT 1")
    fun getLatestResult(uid: Int): LiveData<HasilTes>

    @Insert
    suspend fun insertMood(mood: MoodEntry)

    @Query("SELECT * FROM mood_table WHERE userId = :uid AND tanggal >= :startOfDay AND tanggal < :endOfDay LIMIT 1")
    suspend fun getMoodToday(uid: Int, startOfDay: Long, endOfDay: Long): MoodEntry?

    @androidx.room.Update
    suspend fun updateUser(user: User)
}