package cn.numeron.inject.retrofit

import cn.numeron.inject.Producer
import retrofit2.Retrofit

internal class ApiProducer<T>(
    private val klass: Class<T>,
    private val retrofit: Retrofit
) : Producer<T> {

    override fun produce(): T {
        return retrofit.create(klass)
    }

    class Factory(private val retrofit: Retrofit) : Producer.Factory {

        override fun <T> getProducer(klass: Class<T>): Producer<T>? {
            if (klass.isInterface && klass.interfaces.isEmpty()) {
                return ApiProducer(klass, retrofit)
            }
            return null
        }

    }

}