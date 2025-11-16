package com.platform.payout_service.services.payoutRequest;

import com.platform.payout_service.entities.PayoutRequestEntity;
import com.platform.payout_service.enums.PayoutStatus;
/**
 * Service interface for publishing events related to payout requests.
 */
public interface IPayoutRequestPublishEventService {
    /**
     * Publishes an event when a payout request's status changes.
     *
     * @param payoutRequestEntity the payout request entity
     * @param previousStatus      the previous status of the payout request
     * @param newStatus           the new status of the payout request
     */
    void payoutRequestEventPublisher(PayoutRequestEntity payoutRequestEntity,
                                     PayoutStatus previousStatus,
                                     PayoutStatus newStatus);
}
