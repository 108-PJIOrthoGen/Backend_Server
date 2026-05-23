package com.vietnam.pji.utils.mapper;

import com.vietnam.pji.dto.request.PriorAcceptedDiagnosisDTO;
import com.vietnam.pji.model.agentic.AiRecommendationItem;
import com.vietnam.pji.model.agentic.DoctorRecommendationReview;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.LocalDate;
import java.util.Date;

/**
 * Maps a {@link DoctorRecommendationReview} and its items to the prompt-ready
 * {@link PriorAcceptedDiagnosisDTO} carried to the RAG service.
 *
 * <p>{@code acceptedItems}, {@code finalItemJson} and {@code source} are left
 * for the service to populate because they depend on side queries
 * ({@link AiRecommendationItem}s by run) and business rules
 * (doctor-modification overrides original AI content).</p>
 */
@Mapper(config = DefaultConfigMapper.class)
public interface PriorAcceptedDiagnosisMapper {

    @Mapping(target = "episodeId", source = "episode.id")
    @Mapping(target = "runId", source = "run.id")
    @Mapping(target = "admissionDate", source = "episode.admissionDate", qualifiedByName = "localDateToString")
    @Mapping(target = "dischargeDate", source = "episode.dischargeDate", qualifiedByName = "localDateToString")
    @Mapping(target = "reviewStatus", source = "reviewStatus", qualifiedByName = "enumToString")
    @Mapping(target = "reviewedAt", source = "createdAt", qualifiedByName = "dateToInstantString")
    @Mapping(target = "reviewNote", source = "reviewNote")
    @Mapping(target = "acceptedItems", ignore = true)
    PriorAcceptedDiagnosisDTO toDto(DoctorRecommendationReview review);

    @Mapping(target = "category", source = "category", qualifiedByName = "enumToString")
    @Mapping(target = "title", source = "title")
    @Mapping(target = "isPrimary", source = "isPrimary")
    @Mapping(target = "priorityOrder", source = "priorityOrder")
    @Mapping(target = "finalItemJson", ignore = true)
    @Mapping(target = "source", ignore = true)
    PriorAcceptedDiagnosisDTO.AcceptedItem toAcceptedItem(AiRecommendationItem item);

    @Named("localDateToString")
    default String localDateToString(LocalDate d) {
        return d != null ? d.toString() : null;
    }

    @Named("enumToString")
    default String enumToString(Enum<?> e) {
        return e != null ? e.name() : null;
    }

    @Named("dateToInstantString")
    default String dateToInstantString(Date d) {
        return d != null ? d.toInstant().toString() : null;
    }
}
