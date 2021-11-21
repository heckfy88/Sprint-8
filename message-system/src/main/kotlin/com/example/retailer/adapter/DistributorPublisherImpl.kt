package com.example.retailer.adapter

import com.example.retailer.api.distributor.Order
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.amqp.core.TopicExchange
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired

class DistributorPublisherImpl: DistributorPublisher {

    @Autowired
    lateinit var rabbitTemplate: RabbitTemplate

    @Autowired
    lateinit var topic: TopicExchange

    private val mapper = jacksonObjectMapper()

    override fun placeOrder(order: Order): Boolean {
        val orderMessage = mapper.writeValueAsString(order)
        rabbitTemplate.convertAndSend(topic.name, "distributor.placeOrder.heckfy88.${order.id}", orderMessage) {
            msg ->
            msg.messageProperties.headers["Notify-Exchange"] = "distributor_exchange"
            msg.messageProperties.headers["Notify-RountingKey"] = "retailer.heckfy88"
            msg
        }
        return true
    }
}