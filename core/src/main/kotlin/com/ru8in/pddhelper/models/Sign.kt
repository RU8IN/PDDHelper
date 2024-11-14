package com.ru8in.pddhelper.models

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder


@Serializable(Signs.SignGroupSerializer::class)
data class Signs(
    val signs: Map<String, List<Sign>>
) {
    internal object SignGroupSerializer : KSerializer<Signs> {
        private val signsSerializer =
            MapSerializer(String.serializer(), MapSerializer(String.serializer(), Sign.serializer()))
        override val descriptor: SerialDescriptor
            get() = signsSerializer.descriptor

        override fun deserialize(decoder: Decoder): Signs {
            return Signs(signsSerializer.deserialize(decoder).mapValues { (_, innerMap) -> innerMap.values.toList() })
        }

        override fun serialize(encoder: Encoder, value: Signs) {

            val outputMap = mutableMapOf<String, MutableMap<String, Sign>>()

            for ((key, signList) in value.signs) {
                val innerMap = mutableMapOf<String, Sign>()
                for (sign in signList) {
                    innerMap[sign.number] = sign
                }
                outputMap[key] = innerMap
            }
            return signsSerializer.serialize(encoder, outputMap)
        }
    }

    fun getSignByNumber(number: String): Sign? {
        return this.signs.values.flatten().find { it.number == number }
    }
}

@Serializable()
data class Sign(
    val number: String,
    val title: String,
    @SerialName("image") val imagePath: String,
    val description: String?
)
