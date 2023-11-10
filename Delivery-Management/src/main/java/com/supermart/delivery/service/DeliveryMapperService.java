package com.supermart.delivery.service;

import com.supermart.delivery.dto.DeliveryResponse;
import com.supermart.delivery.model.Delivery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class DeliveryMapperService {
    public DeliveryResponse mapToDeliveryResponse(Delivery delivery) {
        return DeliveryResponse.builder()
                .id(delivery.getId())
                .orderNumber(delivery.getOrderNumber())
                .assignedTo(delivery.getAssignedTo())
                .shippingAddress(delivery.getShippingAddress())
                .status(delivery.getStatus())
                .build();
    }

    public List<DeliveryResponse> mapToDeliveryResponseList(List<Delivery> deliveryList) {
        return deliveryList.stream()
                .map(this::mapToDeliveryResponse)
                .collect(Collectors.toList());
    }
}
