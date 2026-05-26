package com.vietnam.pji.repository;

import com.vietnam.pji.model.auth.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long>, JpaSpecificationExecutor<Role> {
    boolean existsByName(String name);

    Role findByName(String name);

    /**
     * Eager-loads the role and its permissions in one query so callers outside
     * a managed Hibernate session (e.g. startup runners) can safely mutate the
     * collection without LazyInitializationException.
     */
    @Query("SELECT r FROM Role r LEFT JOIN FETCH r.permissions WHERE r.name = :name")
    Optional<Role> findByNameWithPermissions(@Param("name") String name);
}
