package com.gepardec.esb.prototype.services.app.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;

/**
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 06/08/18
 */
@ApiModel(value = "ReportModel", description = "The model holding the report response")
@JsonIgnoreProperties(ignoreUnknown = true, allowGetters = true, allowSetters = true)
public class ReportModelDto {

    @JsonProperty("name")
    private String name;

    public ReportModelDto() {
    }

    public ReportModelDto(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
