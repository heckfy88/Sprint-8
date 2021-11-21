package com.example.retailer.config

import com.example.retailer.adapter.DistributorPublisher
import com.example.retailer.adapter.DistributorPublisherImpl
import com.example.retailer.adapter.RetailerConsumer
import com.example.retailer.adapter.RetailerConsumerImpl
import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.Queue
import org.springframework.amqp.core.TopicExchange
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AppMQConfig {

    @Bean
    fun publisher(): DistributorPublisher {
        return DistributorPublisherImpl()
    }

    @Bean
    fun consumer(): RetailerConsumer {
        return RetailerConsumerImpl()
    }

    @Bean
    fun topic(): TopicExchange {
        return TopicExchange("distributor_exchange", true, false)
    }

    @Bean
    fun autoDeleteQueue(): Queue {
        return Queue("retailer", false, false, true)
    }

    @Bean
    fun bindRetailer(
        topic: TopicExchange,
        autoDeleteQueue: Queue): Binding {
        return BindingBuilder
            .bind(autoDeleteQueue)
            .to(topic)
            .with("retailer.heckfy88.#")
    }

}