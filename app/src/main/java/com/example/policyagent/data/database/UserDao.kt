package com.example.policyagent.data.database
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.policyagent.data.database.entities.CURRENT_USER_ID
import com.example.policyagent.data.database.entities.User

@Dao
interface UserDao{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(user: User) : Long

    @Query("SELECT * FROM user WHERE uid = $CURRENT_USER_ID")
    fun getuser() : LiveData<User>

    @Query("DELETE FROM user")
    fun deleteUser()

}