package com.supermart.delivery.dto;

import com.supermart.delivery.model.DeliveryStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryResponse {
    private Integer id;
    private String orderNumber;
    private String shippingAddress;
    private Integer assignedTo;
    private DeliveryStatus status;
}
