package com.platform.payout_service.services.payoutRequest.impl;

import com.platform.payout_service.context.IContextService;
import com.platform.payout_service.controllers.manageExceptions.CustomException;
import com.platform.payout_service.dtos.Ngo.NgoDto;
import com.platform.payout_service.dtos.payout.PayoutRequestDto;
import com.platform.payout_service.entities.PayoutRequestEntity;
import com.platform.payout_service.enums.PayoutStatus;
import com.platform.payout_service.map.impl.PayoutRequestMapper;
import com.platform.payout_service.repositories.PayoutRequestRepository;
import com.platform.payout_service.services.ngo.INgoService;
import com.platform.payout_service.services.payoutRequest.IPayoutRequestGetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
/**
 * Service implementation for retrieving payout requests.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PayoutRequestGetService implements IPayoutRequestGetService {
    /** Repository for accessing payout request data. */
    private final PayoutRequestRepository payoutRequestRepository;
    /** Service for handling NGO-related operations. */
    private final INgoService ngoService;
    /** Mapper for converting between entities and DTOs. */
    private final PayoutRequestMapper mapper;
    /** Service for accessing context information. */
    private final IContextService contextService;
    /**
     * Retrieves a payout request by its unique identifier.
     *
     * @param id the unique identifier of the payout request
     * @return the PayoutRequestDto if found
     */
    @Override
    public PayoutRequestDto getPayoutRequestById(UUID id) {
        log.trace("getPayoutRequestById({})", id);
        return mapEntityToDto(getPayoutRequestEntityById(id));
    }

    /**
     * Retrieves all payout requests associated with a specific NGO.
     *
     * @param id the unique identifier of the NGO
     * @return a list of PayoutRequestDto associated with the NGO
     */
    @Override
    public List<PayoutRequestDto> getPayoutRequestByNgoId(UUID id) {
        log.trace("getPayoutRequestByNgoId({})", id);
        List<PayoutRequestDto> payoutRequestDtos = getPayoutRequestEntitiesByNgoId(id).stream()
                .map(this::mapEntityToDto)
                .toList();
        return payoutRequestDtos;
    }

    /**
     * Retrieves all payout requests associated with the NGO of the current user.
     *
     * @return a list of PayoutRequestDto associated with the user's NGO
     */
    @Override
    public List<PayoutRequestDto> getPayoutRequestByMyNgo() {
        log.trace("getPayoutRequestByMyNgo()");
        NgoDto myNgo = getMyNgo();
        if (myNgo != null) {
            return getPayoutRequestByNgoId(UUID.fromString(myNgo.getId()));
        }
        log.warn("Ngo is null for the current user");
        return List.of();
    }

    /**
     * Retrieves all payout requests that are currently pending.
     *
     * @return a list of PayoutRequestDto that are pending
     */
    @Override
    public List<PayoutRequestDto> getAllPayoutRequestsPending() {
        log.trace("getAllPayoutRequestsPending()");
        isAdmin();
        List<PayoutRequestDto> payoutRequestDtos = getPayoutRequestEntitiesByStatus(PayoutStatus.PENDING).stream()
                .map(this::mapEntityToDto)
                .toList();
        return payoutRequestDtos;
    }

    private PayoutRequestEntity getPayoutRequestEntityById(UUID id) {
        try {
            return payoutRequestRepository.findById(id)
                    .orElseThrow(() -> new CustomException("Payout Request Not Found", HttpStatus.NOT_FOUND));
        } catch (DataAccessException e) {
            log.error("Error retrieving PayoutRequestEntity with id {}: {}", id, e.getMessage(), e);
            throw new CustomException("Error retrieving payout request", HttpStatus.INTERNAL_SERVER_ERROR, e);
        }
    }

    private List<PayoutRequestEntity> getPayoutRequestEntitiesByNgoId(UUID ngoId) {
        try {
            return payoutRequestRepository.findAllByOrganizationId(ngoId);
        } catch (DataAccessException e) {
            log.error("Error retrieving PayoutRequestEntities for NGO id {}: {}", ngoId, e.getMessage(), e);
            throw new CustomException("Error retrieving payout requests for NGO", HttpStatus.INTERNAL_SERVER_ERROR, e);
        }
    }

    private List<PayoutRequestEntity> getPayoutRequestEntitiesByStatus(PayoutStatus status) {
        try {
            return payoutRequestRepository.findAllByStatus(status);
        } catch (DataAccessException e) {
            log.error("Error retrieving PayoutRequestEntities for Status {}: {}", status, e.getMessage(), e);
            throw new CustomException("Error retrieving payout requests", HttpStatus.INTERNAL_SERVER_ERROR, e);
        }
    }

    private void isAdmin() {
        if (!contextService.isAdmin()) {
            log.error("User is not admin");
            throw new CustomException("Unauthorized", HttpStatus.UNAUTHORIZED);
        }
    }

    private PayoutRequestDto mapEntityToDto(PayoutRequestEntity entity) {
        return mapper.mapToDto(entity);
    }

    private NgoDto getMyNgo() {
        return ngoService.getMyNgo();
    }
}
