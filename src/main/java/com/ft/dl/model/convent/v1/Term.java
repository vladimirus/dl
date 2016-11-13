
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
public class Term {
    @JsonProperty
    private String name;
    @JsonProperty
    private String id;
    @JsonProperty
    private List<Object> attributes = new ArrayList<>();
    @JsonProperty
    private String taxonomy;

}
