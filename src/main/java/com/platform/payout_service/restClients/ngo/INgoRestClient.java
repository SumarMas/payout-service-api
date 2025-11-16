package com.platform.payout_service.restClients.ngo;

import com.platform.payout_service.dtos.Ngo.NgoDto;
import org.springframework.http.ResponseEntity;

/**
 * REST client interface for interacting with the NGO service.
 */
public interface INgoRestClient {
    /**
     * Retrieves the NGO information associated with the current user.
     *
     *
     * @return a ResponseEntity containing the NgoDto if found,
     * or an appropriate error response if no NGO is associated with the user
     */
    ResponseEntity<NgoDto> getMyNgo();
}
