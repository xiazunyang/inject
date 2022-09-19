package cn.numeron.inject

import cn.numeron.inject.chain.InjectChain
import cn.numeron.inject.chain.InjectInterceptor
import cn.numeron.inject.chain.Interceptor
import cn.numeron.inject.chain.SingletonInterceptor
import kotlin.reflect.KProperty

class Inject private constructor(
    producerFactories: List<Producer.Factory>,
    private val interceptors: List<Interceptor>
) {

    private val internalInterceptors = listOf(
        SingletonInterceptor(),
        InjectInterceptor(producerFactories)
    )

    operator fun <T : Any> get(klass: Class<T>): T {
        val injectChain = InjectChain(klass, interceptors + internalInterceptors)
        val instance = injectChain.proceed(klass)
        return klass.cast(instance)
    }

    inline fun <reified T : Any> get(): T = get(T::class.java)

    operator fun <T : Any> getValue(ref: Any?, kProperty: KProperty<*>): T {
        if (ref != null) {
            val clazz = ref.javaClass
            var methodName = kProperty.name
            val key = clazz.canonicalName + "#" + methodName
            val type = REFLECTIONS.getOrPut(key) {
                methodName = "get" + methodName.first().uppercase() + methodName.substring(1)
                val method = clazz.getDeclaredMethod(methodName)
                method.isAccessible = true
                method.returnType
            }
            @Suppress("UNCHECKED_CAST")
            return get(type) as T
        }
        throw RuntimeException("Can't inject global property.")
    }

    class Builder {

        private val interceptors = mutableListOf<Interceptor>()
        private val producerFactories = mutableListOf<Producer.Factory>()

        private val componentScope = InjectBuilderComponentScope { producerFactories += it }

        fun addComponent(componentBuilder: InjectBuilderComponentScope.() -> Unit): Builder {
            componentBuilder(componentScope)
            return this
        }

        fun addInterceptor(interceptor: Interceptor): Builder {
            interceptors += interceptor
            return this
        }

        fun build(): Inject {
            producerFactories += ConstructorProducer.Factory(producerFactories)
            return Inject(producerFactories, interceptors)
        }

    }

    companion object {

        private val REFLECTIONS = mutableMapOf<String, Class<*>>()

    }

}