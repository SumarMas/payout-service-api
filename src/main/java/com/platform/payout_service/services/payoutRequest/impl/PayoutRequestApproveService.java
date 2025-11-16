package com.platform.payout_service.services.payoutRequest.impl;

import com.platform.payout_service.context.IContextService;
import com.platform.payout_service.controllers.manageExceptions.CustomException;
import com.platform.payout_service.dtos.payout.proofFile.ProofFileDto;
import com.platform.payout_service.entities.PayoutRequestEntity;
import com.platform.payout_service.enums.PayoutStatus;
import com.platform.payout_service.repositories.PayoutRequestRepository;
import com.platform.payout_service.services.payoutRequest.IPayoutRequestApproveService;
import com.platform.payout_service.services.payoutRequest.IPayoutRequestPublishEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Service implementation for approving payout requests.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class PayoutRequestApproveService implements IPayoutRequestApproveService {
    /** Repository for accessing payout request data. */
    private final PayoutRequestRepository repository;
    /** Service for accessing context information. */
    private final IContextService contextService;
    /** Service for publishing payout request events. */
    private final IPayoutRequestPublishEventService payoutRequestPublishEventService;
    /**
     * Approves a payout request by its unique identifier.
     *
     * @param payoutRequestId the unique identifier
     *                        of the payout request to be approved
     * @param proofFileDto the proof file associated with the approval
     */
    @Override
    public void approvePayoutRequestById(UUID payoutRequestId, ProofFileDto proofFileDto) {
        log.trace("approvePayoutRequestById {}", payoutRequestId);
        isAdmin();
        UUID userId = getCurrentUserId();
        PayoutRequestEntity payoutRequestEntity = getPayoutRequestEntityById(payoutRequestId);
        if (payoutRequestEntity.getStatus() != null && payoutRequestEntity.getStatus().equals(PayoutStatus.APPROVED)) {
            log.error("Payout request with id: {} is already approved", payoutRequestId);
            throw new CustomException("Payout Request is already approved", HttpStatus.BAD_REQUEST);
        }
        payoutRequestEntity.setStatus(PayoutStatus.APPROVED);
        payoutRequestEntity.setLastUpdatedUser(userId);
        payoutRequestEntity.setApprovalDatetime(LocalDateTime.now());
        payoutRequestEntity.setProofFileId(proofFileDto.getFileId());
        savePayoutRequestEntity(payoutRequestEntity);
        publishApprovedEvent(payoutRequestEntity);
    }

    private void isAdmin() {
        if (!contextService.isAdmin()) {
            log.error("User is not admin");
            throw new CustomException("Unauthorized", HttpStatus.UNAUTHORIZED);
        }
    }

    private UUID getCurrentUserId() {
        return contextService.getUserId();
    }

    private PayoutRequestEntity getPayoutRequestEntityById(UUID payoutRequestId) {
        return repository.findByPayoutRequestIdAndEnabledTrue(payoutRequestId).orElseThrow(() -> {
            log.error("Payout request not found with id: {}", payoutRequestId);
            return new CustomException("Payout Request Not Found", HttpStatus.NOT_FOUND);
        });
    }

    private void savePayoutRequestEntity(PayoutRequestEntity entity) {
        try {
            repository.save(entity);
        } catch (DataAccessException e) {
            log.error("Error saving payout request entity: {}", e.getMessage(), e);
            throw new CustomException("An error occurred when saved payout request, try again later",
                    HttpStatus.INTERNAL_SERVER_ERROR, e);
        }

    }

    private void publishApprovedEvent(PayoutRequestEntity payoutRequestEntity) {
        payoutRequestPublishEventService.payoutRequestEventPublisher(
                payoutRequestEntity,
                PayoutStatus.PENDING,
                PayoutStatus.APPROVED
        );
    }
}
