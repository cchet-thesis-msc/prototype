package com.gepardec.esb.prototype.integration.services.db.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 06/10/18
 */
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown=true)
@ApiModel(value = "Order", description = "The model representing the customer")
public class OrderDto {

    private String orderNr;
    private LocalDateTime createdAt;
    private LocalDateTime deliveredAt;
    private Long customerId;
}
