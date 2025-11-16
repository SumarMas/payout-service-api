package com.platform.payout_service.services.payoutRequest;

import com.platform.payout_service.dtos.payout.AvailableDonationDto;
import com.platform.payout_service.dtos.payout.PayoutRequestDto;
import com.platform.payout_service.dtos.payout.proofFile.ProofFileDto;

import java.util.List;
import java.util.UUID;
/**
 * Service interface for managing payout requests.
 */
public interface IPayoutRequestService {
    /**
     * Retrieves the available donations for payout.
     * @return a list of AvailableDonationDto representing the available donations
     */
    List<AvailableDonationDto> getAvailableDonation();
    /**
     * Approves a payout request by its unique identifier.
     *
     * @param payoutRequestId the unique identifier
     *                        of the payout request to be approved
     * @param proofFileDto the proof file associated with the approval
     */
    void approvePayoutRequestById(UUID payoutRequestId, ProofFileDto proofFileDto);
    /**
     * Creates a new payout request with user context.
     *
     * @return the created PayoutRequestDto
     */
    PayoutRequestDto createPayoutRequest();
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
