package ru.iuliia.repository;

import ru.iuliia.model.BusinessFileModel;

import java.util.List;

public interface ShelfRepository {
    void addToShelf(String item, List<String> text, int dirLen);
    void addToChild(String item, String text, int dirLen);
    List<BusinessFileModel> getBookShelf();
    void clear();
}
