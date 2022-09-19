package cn.numeron.inject.chain

import cn.numeron.inject.Producer

class InjectInterceptor(private val producerFactories: List<Producer.Factory>) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Any {
        val klass = chain.target
        val injector = producerFactories.firstNotNullOfOrNull {
            it.getProducer(klass)
        } ?: throw IllegalStateException()
        return injector.produce()
    }

}