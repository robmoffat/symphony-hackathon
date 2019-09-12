package com.db.symphonyp.tabs.common;

import java.util.List;

public class ObjectWrapper {

    private String version;
    private String type;
    private List<Table> id;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Table> getId() {
        return id;
    }

    public void setId(List<Table> id) {
        this.id = id;
    }
}
