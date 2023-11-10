package com.supermart.delivery.controller;

import com.supermart.delivery.dto.*;
import com.supermart.delivery.model.DeliveryStatus;
import com.supermart.delivery.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("api/delivery")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;

    @GetMapping("/{id}")
    public DeliveryResponse getDeliveryById(@PathVariable Integer id){
        return deliveryService.getDeliveryById(id);
    }

    @GetMapping("/order-number/{order-number}")
    public DeliveryResponse getDeliveryByOrderNumber(@PathVariable("order-number") String orderNumber){
        return deliveryService.getDeliveryByOrderNumber(orderNumber);
    }

    @GetMapping("/shipping-address/{shipping-address}")
    public DeliveryResponse getDeliveryByShippingAddress(@PathVariable("shipping-address") String shippingAddress){
        return deliveryService.getDeliveryByShippingAddress(shippingAddress);
    }

    @GetMapping("/assignee/{assigned-to}")
    public List<DeliveryResponse> getDeliveriesByAssignedTo(@PathVariable("assigned-to") Integer assignedTo){
        return deliveryService.getDeliveriesByAssignedTo(assignedTo);
    }

    @GetMapping("/status/{delivery-status}")
    public List<DeliveryResponse> findDeliveriesByStatus(@PathVariable("delivery-status") DeliveryStatus deliveryStatus){
        return deliveryService.findDeliveriesByStatus(deliveryStatus);
    }

    @GetMapping("/date/{start-date}/{end-date}")
    public List<DeliveryResponse> findDeliveriesByDateRange(
            @PathVariable("start-date") LocalDate startDate,
            @PathVariable("end-date") LocalDate endDate
    ){
        return deliveryService.findDeliveriesByDateRange(startDate, endDate);
    }

    @PostMapping("/initiate")
    public DeliveryResponse initiateDelivery(@RequestBody InitiateDeliveryRequest initiateDeliveryRequest){
        return deliveryService.initiateDelivery(initiateDeliveryRequest);
    }

    @PatchMapping("/status")
    public String updateStatus(@RequestBody UpdateStatusRequest updateStatusRequest){
        return deliveryService.updateStatus(updateStatusRequest);
    }

    @PatchMapping("/cancel/{order-number}")
    public String cancelDelivery(@PathVariable("order-number") String orderNumber){
        return deliveryService.cancelDelivery(orderNumber);
    }

    @PatchMapping("/complete/{order-number}")
    public String completeDelivery(@PathVariable("order-number") String orderNumber){
        return deliveryService.completeDelivery(orderNumber);
    }

    @PatchMapping("/assignee")
    public String changeAssignee(@RequestBody ChangeAssigneeRequest changeAssigneeRequest){
        return deliveryService.changeAssignee(changeAssigneeRequest);
    }

    @PatchMapping("/date")
    public String updateEstimatedDeliveryDate(@RequestBody UpdateEstimatedDateRequest updateEstimatedDateRequest){
        return deliveryService.updateEstimatedDeliveryDate(updateEstimatedDateRequest);
    }


}
