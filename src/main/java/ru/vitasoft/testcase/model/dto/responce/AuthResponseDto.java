package ru.vitasoft.testcase.model.dto.responce;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponseDto {

    private Long id;

    private String username;

    private String token;

    private String refreshToken;

    private List<String> roles = new ArrayList<>();
}
