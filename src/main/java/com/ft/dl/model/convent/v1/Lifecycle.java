
package com.ft.dl.model.convent.v1;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "initialPublishDateTime",
    "lastPublishDateTime"
})
public class Lifecycle {

    @JsonProperty("initialPublishDateTime")
    public String initialPublishDateTime;
    @JsonProperty("lastPublishDateTime")
    public String lastPublishDateTime;

}
