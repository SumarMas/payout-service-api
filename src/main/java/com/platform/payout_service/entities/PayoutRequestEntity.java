package com.platform.payout_service.entities;

import com.platform.payout_service.enums.PayoutStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
/**
 * Entity representing a payout request.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@Table(name = "payout_requests")
public class PayoutRequestEntity extends AuditEntity {
    /** Unique identifier for the payout request. */
    @Id
    @Column(name = "payout_request_id", columnDefinition = "BINARY(16)")
    private UUID payoutRequestId;

    /**  ID of the organization requesting the payout. */
    @Column(name = "organization_id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID organizationId;

    /** Total amount requested for payout. */
    @Column(name = "total_amount", nullable = false)
    private BigDecimal totalAmount;

    /** Status of the payout request. */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PayoutStatus status;

    /** ID of the proof file associated with the payout request. */
    @Column(name = "proof_file_id", columnDefinition = "BINARY(16)")
    private UUID proofFileId;

    /** Date and time when the payout request was made. */
    @Column(name = "request_datetime", nullable = false)
    private LocalDateTime requestDatetime;

    /** Date and time when the payout request was approved. */
    @Column(name = "approval_datetime")
    private LocalDateTime approvalDatetime;

    /** List of donations associated with the payout request. */
    @OneToMany(mappedBy = "payoutRequest", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PayoutRequestDonationEntity> donations;
}
