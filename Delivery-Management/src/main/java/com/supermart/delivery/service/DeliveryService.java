package com.supermart.delivery.service;

import com.supermart.delivery.dto.*;
import com.supermart.delivery.model.Delivery;
import com.supermart.delivery.model.DeliveryStatus;
import com.supermart.delivery.repository.DeliveryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DeliveryService {
    private final DeliveryRepository deliveryRepository;
    private final DeliveryMapperService deliveryMapperService;

    private final ValidateDeliveryStatusService validateDeliveryStatusService;

    private final CallOrderAPIService callOrderAPIService;


    public DeliveryResponse getDeliveryById(Integer id){
        Delivery delivery=deliveryRepository.findDeliveryById(id);
        return deliveryMapperService.mapToDeliveryResponse(delivery);
    }

    public DeliveryResponse getDeliveryByOrderNumber(String orderNumber){
        Delivery delivery=deliveryRepository.findDeliveryByOrderNumber(orderNumber);
        return deliveryMapperService.mapToDeliveryResponse(delivery);
    }

    public DeliveryResponse getDeliveryByShippingAddress(String shippingAddress){
        Delivery delivery=deliveryRepository.findDeliveryByShippingAddress(shippingAddress);
        return deliveryMapperService.mapToDeliveryResponse(delivery);
    }

    public List<DeliveryResponse> getDeliveriesByAssignedTo(Integer assignedTo){
        List<Delivery> deliveries=deliveryRepository.findDeliveriesByAssignedTo(assignedTo);
        return deliveryMapperService.mapToDeliveryResponseList(deliveries);
    }

    public List<DeliveryResponse> findDeliveriesByStatus(DeliveryStatus deliveryStatus){
        List<Delivery> deliveries=deliveryRepository.findDeliveriesByStatus(deliveryStatus);
        return deliveryMapperService.mapToDeliveryResponseList(deliveries);
    }

    public List<DeliveryResponse> findDeliveriesByDateRange(LocalDate startDate,LocalDate endDate){
        List<Delivery> deliveries=deliveryRepository.findDeliveriesByEstimatedDeliveryDateBetween(startDate, endDate);
        return deliveryMapperService.mapToDeliveryResponseList(deliveries);
    }

    public DeliveryResponse initiateDelivery(InitiateDeliveryRequest initiateDeliveryRequest){
        boolean isOrderNumberValid = callOrderAPIService.isOrderValid(initiateDeliveryRequest.getOrderNumber());
        if(!isOrderNumberValid){
            throw new RuntimeException("Invalid Order Number");
        }
        Delivery delivery=Delivery.builder()
                .orderNumber(initiateDeliveryRequest.getOrderNumber())
                .shippingAddress(initiateDeliveryRequest.getShippingAddress())
                .assignedTo(1)
                .status(DeliveryStatus.ASSIGNED)
                .estimatedDeliveryDate(LocalDate.now().plusDays(5))
                .build();
        deliveryRepository.save(delivery);
        return deliveryMapperService.mapToDeliveryResponse(delivery);
    }

    public String updateStatus(UpdateStatusRequest updateStatusRequest){
        Delivery delivery = deliveryRepository.findDeliveryByOrderNumber(updateStatusRequest.getOrderNumber());
        if (delivery == null) {
            return "Delivery not found for order number: " + updateStatusRequest.getOrderNumber();
        }
        boolean isUpdateValid= validateDeliveryStatusService.isStatusUpdateValid(
                delivery.getStatus(),
                updateStatusRequest.getNewStatus()
        );
        if(!isUpdateValid){
            return "Invalid status update from " + delivery.getStatus()
                    + " to " + updateStatusRequest.getNewStatus()
                    + " for order number: " + updateStatusRequest.getOrderNumber();
        }
        delivery.setStatus(updateStatusRequest.getNewStatus());
        deliveryRepository.save(delivery);
        return "Delivery status updated successfully for order number: " + updateStatusRequest.getOrderNumber();
    }

    public String cancelDelivery(String orderNumber) {
        Delivery delivery = deliveryRepository.findDeliveryByOrderNumber(orderNumber);
        if (delivery == null) {
            return "Delivery not found for order number: " + orderNumber;
        }
        boolean canCancel=validateDeliveryStatusService.isStatusUpdateValid(delivery.getStatus(),DeliveryStatus.CANCELLED);
        if(!canCancel){
            return "Delivery cancellation failed! Delivery already completed or cancelled";
        }
        delivery.setStatus(DeliveryStatus.CANCELLED);
        deliveryRepository.save(delivery);
        return "Delivery canceled successfully for order number: " + orderNumber;
    }

    public String completeDelivery(String orderNumber) {
        Delivery delivery = deliveryRepository.findDeliveryByOrderNumber(orderNumber);
        if (delivery == null) {
            return "Delivery not found for order number: " + orderNumber;
        }
        boolean isStatusUpdateValid= validateDeliveryStatusService.isStatusUpdateValid(delivery.getStatus(),DeliveryStatus.DELIVERED);
        if(!isStatusUpdateValid){
            return "Delivery completion failed! " +
                    "The current delivery status is not appropriate for completion." +
                    " Please check the delivery status and try again.";
        }
        delivery.setStatus(DeliveryStatus.DELIVERED);
        deliveryRepository.save(delivery);
        return "Delivery completed successfully for order number: " + orderNumber;
    }

    public String changeAssignee(ChangeAssigneeRequest changeAssigneeRequest){
        Delivery delivery = deliveryRepository.findDeliveryByOrderNumber(changeAssigneeRequest.getOrderNumber());
        if (delivery == null) {
            return "Delivery not found for order number: " + changeAssigneeRequest.getOrderNumber();
        }
        if(delivery.getStatus()==DeliveryStatus.DELIVERED || delivery.getStatus()==DeliveryStatus.CANCELLED){
            return "Assignee cannot be changed since delivery already completed or cancelled";
        }
        delivery.setAssignedTo(changeAssigneeRequest.getNewAssignee());
        deliveryRepository.save(delivery);
        return "Assignee changed successfully for delivery number: " + delivery.getId();
    }

    public String updateEstimatedDeliveryDate(UpdateEstimatedDateRequest updateEstimatedDateRequest){
        Delivery delivery = deliveryRepository.findDeliveryByOrderNumber(updateEstimatedDateRequest.getOrderNumber());
        if (delivery == null) {
            return "Delivery not found for order number: " + updateEstimatedDateRequest.getOrderNumber();
        }
        if(delivery.getStatus()==DeliveryStatus.DELIVERED || delivery.getStatus()==DeliveryStatus.CANCELLED){
            return "delivery date cannot be changed since delivery already completed or cancelled";
        }
        if (updateEstimatedDateRequest.getNewDate().isBefore(LocalDate.now())) {
            return "Invalid update! The new estimated delivery date cannot be set to a past date.";
        }
        delivery.setEstimatedDeliveryDate(updateEstimatedDateRequest.getNewDate());
        deliveryRepository.save(delivery);
        return "Estimated delivery date updated successfully for order number: " + updateEstimatedDateRequest.getOrderNumber();
    }



    //todo:assignee

}
