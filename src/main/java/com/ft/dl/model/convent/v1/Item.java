
package com.ft.dl.model.convent.v1;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.Generated;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "aspectSet",
    "aspects",
    "modelVersion",
    "id",
    "apiUrl",
    "title",
    "body",
    "lifecycle",
    "location",
    "summary",
    "packaging",
    "master",
    "editorial",
    "metadata",
    "images",
    "package",
    "assets",
    "mediaAssets",
    "provenance"
})
public class Item {

    @JsonProperty("aspectSet")
    public String aspectSet;
    @JsonProperty("aspects")
    public List<String> aspects = new ArrayList<String>();
    @JsonProperty("modelVersion")
    public String modelVersion;
    @JsonProperty("id")
    public String id;
    @JsonProperty("apiUrl")
    public String apiUrl;
    @JsonProperty("title")
    public Title title;
    @JsonProperty("body")
    public Body body;
    @JsonProperty("lifecycle")
    public Lifecycle lifecycle;
    @JsonProperty("location")
    public Location location;
    @JsonProperty("summary")
    public Summary summary;
    @JsonProperty("packaging")
    public Packaging packaging;
    @JsonProperty("master")
    public Master master;
    @JsonProperty("editorial")
    public Editorial editorial;
    @JsonProperty("metadata")
    public Metadata metadata;
    @JsonProperty("images")
    public List<Image> images = new ArrayList<Image>();
    @JsonProperty("package")
    public List<Object> _package = new ArrayList<Object>();
    @JsonProperty("assets")
    public List<Object> assets = new ArrayList<Object>();
    @JsonProperty("mediaAssets")
    public List<Object> mediaAssets = new ArrayList<Object>();
    @JsonProperty("provenance")
    public Provenance provenance;

}
