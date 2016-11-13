
package com.ft.dl.model.convent.v1;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "url",
    "type",
    "alt",
    "height",
    "width"
})
public class Image {

    @JsonProperty("url")
    public String url;
    @JsonProperty("type")
    public String type;
    @JsonProperty("alt")
    public String alt;
    @JsonProperty("height")
    public Integer height;
    @JsonProperty("width")
    public Integer width;

}
