package com.platform.payout_service.entities.embeddable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
/**
 * Embeddable class representing the
 * composite key for PayoutRequestDonation entity.
 */
@Data
@Embeddable
public class PayoutRequestDonationId implements Serializable {
    /**
     * Serial version UID for serialization.
     */
    private static final long serialVersionUID = 1L;
    /** ID of the payout request. */
    @Column(name = "payout_request_id", columnDefinition = "BINARY(16)")
    private UUID payoutRequestId;
    /** ID of the donation. */
    @Column(name = "donation_id", columnDefinition = "BINARY(16)")
    private UUID donationId;

    /**
     * Default constructor.
     */
    public PayoutRequestDonationId() {
        // Default constructor
    }

    /**
     * Parameterized constructor.
     *
     * @param payoutRequestIdParam ID of the payout request.
     * @param donationIdParam      ID of the donation.
     */
    public PayoutRequestDonationId(UUID payoutRequestIdParam, UUID donationIdParam) {
        this.payoutRequestId = payoutRequestIdParam;
        this.donationId = donationIdParam;
    }

    /**
     * Overrides equals method for comparison.
     *
     * @param o Object to compare.
     * @return true if equal, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PayoutRequestDonationId that)) {
            return false;
        }
        return payoutRequestId.equals(that.payoutRequestId) && donationId.equals(that.donationId);
    }

    /**
     * Overrides hashCode method.
     *
     * @return hash code of the object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(payoutRequestId, donationId);
    }
}
