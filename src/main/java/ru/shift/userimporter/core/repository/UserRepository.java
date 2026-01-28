package ru.shift.userimporter.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.shift.userimporter.core.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = """
        SELECT *
        FROM users s
        WHERE (:phone IS NULL OR s.phone = :phone)
          AND (:firstName IS NULL OR s.first_name = :firstName)
          AND (:lastName IS NULL OR s.last_name = :lastName)
          AND (:email IS NULL OR s.email = :email)
        LIMIT :limit OFFSET :offset
        """, nativeQuery = true)
    List<User> findUserWithFilters(@Param("phone") String phone,
                                   @Param("firstName") String firstName,
                                   @Param("lastName") String lastName,
                                   @Param("email") String email,
                                   @Param("limit") Integer limit,
                                   @Param("offset") Integer offset);

    Optional<User> findUserByPhone(String phone);
}