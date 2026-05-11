package com.vietnam.pji.utils.mapper;

import com.vietnam.pji.dto.request.ImageResultRequestDTO;
import com.vietnam.pji.dto.response.ImageResultResponseDTO;
import com.vietnam.pji.model.medical.ImageResult;
import org.mapstruct.*;

@Mapper(config = DefaultConfigMapper.class)
public interface ImageResultMapper extends EntityMapper<ImageResultRequestDTO, ImageResult> {

    @Override
    @BeanMapping(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    @Mapping(target = "episode", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    ImageResult toEntity(ImageResultRequestDTO dto);

    @Override
    @Named("update")
    @BeanMapping(
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
            nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    @Mapping(target = "episode", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void update(ImageResultRequestDTO dto, @MappingTarget ImageResult entity);

    @Mapping(target = "episodeId", source = "episode.id")
    @Mapping(target = "url", ignore = true) // populated by the service from MinioChannel
    ImageResultResponseDTO toResponse(ImageResult entity);
}
