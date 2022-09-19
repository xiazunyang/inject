package cn.numeron.inject.chain

class InjectChain internal constructor(
    override val target: Class<*>,
    private val interceptors: List<Interceptor>,
    private val index: Int = 0,
) : Interceptor.Chain {

    override fun proceed(klass: Class<*>): Any {
        val nextInjectChain = InjectChain(klass, interceptors, index + 1)
        return interceptors[index].intercept(nextInjectChain)
    }

}