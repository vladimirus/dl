
package com.ft.dl.model.convent.v1;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "key",
    "value"
})
public class Attribute {

    @JsonProperty("key")
    public String key;
    @JsonProperty("value")
    public String value;

}
