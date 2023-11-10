package com.supermart.delivery.dto;

import com.supermart.delivery.model.DeliveryStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateStatusRequest {
    private String orderNumber;
    private DeliveryStatus newStatus;
}
