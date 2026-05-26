package com.vietnam.pji.services;

import com.vietnam.pji.dto.request.UpdateOwnProfileRequestDTO;
import com.vietnam.pji.dto.request.UserRequestDTO;
import com.vietnam.pji.dto.response.PaginationResultDTO;
import com.vietnam.pji.dto.response.UserDetailResponse;
import com.vietnam.pji.model.auth.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface UserService {

    UserDetailResponse create(UserRequestDTO data);

    UserDetailResponse update(UserRequestDTO data);

    UserDetailResponse getInfo(Long id);

    PaginationResultDTO getAll(Specification<User> spec, Pageable pageable);

    void delete(Long id);

    // Auth helpers
    User handleGetUserByUsername(String username);

    User fetchWithTokenAndEmail(String token, String email);

    void saveRefreshToken(String token, String email);

    void updateLastLogin(String email);

    /**
     * Apply a self-service profile update for the user identified by email.
     * Only the fields named on {@link UpdateOwnProfileRequestDTO} are touched
     * — role, status, and email are not editable here. When newPassword is
     * non-blank, currentPassword must match the stored hash.
     */
    User updateOwnProfile(String email, UpdateOwnProfileRequestDTO data);
}
