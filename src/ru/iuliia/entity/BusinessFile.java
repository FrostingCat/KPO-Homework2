package ru.iuliia.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BusinessFile {
    public final String name;
    public final List<String> kids = new ArrayList<>();
    public final List<String> text;
    public boolean colour;

    public BusinessFile(String x, List<String> z) {
        this.name = x;
        this.text = z;
        this.colour = false;
    }
    public void addKid(String y) {
        kids.add(y);
    }
    public BusinessFile findFile(List<BusinessFile> list, String file) {
        for (BusinessFile File : list) {
            if (Objects.equals(File.name, file)) {
                return File;
            }
        }
        return null;
    }
}
