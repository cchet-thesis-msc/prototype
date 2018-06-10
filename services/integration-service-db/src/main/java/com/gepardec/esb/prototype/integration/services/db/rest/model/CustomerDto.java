package com.gepardec.esb.prototype.integration.services.db.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 06/10/18
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(value = "Customer", description = "The model representing the customer")
public class CustomerDto {

    @ApiModelProperty(value = "The customer first name", readOnly = true)
    private String firstName;

    @ApiModelProperty(value = "The customer last name", readOnly = true)
    private String lastName;

    @ApiModelProperty(value = "The customer email", readOnly = true)
    private String email;
}
