package com.platform.payout_service.services.payoutRequest.impl;

import com.platform.payout_service.dtos.payout.PayoutMessageDto;
import com.platform.payout_service.entities.PayoutRequestEntity;
import com.platform.payout_service.enums.PayoutStatus;
import com.platform.payout_service.messaging.producer.PayoutProducer;
import com.platform.payout_service.services.payoutRequest.IPayoutRequestPublishEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
/**
 * Service implementation for publishing payout request events.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class PayoutRequestPublishEventService implements IPayoutRequestPublishEventService {
    /** Producer for publishing payout events to RabbitMQ. */
    private final PayoutProducer payoutProducer;
    /**
     * Publishes an event when a payout request's status changes.
     *
     * @param payoutRequestEntity the payout request entity
     * @param previousStatus      the previous status of the payout request
     * @param newStatus           the new status of the payout request
     */
    @Override
    public void payoutRequestEventPublisher(PayoutRequestEntity payoutRequestEntity,
                                            PayoutStatus previousStatus, PayoutStatus newStatus) {
        log.trace("Publishing event for payout request ID: {}", payoutRequestEntity.getPayoutRequestId());
        try {
            PayoutMessageDto payoutMessageDto = PayoutMessageDto.builder()
                    .payoutId(payoutRequestEntity.getPayoutRequestId())
                    .ngoId(payoutRequestEntity.getOrganizationId())
                    .totalAmount(payoutRequestEntity.getTotalAmount())
                    .payoutStatus(newStatus)
                    .previousPayoutStatus(previousStatus)
                    .build();
            payoutProducer.publishPayoutEvent(payoutMessageDto);
        } catch (NullPointerException e) {
            log.error("❌ Error publishing payout request event for ID: {}", payoutRequestEntity.getPayoutRequestId(), e);
        }
    }
}
