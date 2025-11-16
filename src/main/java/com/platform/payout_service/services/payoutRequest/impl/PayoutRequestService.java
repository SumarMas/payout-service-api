package com.platform.payout_service.services.payoutRequest.impl;

import com.platform.payout_service.dtos.payout.AvailableDonationDto;
import com.platform.payout_service.dtos.payout.PayoutRequestDto;
import com.platform.payout_service.dtos.payout.proofFile.ProofFileDto;
import com.platform.payout_service.services.payoutRequest.IAvailableDonationService;
import com.platform.payout_service.services.payoutRequest.IPayoutRequestApproveService;
import com.platform.payout_service.services.payoutRequest.IPayoutRequestCreateService;
import com.platform.payout_service.services.payoutRequest.IPayoutRequestGetService;
import com.platform.payout_service.services.payoutRequest.IPayoutRequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
/**
 * Service implementation for managing payout requests.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class PayoutRequestService implements IPayoutRequestService {
    /** * Service for retrieving payout requests. */
    private final IPayoutRequestGetService payoutRequestGetService;
    /** * Service for creating payout requests. */
    private final IPayoutRequestCreateService payoutRequestCreateService;
    /**  * Service for retrieving available donations. */
    private final IAvailableDonationService availableDonationService;
    /** * Service for approving payout requests. */
    private final IPayoutRequestApproveService payoutRequestApproveService;
    /**
     * Retrieves the available donations for payout.
     *
     * @return a list of AvailableDonationDto representing the available donations
     */
    @Override
    public List<AvailableDonationDto> getAvailableDonation() {
        log.trace("getAvailableDonation()");
        return availableDonationService.getAvailableDonation();
    }

    /**
     * Approves a payout request by its unique identifier.
     *
     * @param payoutRequestId the unique identifier
     *                        of the payout request to be approved
     * @param proofFileDto the proof file associated with the approval
     */
    @Override
    public void approvePayoutRequestById(UUID payoutRequestId, ProofFileDto proofFileDto) {
        log.trace("approvePayoutRequestById({})", payoutRequestId);
        payoutRequestApproveService.approvePayoutRequestById(payoutRequestId, proofFileDto);
    }

    /**
     * Creates a new payout request with user context.
     *
     * @return the created PayoutRequestDto
     */
    @Override
    public PayoutRequestDto createPayoutRequest() {
        log.trace("createPayoutRequest()");
        return payoutRequestCreateService.createPayoutRequest();
    }

    /**
     * Retrieves a payout request by its unique identifier.
     *
     * @param id the unique identifier of the payout request
     * @return the PayoutRequestDto if found
     */
    @Override
    public PayoutRequestDto getPayoutRequestById(UUID id) {
        log.trace("getPayoutRequestById({})", id);
        return payoutRequestGetService.getPayoutRequestById(id);
    }

    /**
     * Retrieves all payout requests associated with a specific NGO.
     *
     * @param id the unique identifier of the NGO
     * @return a list of PayoutRequestDto associated with the NGO
     */
    @Override
    public List<PayoutRequestDto> getPayoutRequestByNgoId(UUID id) {
        log.trace("getPayoutRequestByNgoId({})", id);
        return payoutRequestGetService.getPayoutRequestByNgoId(id);
    }

    /**
     * Retrieves all payout requests associated with the NGO of the current user.
     *
     * @return a list of PayoutRequestDto associated with the user's NGO
     */
    @Override
    public List<PayoutRequestDto> getPayoutRequestByMyNgo() {
        log.trace("getPayoutRequestByMyNgo()");
        return payoutRequestGetService.getPayoutRequestByMyNgo();
    }

    /**
     * Retrieves all payout requests that are currently pending.
     *
     * @return a list of PayoutRequestDto that are pending
     */
    @Override
    public List<PayoutRequestDto> getAllPayoutRequestsPending() {
        log.trace("getAllPayoutRequestsPending()");
        return payoutRequestGetService.getAllPayoutRequestsPending();
    }
}
