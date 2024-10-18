package ru.vitasoft.testcase.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.vitasoft.testcase.model.enums.roles.RoleType;
import java.util.Set;

@Setter
@Getter
@Builder
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private String username;

    private String email;

    private Set<RoleType> roles;

}
