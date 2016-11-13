package com.ft.dl.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ft.apiingester.configuration.ContentApiConfiguration;
import com.xeiam.dropwizard.sundial.SundialConfiguration;
import io.dropwizard.Configuration;
import lombok.Getter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Getter
public class DlConfiguration extends Configuration {
    @Valid
    @NotNull
    @JsonProperty("sundial")
    private SundialConfiguration sundialConfiguration = new SundialConfiguration();
    @Getter
    @NotNull
    @JsonProperty
    private ContentApiConfiguration contentApiConfiguration;
}
