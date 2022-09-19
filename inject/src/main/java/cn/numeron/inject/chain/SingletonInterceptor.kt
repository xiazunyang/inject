package cn.numeron.inject.chain

import cn.numeron.inject.Singleton

class SingletonInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Any {
        val klass = chain.target
        val singleton = klass.getAnnotation(Singleton::class.java)
        if (singleton != null) {
            // 尝试从缓存中取出对象实例
            val instance = INSTANCES[klass.name]
            if (klass.isInstance(instance)) {
                return klass.cast(instance)
            }
        }
        // 根据链路创建对象并缓存
        val instance = chain.proceed(klass)
        INSTANCES[klass.name] = instance
        return instance
    }

    companion object {

        private val INSTANCES = mutableMapOf<String, Any>()

    }

}