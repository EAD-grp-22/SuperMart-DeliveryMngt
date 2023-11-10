package com.supermart.delivery.service;

import com.supermart.delivery.model.DeliveryStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ValidateDeliveryStatusService {

    public boolean isStatusUpdateValid(DeliveryStatus currentStatus, DeliveryStatus newStatus) {
        switch (currentStatus) {
            case ASSIGNED:
                return newStatus == DeliveryStatus.IN_PROGRESS || newStatus == DeliveryStatus.CANCELLED;
            case IN_PROGRESS:
                return newStatus == DeliveryStatus.DELIVERED || newStatus == DeliveryStatus.CANCELLED;
            default:
                return false;
        }
    }
}
