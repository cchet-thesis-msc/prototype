package com.gepardec.esb.prototype.integration.services.db.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 06/15/18
 */
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(value = "Item", description = "The model representing the order item")
public class ItemDto {

    @ApiModelProperty(value = "The item name, which is unique", readOnly = true)
    private String name;

    @ApiModelProperty(value = "The count of ordered items", readOnly = true)
    private Long count;

    @ApiModelProperty(value = "The full price of the item", readOnly = true)
    private Double price;
}
