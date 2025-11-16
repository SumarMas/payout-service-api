package com.platform.payout_service.services.payoutRequest;

import com.platform.payout_service.dtos.payout.AvailableDonationDto;

import java.util.List;
/**
 * Service interface for retrieving available donations for payout.
 */
public interface IAvailableDonationService {
    /**
     * Retrieves the available donations for payout.
     * @return a list of AvailableDonationDto representing the available donations
     */
    List<AvailableDonationDto> getAvailableDonation();
}
