package com.platform.payout_service.services.payoutRequest.impl;

import com.platform.payout_service.context.IContextService;
import com.platform.payout_service.controllers.manageExceptions.CustomException;
import com.platform.payout_service.dtos.Ngo.NgoDto;
import com.platform.payout_service.dtos.donation.DonationDto;
import com.platform.payout_service.dtos.payout.AvailableDonationDto;
import com.platform.payout_service.dtos.payout.PayoutRequestDto;
import com.platform.payout_service.entities.PayoutRequestDonationEntity;
import com.platform.payout_service.entities.PayoutRequestEntity;
import com.platform.payout_service.entities.embeddable.PayoutRequestDonationId;
import com.platform.payout_service.enums.PayoutStatus;
import com.platform.payout_service.map.impl.PayoutRequestMapper;
import com.platform.payout_service.repositories.PayoutRequestRepository;
import com.platform.payout_service.services.ngo.INgoService;
import com.platform.payout_service.services.payoutRequest.IAvailableDonationService;
import com.platform.payout_service.services.payoutRequest.IPayoutRequestCreateService;
import com.platform.payout_service.services.payoutRequest.IPayoutRequestPublishEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Service implementation for creating payout requests.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class PayoutRequestCreateService implements IPayoutRequestCreateService {
    /**
     * Ngo service for getting NGO details.
     */
    private final INgoService ngoService;
    /**
     * Service for retrieving available donations.
     */
    private final IAvailableDonationService availableDonationService;
    /**
     * Repository for managing payout requests.
     */
    private final PayoutRequestRepository repository;
    /**
     * Mapper for converting between entities and DTOs.
     */
    private final PayoutRequestMapper mapper;

    /** Service for accessing user context information. */
    private final IContextService contextService;

    /** Service for publishing payout request events. */
    private final IPayoutRequestPublishEventService payoutRequestPublishEventService;

    /**
     * Creates a new payout request with user context.
     *
     * @return the created PayoutRequestDto
     */
    @Override
    @Transactional
    public PayoutRequestDto createPayoutRequest() {
        UUID userId = getUserId();
        NgoDto ngoDto = getNgoDto();
        List<AvailableDonationDto> availableDonations = getAvailableDonations();
        PayoutRequestEntity payoutRequestEntity = buildPayoutRequestEntity(ngoDto, userId);
        List<PayoutRequestDonationEntity> payoutRequestDonationEntities = buildPayoutRequestDonationEntity(
                payoutRequestEntity, availableDonations, userId);
        payoutRequestEntity.setDonations(payoutRequestDonationEntities);
        payoutRequestEntity.setTotalAmount(calculateTotalAmount(payoutRequestDonationEntities));
        PayoutRequestEntity savedEntity = savePayoutRequestEntity(payoutRequestEntity);
        publishCreatedEvent(payoutRequestEntity);
        return mapEntityToDto(savedEntity);
    }

    private UUID getUserId() {
        return contextService.getUserId();
    }

    private NgoDto getNgoDto() {
        return ngoService.getMyNgo();
    }

    private List<AvailableDonationDto> getAvailableDonations() {
        return availableDonationService.getAvailableDonation();
    }

    private PayoutRequestDto mapEntityToDto(PayoutRequestEntity entity) {
        return mapper.mapToDto(entity);
    }

    private List<PayoutRequestDonationEntity> buildPayoutRequestDonationEntity(
            PayoutRequestEntity entity,
            List<AvailableDonationDto> availableDonations,
            UUID userId) {
        List<PayoutRequestDonationEntity> entities = new ArrayList<>();
        for (AvailableDonationDto availableDonation : availableDonations) {
            for (DonationDto donation : availableDonation.getDonations()) {
                PayoutRequestDonationEntity payoutRequestDonationEntity =
                        PayoutRequestDonationEntity.builder()
                                .payoutRequest(entity)
                                .campaignId(donation.getCampaignId())
                                .amount(donation.getAmount())
                                .id(new PayoutRequestDonationId(entity.getPayoutRequestId(), donation.getDonationId()))
                                .createdUser(userId)
                                .enabled(true)
                                .build();
                entities.add(payoutRequestDonationEntity);
            }
        }
        return entities;
    }

    private PayoutRequestEntity buildPayoutRequestEntity(NgoDto ngoDto, UUID userId) {
        return PayoutRequestEntity.builder()
                .payoutRequestId(UUID.randomUUID())
                .createdUser(userId)
                .requestDatetime(LocalDateTime.now())
                .enabled(true)
                .organizationId(UUID.fromString(ngoDto.getId()))
                .status(PayoutStatus.PENDING)
                .build();
    }

    private PayoutRequestEntity savePayoutRequestEntity(PayoutRequestEntity entity) {
        try {
            return repository.save(entity);
        } catch (DataAccessException ex) {
            log.error("Error saving PayoutRequestEntity: {}", ex.getMessage(), ex);
            throw new CustomException("Failed to create payout request", HttpStatus.INTERNAL_SERVER_ERROR, ex);
        }
    }

    private BigDecimal calculateTotalAmount(List<PayoutRequestDonationEntity> donations) {
        BigDecimal totalAmount;
        totalAmount = donations.stream()
                .map(PayoutRequestDonationEntity::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
        return totalAmount;
    }

    private void publishCreatedEvent(PayoutRequestEntity payoutRequestEntity) {
        payoutRequestPublishEventService.payoutRequestEventPublisher(
                payoutRequestEntity,
                null,
                PayoutStatus.PENDING
        );
    }
}
