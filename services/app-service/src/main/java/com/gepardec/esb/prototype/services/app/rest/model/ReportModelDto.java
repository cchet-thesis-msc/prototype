package com.gepardec.esb.prototype.services.app.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 06/08/18
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(value = "ReportModel",
        description = "The model holding the report response")
public class ReportModelDto {

    @JsonProperty("name")
    @ApiModelProperty(value = "The report name", readOnly = true)
    private String name;

    @JsonProperty("orderCount")
    @ApiModelProperty(value = "The report order count", readOnly = true)
    private Long orderCount;

    @JsonProperty("price")
    @ApiModelProperty(value = "The report price", readOnly = true)
    private Double price;
}
