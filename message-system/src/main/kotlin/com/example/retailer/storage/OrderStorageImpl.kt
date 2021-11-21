package com.example.retailer.storage

import com.example.retailer.api.distributor.Order
import com.example.retailer.api.distributor.OrderInfo
import com.example.retailer.api.distributor.OrderStatus
import com.example.retailer.repository.OrderInfoRepo
import com.example.retailer.repository.OrderRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class OrderStorageImpl: OrderStorage {

    @Autowired
    lateinit var orderRepo: OrderRepo

    @Autowired
    lateinit var orderInfoRepo: OrderInfoRepo

    override fun createOrder(draftOrder: Order): PlaceOrderData {
        val order = orderRepo.save(draftOrder)

        val orderInfo = orderInfoRepo.save(OrderInfo(order.id!!, OrderStatus.SENT, ""))
        return PlaceOrderData(order, orderInfo)
    }

    override fun updateOrder(order: OrderInfo): Boolean {
       if (orderInfoRepo.findById(order.orderId).isPresent) {
           orderInfoRepo.save(order)
           return true
       }
        return false
    }

    override fun getOrderInfo(id: String): OrderInfo? {
        val orderInfo = orderInfoRepo.findById(id)
        if (orderInfo.isPresent) {
            return orderInfo.get()
        }
        return null
    }


}