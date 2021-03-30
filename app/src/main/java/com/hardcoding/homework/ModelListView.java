package com.hardcoding.homework;


public class ModelListView {

    private String id;
    private String title_task;
    private String desc_task;
    private String lavel;
    private String inactive;
    private String cat;



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCat() {
        return cat;
    }

    public void setCat(String cat) {
        this.cat = cat;
    }

    public String getTitle() {
        return title_task;
    }

    public void setTitle(String title_task) {
        this.title_task = title_task;
    }

    public String getDecsript() {
        return desc_task;
    }

    public void setDecsript(String desc_task) {
        this.desc_task = desc_task;
    }

    public String getLavel() {
        return lavel;
    }

    public void setLavel(String lavel) {
        this.lavel = lavel;
    }

    public String getInactive() {
        return inactive;
    }

    public void setInactive(String inactive) {
        this.inactive = inactive;
    }

}
