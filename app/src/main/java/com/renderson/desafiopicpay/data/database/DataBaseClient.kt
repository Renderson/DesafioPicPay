package com.renderson.desafiopicpay.data.database

import android.content.Context
import androidx.room.Room

class DataBaseClient private constructor(context: Context) {

    val appDatabase: ManagerDao = Room.databaseBuilder(context,
        ManagerDao::class.java, "picpay_database")
        .allowMainThreadQueries()
        .fallbackToDestructiveMigration()
        .build()

    fun destroyInstance() {
        INSTANCE = null
    }

    companion object {
        private var INSTANCE: DataBaseClient? = null

        @Synchronized
        fun getInstance(mCtx: Context): DataBaseClient? {
            if (INSTANCE == null) {
                INSTANCE = DataBaseClient(mCtx)
            }
            return INSTANCE
        }
    }
}