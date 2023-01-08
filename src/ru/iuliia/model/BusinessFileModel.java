package ru.iuliia.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
/**
 * Класс для создания структуры файла
 */
public class BusinessFileModel {
    /** Имя файла */
    public final String name;
    /** Потомки файла */
    public final List<String> kids = new ArrayList<>();
    /** Содержимое файла */
    public final List<String> text;

    /**
     * Конструктор - создание нового объекта с определенными значениями
     * @param x имя файла
     * @param z содержимое файла
     */
    public BusinessFileModel(String x, List<String> z) {
        this.name = x;
        this.text = z;
    }

    /**
     * Функция добавления потомка определенному файлу
     * @param y имя потомка
     */
    public void addKid(String y) {
        kids.add(y);
    }

    /**
     * Функция поиска файла
     * @param list список файлов, в которых ведется поиск
     * @param file имя файла для поиска
     * @return возвращает найденный файл в случае его нахождения, иначе null
     */
    public BusinessFileModel findFile(List<BusinessFileModel> list, String file) {
        for (BusinessFileModel File : list) {
            if (Objects.equals(File.name, file)) {
                return File;
            }
        }
        return null;
    }
}
