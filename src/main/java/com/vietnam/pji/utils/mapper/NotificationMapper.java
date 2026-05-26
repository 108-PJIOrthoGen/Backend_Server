package com.vietnam.pji.utils.mapper;

import com.vietnam.pji.dto.response.NotificationResponseDTO;
import com.vietnam.pji.model.notification.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Date;

@Mapper(config = DefaultConfigMapper.class)
public interface NotificationMapper {

    @Mapping(target = "createdAt", source = "createdAt", qualifiedByName = "dateToInstant")
    NotificationResponseDTO toResponse(Notification entity);

    @org.mapstruct.Named("dateToInstant")
    default java.time.Instant dateToInstant(Date date) {
        return date == null ? null : date.toInstant();
    }
}
