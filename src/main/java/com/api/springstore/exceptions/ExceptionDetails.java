package com.api.springstore.exceptions;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@SuperBuilder
public class ExceptionDetails {
    protected int status;
    protected String title;
    protected String details;
    protected LocalDateTime timestamp;
}
