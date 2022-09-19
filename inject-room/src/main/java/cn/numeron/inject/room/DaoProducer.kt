package cn.numeron.inject.room

import androidx.room.RoomDatabase
import cn.numeron.inject.Producer
import java.lang.reflect.Method
import java.lang.reflect.Modifier

internal class DaoProducer<T>(
    private val database: RoomDatabase,
    private val daoMethod: Method,
    private val klass: Class<T>
) : Producer<T> {

    override fun produce(): T {
        val instance = daoMethod.invoke(database)
        return klass.cast(instance)!!
    }

    class Factory(private val database: RoomDatabase) : Producer.Factory {

        private val daoMethods = database.javaClass.methods.filter(::isAbstractReturnType)

        override fun <T> getProducer(klass: Class<T>): Producer<T>? {
            val method = daoMethods.find {
                it.returnType == klass
            }
            if (method != null) {
                return DaoProducer(database, method, klass)
            }
            return null
        }

        private fun isAbstractReturnType(method: Method): Boolean {
            return Modifier.isAbstract(method.returnType.modifiers)
        }

    }

}