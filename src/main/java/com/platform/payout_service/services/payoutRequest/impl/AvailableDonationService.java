package com.platform.payout_service.services.payoutRequest.impl;

import com.platform.payout_service.dtos.Ngo.NgoDto;
import com.platform.payout_service.dtos.campaign.CampaignDto;
import com.platform.payout_service.dtos.donation.DonationDto;
import com.platform.payout_service.dtos.donation.DonationsDto;
import com.platform.payout_service.dtos.payout.AvailableDonationDto;
import com.platform.payout_service.entities.PayoutRequestDonationEntity;
import com.platform.payout_service.enums.CampaignState;
import com.platform.payout_service.enums.DonationStatus;
import com.platform.payout_service.repositories.PayoutRequestDonationRepository;
import com.platform.payout_service.services.campaign.ICampaignService;
import com.platform.payout_service.services.donation.IDonationGetService;
import com.platform.payout_service.services.ngo.INgoService;
import com.platform.payout_service.services.payoutRequest.IAvailableDonationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
/**
 * Service implementation for retrieving available donations for payout.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AvailableDonationService implements IAvailableDonationService {
    /**
     * Repository for accessing payout request donations.
     */
    private final PayoutRequestDonationRepository payoutRequestDonationRepository;
    /**
     * Service for managing campaigns.
     */
    private final ICampaignService campaignService;
    /**
     * Service for managing NGOs.
     */
    private final INgoService ngoService;
    /**
     * Service for retrieving donation information.
     */
    private final IDonationGetService donationGetService;
    /**
     * Model mapper for converting between entities and DTOs.
     */
    private final ModelMapper modelMapper;
    /**
     * Retrieves the available donations for payout.
     *
     * @return a list of AvailableDonationDto representing the available donations
     */
    @Override
    public List<AvailableDonationDto> getAvailableDonation() {
        log.trace("getAvailableDonation");
        NgoDto myNgo = getMyNgo();
        Map<UUID, CampaignDto> campaigns = getCampaigns(UUID.fromString(myNgo.getId()));
        Set<UUID> campaignsId = campaigns.keySet();
        DonationsDto donationsDto = getDonations(campaignsId);
        List<PayoutRequestDonationEntity> payoutRequestDonations = getPayoutRequestDonations(UUID.fromString(myNgo.getId()));
        List<AvailableDonationDto> availableDonationDtos = new ArrayList<>();
        for (Map.Entry<UUID, List<DonationDto>> entry : donationsDto.getDonations().entrySet()) {
            UUID campaignId = entry.getKey();
            List<DonationDto> donationDtos = entry.getValue();
            donationDtos.removeIf(donationDto -> payoutRequestDonations.stream()
                    .anyMatch(prd -> prd.getId().getDonationId().equals(donationDto.getDonationId())));
            CampaignDto campaignDto = campaigns.get(campaignId);
            AvailableDonationDto data = modelMapper.map(campaignDto, AvailableDonationDto.class);
            data.setDonations(donationDtos);
            availableDonationDtos.add(data);
        }

        return availableDonationDtos;
    }

    private NgoDto getMyNgo() {
        return ngoService.getMyNgo();
    }

    private Map<UUID, CampaignDto> getCampaigns(UUID ngoId) {
        return campaignService.getCampaignsByStateAndNgoId(CampaignState.CLOSED, ngoId).stream()
                .collect(Collectors.toMap(dto -> UUID.fromString(dto.getId()), dto -> dto, (dto1, dto2) -> dto1));
    }

    private DonationsDto getDonations(Set<UUID> campaignsId) {
        return donationGetService.getDonationsByStatesAndNgosId(Set.of(DonationStatus.CONFIRMED), campaignsId);
    }

    private List<PayoutRequestDonationEntity> getPayoutRequestDonations(UUID ngoId) {
        return payoutRequestDonationRepository.findAllByPayoutRequestIsEnabledAndNgoId(ngoId);
    }
}
