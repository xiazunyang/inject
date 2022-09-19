package cn.numeron.inject.retrofit

import cn.numeron.inject.InjectBuilderComponentScope
import retrofit2.Retrofit

fun InjectBuilderComponentScope.addApiProducerFactory(retrofit: Retrofit) {
    addProducerFactory(ApiProducer.Factory(retrofit))
}