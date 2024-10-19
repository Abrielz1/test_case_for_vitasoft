package ru.vitasoft.testcase.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.vitasoft.testcase.model.entity.User;
import ru.vitasoft.testcase.model.enums.status.Status;
import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class TicketDto {

    private String message;

    private Status status;

    private LocalDateTime created;

    private User author;
}
