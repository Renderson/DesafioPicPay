package com.renderson.desafiopicpay.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.renderson.desafiopicpay.data.database.dao.CreditCardDAO
import com.renderson.desafiopicpay.data.database.entity.CardEntity

@Database(entities = [CardEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract val creditCardDAO: CreditCardDAO

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            synchronized(this) {
                var instance: AppDatabase? = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context,
                        AppDatabase::class.java,
                        "app_database"
                    )
                        .allowMainThreadQueries()
                        .fallbackToDestructiveMigration()
                        .build()
                }

                return instance
            }
        }
    }
}