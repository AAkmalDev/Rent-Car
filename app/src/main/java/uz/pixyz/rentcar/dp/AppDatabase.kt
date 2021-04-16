package uz.pixyz.rentcar.dp

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import uz.pixyz.rentcar.models.registration.User

@Database(entities = [User::class], version = 3)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        private var INSTANSE: AppDatabase? = null
        fun getDataBase(context: Context): AppDatabase {
            if (INSTANSE == null) {
                INSTANSE = Room.databaseBuilder(context, AppDatabase::class.java, "rentCarUser")
                    .allowMainThreadQueries()
                    .build()
            }
            return INSTANSE as AppDatabase
        }
    }

    abstract fun userDao(): UserDao

}