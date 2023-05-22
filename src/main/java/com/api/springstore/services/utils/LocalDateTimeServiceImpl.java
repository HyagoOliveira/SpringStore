package com.api.springstore.services.utils;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
@Profile("!test")
public class LocalDateTimeServiceImpl implements LocalDateTimeService {
    @Override
    public LocalDateTime getCurrentDateTimeInUTCZone() {
        return LocalDateTime.now(ZoneId.of("UTC"));
    }
}
