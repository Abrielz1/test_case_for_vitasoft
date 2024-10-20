package ru.vitasoft.testcase.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.vitasoft.testcase.security.entity.RefreshToken;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long> {

    void deleteByUserId(Long userId);

    Optional<RefreshToken> findByToken(String refreshToken);
}
