package com.supermart.delivery.repository;

import com.supermart.delivery.model.Delivery;
import com.supermart.delivery.model.DeliveryStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface DeliveryRepository extends JpaRepository<Delivery,Integer> {
    Delivery findDeliveryById(Integer id);
    Delivery findDeliveryByOrderNumber(String orderNumber);
    Delivery findDeliveryByShippingAddress(String shippingAddress);
    List<Delivery> findDeliveriesByAssignedTo(Integer assignedTo);
    List<Delivery> findDeliveriesByStatus(DeliveryStatus status);

    List<Delivery> findDeliveriesByEstimatedDeliveryDateBetween(LocalDate startDate,LocalDate endDate);
}
