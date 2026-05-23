package com.vietnam.pji.services;

import com.vietnam.pji.dto.request.PriorAcceptedDiagnosisDTO;

import java.util.List;

public interface PriorAcceptedDiagnosisAssemblerService {

    /**
     * Build the list of prior physician-validated diagnoses for a patient,
     * excluding the current episode, so the RAG service can ground its new
     * recommendation in what the doctor has already accepted on previous
     * encounters.
     *
     * <p>Only reviews with {@code ACCEPTED} or {@code MODIFIED} status are
     * included. Each entry collapses the doctor's modifications back into the
     * item content when the review was {@code MODIFIED}.</p>
     *
     * @param currentEpisodeId the episode that is generating a new run — excluded
     * @return latest prior accepted diagnoses, most recent first; never {@code null}
     */
    List<PriorAcceptedDiagnosisDTO> assemble(Long currentEpisodeId);
}
