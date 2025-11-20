package com.platform.payout_service.dtos.payout.proofFile;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;
/**
 * Data Transfer Object representing a proof file.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProofFileDto {
    /**
     * The unique identifier of the proof file.
     */
    @JsonProperty("file_id")
    @NotNull(message = "file_id cannot be null")
    private UUID fileId;
}
