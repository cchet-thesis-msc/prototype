package com.gepardec.esb.prototype.integration.services.db.rest.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.datatype.jsr310.ser.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Locale;

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

    @ApiModelProperty(value = "The customer id", readOnly = true)
    private Long id;

    @ApiModelProperty(value = "The customer version", readOnly = true)
    private Long version;

    @ApiModelProperty(value = "The customer first name", readOnly = true)
    private String firstName;

    @ApiModelProperty(value = "The customer last name", readOnly = true)
    private String lastName;

    @ApiModelProperty(value = "The created date", dataType = "string", readOnly = true)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private Date createdAt;

    @ApiModelProperty(value = "The last update date", dataType = "string", readOnly = true)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private Date modifiedAt;

    @ApiModelProperty(value = "The customer email", readOnly = true)
    private String email;
}
