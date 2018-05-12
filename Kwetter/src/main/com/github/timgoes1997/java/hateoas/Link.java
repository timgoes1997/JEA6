package com.github.timgoes1997.java.hateoas;

import javax.json.Json;
import javax.json.JsonObject;
import java.io.Serializable;

public class Link implements Serializable {
    private String link;
    private String rel;
    private String requestType;

    public Link(){

    }

    public Link(String link, String rel, String requestType){
        this.link = link;
        this.rel = rel;
        this.requestType = requestType;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getRel() {
        return rel;
    }

    public void setRel(String rel) {
        this.rel = rel;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public JsonObject toJson(){
        return Json.createObjectBuilder()
                .add("link", this.link)
                .add("rel", this.rel)
                .add("requestType", this.requestType).build();
    }
}
