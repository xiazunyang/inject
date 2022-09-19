package cn.numeron.inject.room

import androidx.room.RoomDatabase
import cn.numeron.inject.InjectBuilderComponentScope

fun InjectBuilderComponentScope.addDaoProducerFactory(database: RoomDatabase) {
    addProducerFactory(DaoProducer.Factory(database))
}