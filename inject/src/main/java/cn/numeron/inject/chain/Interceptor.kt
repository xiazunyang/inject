package cn.numeron.inject.chain

fun interface Interceptor {

    fun intercept(chain: Chain): Any

    interface Chain {
        val target: Class<*>
        fun proceed(klass: Class<*>): Any
    }

}