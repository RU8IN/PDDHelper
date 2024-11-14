package com.ru8in.pddhelper.models

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(Ticket.Serializer::class)
data class Ticket(
    val questions: List<Question>
) {
    internal object Serializer: KSerializer<Ticket> {
        private val listSerializer = ListSerializer(Question.serializer())

        override val descriptor: SerialDescriptor
            get() = this.listSerializer.descriptor

        override fun deserialize(decoder: Decoder): Ticket {
            return Ticket(this.listSerializer.deserialize(decoder))
        }

        override fun serialize(encoder: Encoder, value: Ticket) {
            listSerializer.serialize(encoder, value.questions)
        }

    }
}
