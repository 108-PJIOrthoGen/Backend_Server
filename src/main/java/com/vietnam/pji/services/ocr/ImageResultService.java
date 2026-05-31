package com.vietnam.pji.services.ocr;

import com.vietnam.pji.dto.request.ImageResultRequestDTO;
import com.vietnam.pji.dto.response.ImageResultResponseDTO;
import com.vietnam.pji.dto.response.PaginationResultDTO;
import org.springframework.data.domain.Pageable;

public interface ImageResultService {
    ImageResultResponseDTO create(ImageResultRequestDTO data);

    ImageResultResponseDTO update(Long id, ImageResultRequestDTO data);

    ImageResultResponseDTO getById(Long id);

    void delete(Long id);

    PaginationResultDTO getByEpisode(Long episodeId, Pageable pageable);
}
