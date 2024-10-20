package ru.vitasoft.testcase.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisKeyExpiredEvent;
import org.springframework.stereotype.Component;
import ru.vitasoft.testcase.exception.exceptions.ObjectNotFoundException;
import ru.vitasoft.testcase.security.entity.RefreshToken;

@Slf4j
@Component
public class RedisExpirationEvent {

    @EventListener
    public void handleRedisKeyExpiredEvent(RedisKeyExpiredEvent<RefreshToken> event) {

        RefreshToken expiredRefreshToken = (RefreshToken) event.getValue();

        if (expiredRefreshToken == null) {
            throw new ObjectNotFoundException("Refresh token is null in handleRedisKeyExpiredEvent function");
        }

        log.info("Refresh token with key: {}" +
                " has expired and Refresh Token is: {}",
                expiredRefreshToken.getId(), expiredRefreshToken.getToken());
    }
}
