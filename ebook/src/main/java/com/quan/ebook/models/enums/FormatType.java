package com.quan.ebook.models.enums;

public enum FormatType {
    epub("epub"),
    txt("txt"),
    mobi("mobi"),
    azw("azw"),
    pdf("pdf");
    

    private String name;

    FormatType(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
