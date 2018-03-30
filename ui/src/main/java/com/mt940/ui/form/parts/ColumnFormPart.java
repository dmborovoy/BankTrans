package com.mt940.ui.form.parts;


public class ColumnFormPart {
    String data;
    String name;
    Boolean searchable;
    Boolean orderable;
    SearchFromPart search;

    public ColumnFormPart(String data, String name, Boolean searchable, Boolean orderable, SearchFromPart search) {
        this.data = data;
        this.name = name;
        this.searchable = searchable;
        this.orderable = orderable;
        this.search = search;
    }

    public ColumnFormPart() {
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getSearchable() {
        return searchable;
    }

    public void setSearchable(Boolean searchable) {
        this.searchable = searchable;
    }

    public Boolean getOrderable() {
        return orderable;
    }

    public void setOrderable(Boolean orderable) {
        this.orderable = orderable;
    }

    public SearchFromPart getSearch() {
        return search;
    }

    public void setSearch(SearchFromPart search) {
        this.search = search;
    }

    @Override
    public String toString() {
        return "ColumnFormPart{" +
                "data='" + data + '\'' +
                ", name='" + name + '\'' +
                ", searchable=" + searchable +
                ", orderable=" + orderable +
                ", search=" + search +
                '}';
    }
}
