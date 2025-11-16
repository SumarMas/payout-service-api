package com.platform.payout_service.services.ngo;

import com.platform.payout_service.dtos.Ngo.NgoDto;
/**
 * Service interface for managing NGO-related operations.
 */
public interface INgoService {
    /**
     * Retrieves the NGO information associated with the current user.
     *
     * @return an NgoDto if found
     */
    NgoDto getMyNgo();
}
