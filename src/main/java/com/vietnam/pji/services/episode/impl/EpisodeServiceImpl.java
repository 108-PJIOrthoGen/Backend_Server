package com.vietnam.pji.services.episode.impl;

import com.vietnam.pji.dto.request.EpisodeRequestDTO;
import com.vietnam.pji.dto.response.PaginationResultDTO;
import com.vietnam.pji.exception.ResourceNotFoundException;
import com.vietnam.pji.model.medical.Patient;
import com.vietnam.pji.model.medical.PjiEpisode;
import com.vietnam.pji.repository.EpisodeRepository;
import com.vietnam.pji.repository.PatientRepository;
import com.vietnam.pji.services.episode.EpisodeService;
import com.vietnam.pji.utils.mapper.EpisodeMapper;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EpisodeServiceImpl implements EpisodeService {

    private final EpisodeRepository episodeRepository;
    private final PatientRepository patientRepository;
    private final EpisodeMapper episodeMapper;

    @Override
    @Transactional
    public PjiEpisode create(EpisodeRequestDTO data) {
        Patient patient = patientRepository.findById(data.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));

        PjiEpisode episode = episodeMapper.toEntity(data);
        episode.setPatient(patient);

        PjiEpisode saved = episodeRepository.save(episode);
        Hibernate.initialize(saved.getPatient());
        return saved;
    }

    @Override
    @Transactional
    public PjiEpisode update(Long id, EpisodeRequestDTO data) {
        PjiEpisode episode = episodeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Episode not found"));

        if (data.getPatientId() != null && !data.getPatientId().equals(episode.getPatient().getId())) {
            Patient patient = patientRepository.findById(data.getPatientId())
                    .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));
            episode.setPatient(patient);
        }

        episodeMapper.update(data, episode);
        PjiEpisode saved = episodeRepository.save(episode);
        Hibernate.initialize(saved.getPatient());
        return saved;
    }

    @Override
    @Transactional(readOnly = true)
    public PjiEpisode getById(Long id) {
        PjiEpisode episode = episodeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Episode not found"));
        Hibernate.initialize(episode.getPatient());
        return episode;
    }

    @Override
    public void delete(Long id) {
        if (!episodeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Episode not found");
        }
        episodeRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public PaginationResultDTO getAll(Specification<PjiEpisode> spec, Pageable pageable) {
        Page<PjiEpisode> page = episodeRepository.findAll(spec, pageable);
        return buildPaginationResult(page);
    }

    @Override
    @Transactional(readOnly = true)
    public PaginationResultDTO getByPatient(Long patientId, Pageable pageable) {
        if (!patientRepository.existsById(patientId)) {
            throw new ResourceNotFoundException("Patient not found");
        }
        Page<PjiEpisode> page = episodeRepository.findByPatientId(patientId, pageable);
        return buildPaginationResult(page);
    }

    private PaginationResultDTO buildPaginationResult(Page<PjiEpisode> page) {
        page.getContent().forEach(e -> Hibernate.initialize(e.getPatient()));
        PaginationResultDTO.Meta meta = new PaginationResultDTO.Meta();
        meta.setPage(page.getNumber() + 1);
        meta.setPageSize(page.getSize());
        meta.setPages(page.getTotalPages());
        meta.setTotal(page.getTotalElements());

        PaginationResultDTO result = new PaginationResultDTO();
        result.setMeta(meta);
        result.setResult(page.getContent());
        return result;
    }
}
