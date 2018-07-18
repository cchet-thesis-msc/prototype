package com.gepardec.esb.prototype.integration.services.db.rest.model;

import com.fasterxml.jackson.annotation.JsonFormat;
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
import java.util.Date;
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

    @ApiModelProperty(value = "The order id", readOnly = true)
    private Long id;

    @ApiModelProperty(value = "The order version", readOnly = true)
    private Long version;

    @ApiModelProperty(value = "Customer id", readOnly = true)
    private Long customerId;

    @ApiModelProperty(value = "The creation date of the order", dataType = "string", readOnly = true)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private Date createdAt;

    @ApiModelProperty(value = "The modification date of the order", dataType = "string", readOnly = true)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private Date deliveredAt;

    @ApiModelProperty(value = "Item prices", readOnly = true)
    private Map<String, Double> itemPrices;

    @ApiModelProperty(value = "The list of ordered items", readOnly = true)
    private List<ItemDto> items;
}
