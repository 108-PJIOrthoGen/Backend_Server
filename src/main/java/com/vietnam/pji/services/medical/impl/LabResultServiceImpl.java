package com.vietnam.pji.services.medical.impl;

import com.vietnam.pji.dto.request.LabResultRequestDTO;
import com.vietnam.pji.dto.response.PaginationResultDTO;
import com.vietnam.pji.exception.ResourceNotFoundException;
import com.vietnam.pji.model.medical.LabResult;
import com.vietnam.pji.model.medical.PjiEpisode;
import com.vietnam.pji.repository.EpisodeRepository;
import com.vietnam.pji.repository.LabResultRepository;
import com.vietnam.pji.services.feat.RedisService;
import com.vietnam.pji.services.medical.LabResultService;
import com.vietnam.pji.services.medical.PendingLabTaskService;
import com.vietnam.pji.utils.mapper.LabResultMapper;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LabResultServiceImpl implements LabResultService {

    private final LabResultRepository labResultRepository;
    private final EpisodeRepository episodeRepository;
    private final LabResultMapper labResultMapper;
    private final PendingLabTaskService pendingLabTaskService;
    private final RedisService redisService;

    @Override
    @Transactional
    public LabResult create(LabResultRequestDTO data) {
        PjiEpisode episode = episodeRepository.findById(data.getEpisodeId())
                .orElseThrow(() -> new ResourceNotFoundException("Episode not found"));
        LabResult labResult = labResultMapper.toEntity(data);
        labResult.setEpisode(episode);
        LabResult saved = labResultRepository.save(labResult);

        pendingLabTaskService.autoFulfillForEpisode(
                episode.getId(), saved.getId(),
                data.getHematologyTests(), data.getFluidAnalysis(), data.getBiochemicalData());

        redisService.evictSnapshotCache(data.getEpisodeId());
        Hibernate.initialize(saved.getEpisode());
        Hibernate.initialize(saved.getEpisode().getPatient());
        return saved;
    }

    @Override
    @Transactional
    public LabResult update(Long id, LabResultRequestDTO data) {
        LabResult labResult = labResultRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lab result not found"));
        labResultMapper.update(data, labResult);
        LabResult saved = labResultRepository.save(labResult);

        Long episodeId = labResult.getEpisode().getId();
        pendingLabTaskService.autoFulfillForEpisode(
                episodeId, saved.getId(),
                data.getHematologyTests(), data.getFluidAnalysis(), data.getBiochemicalData());

        redisService.evictSnapshotCache(episodeId);
        Hibernate.initialize(saved.getEpisode());
        Hibernate.initialize(saved.getEpisode().getPatient());
        return saved;
    }

    @Override
    @Transactional(readOnly = true)
    public LabResult getById(Long id) {
        LabResult labResult = labResultRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lab result not found"));
        Hibernate.initialize(labResult.getEpisode());
        Hibernate.initialize(labResult.getEpisode().getPatient());
        return labResult;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        LabResult labResult = labResultRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lab result not found"));
        Long episodeId = labResult.getEpisode().getId();
        labResultRepository.deleteById(id);
        redisService.evictSnapshotCache(episodeId);
    }

    @Override
    @Transactional(readOnly = true)
    public PaginationResultDTO getByEpisode(Long episodeId, Pageable pageable) {
        if (!episodeRepository.existsById(episodeId)) {
            throw new ResourceNotFoundException("Episode not found");
        }
        Page<LabResult> page = labResultRepository.findByEpisodeId(episodeId, pageable);
        page.getContent().forEach(r -> {
            Hibernate.initialize(r.getEpisode());
            Hibernate.initialize(r.getEpisode().getPatient());
        });
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
