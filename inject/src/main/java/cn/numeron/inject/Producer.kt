package cn.numeron.inject

/** 创建或获取指定类型实例的生产者 */
fun interface Producer<T> {

    /** 创建或获取该实例 */
    fun produce() : T

    /** 生产者的工厂类 */
    interface Factory {

        /** 判断是否支持[Class]实例的创建，如果支持，则返回该类型的生产者，如果不支持，则返回null */
        fun <T> getProducer(klass: Class<T>): Producer<T>?

    }

}