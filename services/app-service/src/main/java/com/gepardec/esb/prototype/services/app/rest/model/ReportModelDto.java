package com.gepardec.esb.prototype.services.app.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
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
@JsonIgnoreProperties(ignoreUnknown=true)
@ApiModel(value = "ReportModel", description = "The model holding the report response")
public class ReportModelDto {

    @JsonProperty("name")
    private String name;

    private Long orderCount;

    private Double price;
}
