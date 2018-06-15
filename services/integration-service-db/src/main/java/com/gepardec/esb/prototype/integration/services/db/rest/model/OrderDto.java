package com.gepardec.esb.prototype.integration.services.db.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 06/10/18
 */
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown=true)
@ApiModel(value = "Order", description = "The model representing the customer order")
public class OrderDto {

    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private Long id;

    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private Long version;

    @ApiModelProperty(value = "Customer id", readOnly = true)
    private Long customerId;

    @ApiModelProperty(value = "The creation date of the order", readOnly = true)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createdAt;

    @ApiModelProperty(value = "The modification date of the order", readOnly = true)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime deliveredAt;

    @ApiModelProperty(value = "Item prices", readOnly = true)
    private Map<String, Double> itemPrices;

    @ApiModelProperty(value = "The list of ordered items", readOnly = true)
    private List<ItemDto> items;
}
