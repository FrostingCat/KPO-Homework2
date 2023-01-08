package ru.iuliia.repository;

import ru.iuliia.model.BusinessFileModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Класс для работы со всеми файлами
 */
public class DefaultShelfRepository implements ShelfRepository {
    /** Список для хранения всех файлов, их потомков и содержимого */
    private final List<BusinessFileModel> bookShelf = new ArrayList<>();

    /**
     * Функция добавления файла в список
     * @param item полное имя файла
     * @param fullText текст файла
     * @param dirLen кол-во символов в директории
     */
    @Override
    public void addToShelf(String item, List<String> fullText, int dirLen) {
        item = item.substring(dirLen + 1, item.length() - 4);
        bookShelf.add(new BusinessFileModel(item, fullText));
    }

    /**
     * Функция добавления потомка к файлу
     * @param item полное имя файла
     * @param kid имя файла потомка
     * @param dirLen кол-во символов в директории
     */
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

    /**
     * Функция получения доступа к списку
     * @return возвращает список
     */
    @Override
    public List<BusinessFileModel> getBookShelf() {
        return bookShelf;
    }

    /**
     * Функция очищения памяти, выделенной под список
     */
    @Override
    public void clear() {
        bookShelf.clear();
    }
}
