package com.platform.payout_service.services.payoutRequest;

import com.platform.payout_service.dtos.payout.proofFile.ProofFileDto;

import java.util.UUID;

/**
 * Service interface for approving payout requests.
 */
public interface IPayoutRequestApproveService {
    /**
     * Approves a payout request by its unique identifier.
     *
     * @param payoutRequestId the unique identifier
     *                        of the payout request to be approved
     * @param proofFileDto the proof file associated with the approval
     */
    void approvePayoutRequestById(UUID payoutRequestId, ProofFileDto proofFileDto);
}
