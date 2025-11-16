package com.platform.payout_service.services.payoutRequest;

import com.platform.payout_service.dtos.payout.PayoutRequestDto;

import java.util.List;
import java.util.UUID;
/**
 * Service interface for retrieving payout requests.
 */
public interface IPayoutRequestGetService {
    /**
     * Retrieves a payout request by its unique identifier.
     *
     * @param id the unique identifier of the payout request
     * @return the PayoutRequestDto if found
     */
    PayoutRequestDto getPayoutRequestById(UUID id);
    /**
     * Retrieves all payout requests associated with a specific NGO.
     *
     * @param id the unique identifier of the NGO
     * @return a list of PayoutRequestDto associated with the NGO
     */
    List<PayoutRequestDto> getPayoutRequestByNgoId(UUID id);
    /**
     * Retrieves all payout requests associated with the NGO of the current user.
     *
     * @return a list of PayoutRequestDto associated with the user's NGO
     */
    List<PayoutRequestDto> getPayoutRequestByMyNgo();

    /**
     * Retrieves all payout requests that are currently pending.
     *
     * @return a list of PayoutRequestDto that are pending
     */
    List<PayoutRequestDto> getAllPayoutRequestsPending();
}
