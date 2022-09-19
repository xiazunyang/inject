package cn.numeron.inject

fun interface InjectBuilderComponentScope {

    fun addProducerFactory(producerFactory: Producer.Factory)

    companion object {

        fun InjectBuilderComponentScope.addInstanceProducerFactory(instance: Any) {
            addProducerFactory(InstanceProducer.Factory(instance))
        }

    }

}