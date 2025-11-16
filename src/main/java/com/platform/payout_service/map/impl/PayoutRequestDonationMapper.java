package com.platform.payout_service.map.impl;

import com.platform.payout_service.dtos.payout.donation.PayoutRequestDonationDto;
import com.platform.payout_service.entities.PayoutRequestDonationEntity;
import com.platform.payout_service.map.IMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Mapper implementation
 * for PayoutRequestDonationEntity to PayoutRequestDonationDto.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PayoutRequestDonationMapper implements IMapper<PayoutRequestDonationDto, PayoutRequestDonationEntity> {
    /**
     * Maps an entity to a DTO.
     *
     * @param entity the entity to map
     * @return the mapped DTO
     */
    @Override
    public PayoutRequestDonationDto mapToDto(PayoutRequestDonationEntity entity) {
        try {
            return PayoutRequestDonationDto.builder()
                    .amount(entity.getAmount())
                    .payoutRequestId(entity.getId().getPayoutRequestId())
                    .campaignId(entity.getCampaignId())
                    .donationId(entity.getId().getDonationId())
                    .build();
        } catch (NullPointerException ex) {
            log.error("NullPointerException in mapToDto: {}", ex.getMessage(), ex);
            return new PayoutRequestDonationDto();
        }
    }
}
