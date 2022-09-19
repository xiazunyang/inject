package cn.numeron.inject

class InstanceProducer<T>(private val instance: T) : Producer<T> {

    override fun produce(): T {
        return instance
    }

    class Factory(private val instance: Any) : Producer.Factory {

        override fun <T> getProducer(klass: Class<T>): Producer<T>? {
            if (klass.isInstance(instance)) {
                return InstanceProducer(klass.cast(instance))
            }
            return null
        }

    }


}