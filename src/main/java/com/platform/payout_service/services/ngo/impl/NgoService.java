package com.platform.payout_service.services.ngo.impl;

import com.platform.payout_service.context.IContextService;
import com.platform.payout_service.controllers.manageExceptions.CustomException;
import com.platform.payout_service.dtos.Ngo.NgoDto;
import com.platform.payout_service.restClients.ngo.INgoRestClient;
import com.platform.payout_service.services.ngo.INgoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Service implementation for managing NGO information.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class NgoService implements INgoService {
    /** REST client for interacting with the NGO service. */
    private final INgoRestClient ngoRestClient;
    /** Service for accessing context information such as the current user ID. */
    private final IContextService contextService;
    /**
     * Retrieves the NGO information associated with the current user.
     *
     * @return an NgoDto if found
     */
    @Override
    public NgoDto getMyNgo() {
        log.trace("getMyNgo");
        try {
            UUID userId = getUserId();
            NgoDto ngoDto = ngoRestClient.getMyNgo().getBody();
            if (ngoDto == null) {
                log.warn("No NGO found for user ID: {}", userId);
                throw  new CustomException("NGO Not Found", HttpStatus.NOT_FOUND);
            }
            return ngoDto;
        } catch (CustomException e) {
            log.error("Error retrieving NGO for user ID: {}", getUserId(), e);
            throw e;
        }
    }

    private UUID getUserId() {
        return contextService.getUserId();
    }
}
