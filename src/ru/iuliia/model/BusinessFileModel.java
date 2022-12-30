package ru.iuliia.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BusinessFileModel {
    public final String name;
    public final List<String> kids = new ArrayList<>();
    public final List<String> text;
    public static boolean colour;

    public BusinessFileModel(String x, List<String> z) {
        this.name = x;
        this.text = z;
        this.colour = false;
    }
    public void addKid(String y) {
        kids.add(y);
    }
    public BusinessFileModel findFile(List<BusinessFileModel> list, String file) {
        for (BusinessFileModel File : list) {
            if (Objects.equals(File.name, file)) {
                return File;
            }
        }
        return null;
    }
}
