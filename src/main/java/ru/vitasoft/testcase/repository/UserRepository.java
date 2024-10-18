package ru.vitasoft.testcase.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.vitasoft.testcase.model.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {


}
