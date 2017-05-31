package com.jhzy.nursinghandover.beans;

/**
 * Created by sxmd on 2017/2/24.
 */

public class NurseItemBean {

    private String name;
    private boolean selected;

    public NurseItemBean(String name, boolean selected) {
        this.name = name;
        this.selected = selected;
    }

    @Override
    public String toString() {
        return "NurseItemBean{" +
                "name='" + name + '\'' +
                ", selected=" + selected +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
