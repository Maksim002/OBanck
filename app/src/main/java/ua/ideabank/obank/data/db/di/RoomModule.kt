package ua.ideabank.obank.data.db.di

import android.app.Application
import androidx.room.Room
import org.koin.dsl.module
import ua.ideabank.obank.data.db.dao.UserDao
import ua.ideabank.obank.data.db.RoomDatabase

//TODO Настроить Room, Dao в папке db и прописать нужные модули здесь
val databaseModule = module {
    fun provideDatabase(application: Application): RoomDatabase {
        return Room.databaseBuilder(application, RoomDatabase::class.java, "users")
            .fallbackToDestructiveMigration()
            .build()
    }

    fun provideDao(database: RoomDatabase): UserDao {
        return database.roomDao
    }

    single { provideDatabase(get()) }
    single { provideDao(get()) }
}
