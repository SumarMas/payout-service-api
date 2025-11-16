package com.platform.payout_service.entities;

import com.platform.payout_service.entities.embeddable.PayoutRequestDonationId;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.UUID;
/**
 * Entity representing the association between PayoutRequest and Donation.
 */
@Entity
@Table(name = "payout_request_donations")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class PayoutRequestDonationEntity extends AuditEntity {
    /** Composite primary key for PayoutRequestDonationEntity. */
    @EmbeddedId
    private PayoutRequestDonationId id;
    /** ID of the associated campaign. */
    @Column(name = "campaign_id", columnDefinition = "BINARY(16)")
    private UUID campaignId;

    /** Amount donated. */
    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    /** Payout Request associated with this donation. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payout_request_id", insertable = false, updatable = false)
    @MapsId("payoutRequestId")
    private PayoutRequestEntity payoutRequest;
}
