package com.platform.payout_service.dtos.payout;

import com.platform.payout_service.dtos.campaign.CampaignDto;
import com.platform.payout_service.dtos.donation.DonationDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;
/**
 * Data Transfer Object representing available donations for payment.
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class AvailableDonationDto extends CampaignDto {
    /** List of donations. */
    private List<DonationDto> donations;
}
