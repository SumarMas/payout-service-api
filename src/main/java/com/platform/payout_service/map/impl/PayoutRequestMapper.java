package com.platform.payout_service.map.impl;

import com.platform.payout_service.dtos.payout.PayoutRequestDto;
import com.platform.payout_service.dtos.payout.donation.PayoutRequestDonationDto;
import com.platform.payout_service.entities.PayoutRequestDonationEntity;
import com.platform.payout_service.entities.PayoutRequestEntity;
import com.platform.payout_service.map.IMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Mapper implementation for PayoutRequestEntity to PayoutRequestDto.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PayoutRequestMapper implements IMapper<PayoutRequestDto, PayoutRequestEntity> {
    /** Mapper for PayoutRequestDonationEntity to PayoutRequestDonationDto. */
    private final IMapper<PayoutRequestDonationDto, PayoutRequestDonationEntity> payoutRequestDonationMapper;
    /**
     * Maps an entity to a DTO.
     *
     * @param entity the entity to map
     * @return the mapped DTO
     */
    @Override
    public PayoutRequestDto mapToDto(PayoutRequestEntity entity) {
        try {
            return PayoutRequestDto.builder()
                    .payoutRequestId(entity.getPayoutRequestId())
                    .ngoId(entity.getOrganizationId())
                    .amount(entity.getTotalAmount())
                    .status(entity.getStatus())
                    .requestDateTime(entity.getRequestDatetime())
                    .approvalDatetime(entity.getApprovalDatetime())
                    .donations(
                            entity.getDonations().stream()
                                    .map(this::mapToDtoPayoutRequestDonation)
                                    .toList()
                    )
                    .build();
        } catch (NullPointerException ex) {
            log.error("NullPointerException in mapToDto: {}", ex.getMessage(), ex);
            return new PayoutRequestDto();
        }
    }

    private PayoutRequestDonationDto mapToDtoPayoutRequestDonation(PayoutRequestDonationEntity entity) {
        return payoutRequestDonationMapper.mapToDto(entity);
    }
}
