package com.example.retailer.repository

import com.example.retailer.api.distributor.Order
import org.springframework.data.repository.CrudRepository


interface OrderRepo: CrudRepository<Order, String>