package ru.vitasoft.testcase.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.vitasoft.testcase.model.entity.Ticket;
import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    List<Ticket> getAllByAuthor_Id(Long userId, PageRequest pageRequest);

    @Query(value =
           "SELECT * "
         + "FROM tickets "
         + "WHERE tickets.author_id = :authorId " +
                   "AND  tickets.id = :ticketId" ,nativeQuery = true)
    Optional<Ticket> findByAuthorIdAndTicketId(Long authorId, Long ticketId);

    List<Ticket> findAllByAuthor_Id(Long author_id, PageRequest pageRequest);

    @Query(value =
            "SELECT * "
                    + "FROM tickets "
                    + "WHERE tickets.author_id = :authorId " ,nativeQuery = true)
    Optional<Ticket> findByAuthorId(Long authorId);
}
