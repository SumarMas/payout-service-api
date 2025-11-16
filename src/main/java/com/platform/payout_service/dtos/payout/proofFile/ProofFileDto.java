package com.platform.payout_service.dtos.payout.proofFile;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
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
    @NotBlank(message = "fileId cannot be blank")
    private UUID fileId;
}
