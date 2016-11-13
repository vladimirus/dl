
package com.ft.dl.model.convent.v1;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(NON_NULL)
public class Metadata {
    @JsonProperty("primarySection")
    private TermWrapper primarySection;
    @JsonProperty("tags")
    private List<TermWrapper> tags = new ArrayList<>();
    @JsonProperty("authors")
    private List<TermWrapper> authors = new ArrayList<>();
    @JsonProperty("brand")
    private List<TermWrapper> brand = new ArrayList<>();
    @JsonProperty("genre")
    private List<TermWrapper> genre = new ArrayList<>();
    @JsonProperty("icb")
    private List<TermWrapper> icb = new ArrayList<>();
    @JsonProperty("iptc")
    private List<TermWrapper> iptc = new ArrayList<>();
    @JsonProperty("mediaType")
    private List<TermWrapper> mediaType = new ArrayList<>();
    @JsonProperty("organisations")
    private List<TermWrapper> organisations = new ArrayList<>();
    @JsonProperty("people")
    private List<TermWrapper> people = new ArrayList<>();
    @JsonProperty("regions")
    private List<TermWrapper> regions = new ArrayList<>();
    @JsonProperty("sections")
    private List<TermWrapper> sections = new ArrayList<>();
    @JsonProperty("specialReports")
    private List<Object> specialReports = new ArrayList<Object>();
    @JsonProperty("subjects")
    private List<TermWrapper> subjects = new ArrayList<>();
    @JsonProperty("topics")
    private List<TermWrapper> topics = new ArrayList<>();
}
