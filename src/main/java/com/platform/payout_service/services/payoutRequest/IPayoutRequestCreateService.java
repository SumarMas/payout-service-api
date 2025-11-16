package com.platform.payout_service.services.payoutRequest;

import com.platform.payout_service.dtos.payout.PayoutRequestDto;
/**
 * Service interface for creating payout requests.
 */
public interface IPayoutRequestCreateService {
    /**
     * Creates a new payout request with user context.
     *
     * @return the created PayoutRequestDto
     */
    PayoutRequestDto createPayoutRequest();
}
