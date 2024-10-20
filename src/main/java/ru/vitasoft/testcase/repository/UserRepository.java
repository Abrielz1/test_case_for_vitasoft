package ru.vitasoft.testcase.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.vitasoft.testcase.model.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {


    @Query(value = "SELECT *" +
                  " FROM users" +
                  " WHERE users.username LIKE :username", nativeQuery = true)
    Optional<User> getAuthorFromDbByUsername(String username);

    boolean existsByUsernameAndAndEmail(String username, String email);
}
