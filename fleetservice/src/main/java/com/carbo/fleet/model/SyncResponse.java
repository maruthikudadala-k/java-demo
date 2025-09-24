package com.carbo.fleet.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Map;
import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SyncResponse {
    private List<Fleet> get;

    private Map<String, Long> updated;

    private Set<String> removed;

    public Map<String, Long> getUpdated() {
        return this.updated;
    }

    public void setUpdated(Map<String, Long> updated) {
        this.updated = updated;
    }

    public List<Fleet> getGet() {
        return this.get;
    }

    public void setGet(List<Fleet> get) {
        this.get = get;
    }

    public Set<String> getRemoved() {
        return this.removed;
    }

    public void setRemoved(Set<String> removed) {
        this.removed = removed;
    }
}
