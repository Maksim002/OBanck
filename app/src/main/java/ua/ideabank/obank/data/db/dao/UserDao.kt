package ua.ideabank.obank.data.db.dao

import androidx.room.*
import ua.ideabank.obank.data.db.entity.UserDbModel

@Dao
interface UserDao{

    @Query("SELECT * FROM users")
    fun getAllModel():List<UserDbModel>

    @Insert
    fun insert(word: UserDbModel)

    @Query("DELETE FROM users ")
    fun deleteAllModel()

    @Insert
    fun insertModel(model: UserDbModel)

    @Delete
    fun deleteWord(word: UserDbModel)

    @Update
    fun update(word: UserDbModel)
}