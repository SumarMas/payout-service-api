package com.platform.payout_service.controllers;

import com.platform.payout_service.dtos.payout.AvailableDonationDto;
import com.platform.payout_service.dtos.payout.PayoutRequestDto;
import com.platform.payout_service.dtos.payout.proofFile.ProofFileDto;
import com.platform.payout_service.services.payoutRequest.IPayoutRequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * Controller for handling payout-related API requests.
 */
@RestController
@RequestMapping("/api/v1/payouts")
@Slf4j
@RequiredArgsConstructor
public class PayoutController {
    /** * Service for managing payout requests. */
    private final IPayoutRequestService payoutRequestService;

    /**
     * Creates a new payout request.
     *
     * @return a ResponseEntity containing the created PayoutRequestDto
     */
    @PostMapping("/request")
    public ResponseEntity<PayoutRequestDto> createPayoutRequest() {
        log.trace("createPayoutRequest");
        PayoutRequestDto payoutRequestDto = payoutRequestService.createPayoutRequest();
        return ResponseEntity.ok(payoutRequestDto);
    }

    /**
     * Retrieves a list of available donations for payout.
     *
     * @return a ResponseEntity containing a list of AvailableDonationDto
     */
    @GetMapping("/available-donations")
    public ResponseEntity<List<AvailableDonationDto>> getAvailableDonations() {
        log.trace("getAvailableDonations");
        List<AvailableDonationDto> availableDonations = payoutRequestService.getAvailableDonation();
        return ResponseEntity.ok(availableDonations);
    }

    /**
     * Retrieves a payout request by its unique identifier.
     *
     * @param requestId the unique identifier of the payout request
     * @return a ResponseEntity containing the PayoutRequestDto
     */
    @GetMapping("/{requestId}")
    public ResponseEntity<PayoutRequestDto> getPayoutRequestById(@PathVariable UUID requestId) {
        log.trace("getPayoutRequestById: {}", requestId);
        PayoutRequestDto payoutRequestDto = payoutRequestService.getPayoutRequestById(requestId);
        return ResponseEntity.ok(payoutRequestDto);
    }

    /**
     * Retrieves all payout requests associated with a specific NGO.
     *
     * @param ngoId the unique identifier of the NGO
     * @return a ResponseEntity containing a list of PayoutRequestDto
     */
    @GetMapping("/by-ngo/{ngoId}")
    public ResponseEntity<List<PayoutRequestDto>> getPayoutRequestsByNgoId(@PathVariable UUID ngoId) {
        log.trace("getPayoutRequestsByNgoId: {}", ngoId);
        List<PayoutRequestDto> payoutRequests = payoutRequestService.getPayoutRequestByNgoId(ngoId);
        return ResponseEntity.ok(payoutRequests);
    }

    /**
     * Retrieves all payout requests associated with the NGO of the current user.
     *
     * @return a ResponseEntity containing a list of PayoutRequestDto
     */
    @GetMapping("/my-ngo")
    public ResponseEntity<List<PayoutRequestDto>> getPayoutRequestsByMyNgo() {
        log.trace("getPayoutRequestsByMyNgo");
        List<PayoutRequestDto> payoutRequests = payoutRequestService.getPayoutRequestByMyNgo();
        return ResponseEntity.ok(payoutRequests);
    }

    /**
     * Retrieves all pending payout requests.
     *
     * @return a ResponseEntity containing a list of pending PayoutRequestDto
     */
    @GetMapping("/pending")
    public ResponseEntity<List<PayoutRequestDto>> getAllPendingPayoutRequests() {
        log.trace("getAllPendingPayoutRequests");
        List<PayoutRequestDto> pendingPayoutRequests = payoutRequestService.getAllPayoutRequestsPending();
        return ResponseEntity.ok(pendingPayoutRequests);
    }

    /**
     * Approves a payout request by its unique identifier.
     *
     * @param payoutRequestId the unique identifier
     *                        of the payout request to be approved
     * @param proofFileDto the proof file associated with the approval
     * @return a ResponseEntity with HTTP status 200 (OK)
     * if the approval is successful
     */
    @PutMapping("/{payoutRequestId}/approve")
    public ResponseEntity<Void> approvePayoutRequestById(@PathVariable UUID payoutRequestId,
                                                         @RequestBody @Valid ProofFileDto proofFileDto) {
        log.trace("approvePayoutRequestById: {}", payoutRequestId);
        payoutRequestService.approvePayoutRequestById(payoutRequestId, proofFileDto);
        return ResponseEntity.ok().build();
    }
}
