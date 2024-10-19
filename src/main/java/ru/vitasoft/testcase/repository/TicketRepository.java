package ru.vitasoft.testcase.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.vitasoft.testcase.model.entity.Ticket;
import ru.vitasoft.testcase.model.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    List<Ticket> getAllByAuthor_Id(Long userId, PageRequest pageRequest);

    @Query(value =
           "SELECT * "
         + "FROM tickets "
         + "WHERE tickets.author = :userId " ,nativeQuery = true)
    Optional<Ticket> findByAuthorId(Long userId);

    List<Ticket> findAllByUsername(@Param("author") User author, PageRequest pageRequest);
}
