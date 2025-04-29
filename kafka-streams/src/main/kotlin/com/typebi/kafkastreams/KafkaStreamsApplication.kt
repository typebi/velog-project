package com.typebi.kafkastreams

import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.KafkaStreams
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.StreamsConfig
import java.util.*

fun main() {
    val props = Properties().apply {
        put(StreamsConfig.APPLICATION_ID_CONFIG, "kotlin-streams-app")
        put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
        put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String()::class.java.name)
        put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String()::class.java.name)
    }

    val builder = StreamsBuilder()

    val inputStream = builder.stream<String, String>("input-topic")

    inputStream
        .mapValues { value -> value.uppercase() }
        .to("output-topic")

    val streams = KafkaStreams(builder.build(), props)

    streams.start()

    Runtime.getRuntime().addShutdownHook(Thread(streams::close))
}
