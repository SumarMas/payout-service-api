package com.platform.payout_service.repositories;

import com.platform.payout_service.entities.PayoutRequestDonationEntity;
import com.platform.payout_service.entities.embeddable.PayoutRequestDonationId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repository interface for managing PayoutRequestDonationEntity entities.
 */
@Repository
public interface PayoutRequestDonationRepository extends JpaRepository<PayoutRequestDonationEntity, PayoutRequestDonationId> {
    /**
     * Finds all PayoutRequestDonationEntity records
     * where the associated payout request is enabled
     * and Ngo ID.
     *
     * @param ngoId the unique identifier of the NGO
     * @return a list of PayoutRequestDonationEntity with enabled payout requests
     */
    @Query("SELECT D FROM PayoutRequestDonationEntity D "
            + "WHERE D.payoutRequest.enabled = true and D.payoutRequest.organizationId = :ngoId")
    List<PayoutRequestDonationEntity> findAllByPayoutRequestIsEnabledAndNgoId(UUID ngoId);
}
