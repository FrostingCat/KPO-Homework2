package ru.iuliia.repository;

import ru.iuliia.model.BusinessFileModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DefaultShelfRepository implements ShelfRepository {

    private final List<BusinessFileModel> bookShelf = new ArrayList<>();

    @Override
    public void addToShelf(String item, List<String> fullText, int dirLen) {
        item = item.substring(dirLen + 1, item.length() - 4);
        bookShelf.add(new BusinessFileModel(item, fullText));
    }

    @Override
    public void addToChild(String item, String kid, int dirLen) {
        item = item.substring(dirLen + 1, item.length() - 4);
        kid = kid.replace('/', '\\');
        for (BusinessFileModel businessFileModel : bookShelf) {
            if (Objects.equals(businessFileModel.name, item)) {
                businessFileModel.addKid(kid);
            }
        }
    }

    @Override
    public List<BusinessFileModel> getBookShelf() {
        return bookShelf;
    }

    @Override
    public void clear() {
        bookShelf.clear();
    }
}
