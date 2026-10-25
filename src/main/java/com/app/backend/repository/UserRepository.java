package com.app.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.app.backend.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    boolean existsByUsername(String userName);
    boolean existsByEmail(String email);
    Optional<User> findByUsername(String userName);
    Optional<User> findByEmail(String email);

    @Query(value = "Select u FROM User u  ORDER BY u.id DESC LIMIT :size OFFSET :offset")
    List<User> findAddByPage(@Param("offset") long offset, @Param("size") long size);
}
