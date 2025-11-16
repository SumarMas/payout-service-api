package com.platform.payout_service.repositories;

import com.platform.payout_service.entities.PayoutRequestEntity;
import com.platform.payout_service.enums.PayoutStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
/**
 * Repository interface for managing PayoutRequestEntity entities.
 */
@Repository
public interface PayoutRequestRepository extends JpaRepository<PayoutRequestEntity, UUID> {
    /**
     * Finds all payout requests associated with a specific organization ID.
     *
     * @param organizationId the unique identifier of the organization
     * @return a list of PayoutRequestEntity associated with the organization
     */
    List<PayoutRequestEntity> findAllByOrganizationId(UUID organizationId);
    /**
     * Finds a payout request by its unique identifier and enabled status true.
     *
     * @param payoutRequestId the unique identifier of the payout request
     * @return an Optional containing the
     * PayoutRequestEntity if found and enabled, otherwise empty
     */
    @Query("SELECT pre FROM PayoutRequestEntity pre WHERE pre.payoutRequestId = :payoutRequestId AND pre.enabled = true")
    Optional<PayoutRequestEntity> findByPayoutRequestIdAndEnabledTrue(UUID payoutRequestId);

    /**
     * Finds all payout requests with a specific status.
     *
     * @param status the status of the payout requests to find
     * @return a list of PayoutRequestEntity with the specified status
     */
    @Query("SELECT pre FROM PayoutRequestEntity pre WHERE pre.status = :status")
    List<PayoutRequestEntity> findAllByStatus(PayoutStatus status);
}
