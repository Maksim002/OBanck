package ua.ideabank.obank.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ua.ideabank.obank.data.db.dao.UserDao
import ua.ideabank.obank.data.db.entity.UserDbModel

@Database(
    entities = [ UserDbModel::class ],
    version = 2,
    exportSchema = false
)
abstract class RoomDatabase: RoomDatabase() {
    abstract val roomDao: UserDao
}