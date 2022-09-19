package cn.numeron.inject.chain

class CacheInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Any {
        val klass = chain.target
        // 尝试从缓存中取出对象实例
        return INSTANCES.getOrPut(klass.name) {
            // 创建实例并放入缓存中
            chain.proceed(klass)
        }
    }

    companion object {

        private val INSTANCES = mutableMapOf<String, Any>()

    }

}