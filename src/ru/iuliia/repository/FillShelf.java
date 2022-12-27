package ru.iuliia.repository;

import ru.iuliia.entity.BusinessFile;
import ru.iuliia.service.ReadFiles;

import java.util.List;
import java.util.Objects;

public class FillShelf extends ReadFiles {
    public static void addToShelf(String item, List<String> text, int dirLen) {
        item = item.substring(dirLen + 1, item.length() - 4);
        bookShelf.add(new BusinessFile(item, text));
    }
    public static void addToChild(String item, String kid, int dirLen) {
        item = item.substring(dirLen + 1, item.length() - 4);
        kid = kid.replace('/', '\\');
        for (BusinessFile businessFile : bookShelf) {
            if (Objects.equals(businessFile.name, item)) {
                businessFile.addKid(kid);
            }
        }
    }
}
