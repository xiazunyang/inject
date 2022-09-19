package cn.numeron.inject

import java.lang.reflect.Constructor
import java.lang.reflect.Modifier

internal class ConstructorProducer<T>(
    private val constructor: Constructor<T>,
    private val parameterProducers: List<Producer<*>>
) : Producer<T> {

    override fun produce(): T {
        val parameters = parameterProducers.mapNotNull(Producer<*>::produce).toTypedArray()
        return constructor.newInstance(*parameters)
    }

    internal class Factory(private val factories: List<Producer.Factory>) : Producer.Factory {

        override fun <T> getProducer(klass: Class<T>): Producer<T>? {
            if (Modifier.isAbstract(klass.modifiers)) {
                // 如果返回类型是抽象的，不支持
                return null
            }
            return klass.constructors
                .filterIsInstance<Constructor<T>>()
                .firstNotNullOfOrNull(::getInjector)
        }

        private fun <T> getInjector(constructor: Constructor<T>): Producer<T>? {
            val list = mutableListOf<Producer<*>>()
            for (klass in constructor.parameterTypes) {
                val injector = factories.firstNotNullOfOrNull {
                    it.getProducer(klass)
                } ?: return null
                list.add(injector)
            }
            return ConstructorProducer(constructor, list)
        }

    }

}