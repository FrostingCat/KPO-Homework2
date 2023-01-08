package ru.iuliia.handler;

import ru.iuliia.exception.FileRelationException;
import ru.iuliia.exception.HomeworkException;
import ru.iuliia.model.BusinessFileModel;
import ru.iuliia.model.MessageStorage;
import ru.iuliia.repository.ShelfRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс, проверяющий файлы на содержание ошибок, а именно: цикличность и рекурсивный вызов файла.
 */

public class CheckErrorsHandler implements ErrorsHandler {
    /** Список цикличных файлов */
    private final List<String> filesIncluded = new ArrayList<>();
    /** Список файлов, рекурсивно вызывающих сами себя */
    private final List<String> recursiveFiles = new ArrayList<>();
    private final ShelfRepository shelfRepository;

    public CheckErrorsHandler(ShelfRepository shelfRepository) {
        this.shelfRepository = shelfRepository;
    }

    /**
     * Функция проверки ошибок
     */
    @Override
    public void check() {
        checkInFiles();
        checkRecursionFiles();
        checkErrors();
    }

    /**
     * Функция вывода имеюшихся ошибок в консоль
     */
    @Override
    public void print() {
        if (!filesIncluded.isEmpty()) {
            System.out.println(MessageStorage.ERROR_WITH_TWO_FILES);
            for (int i = 0; i < filesIncluded.size(); i += 2) {
                System.out.println(filesIncluded.get(i) + " и " + filesIncluded.get(i + 1));
            }
            System.out.println();
        }
        if (!recursiveFiles.isEmpty()) {
            System.out.println(MessageStorage.RECURSIVE_FILES);
            for (Object obj : recursiveFiles) {
                System.out.println(obj);
            }
        }
    }

    /**
     * Функция очищения памяти для списков
     */
    @Override
    public void clear() {
        filesIncluded.clear();
        recursiveFiles.clear();
    }

    /**
     * Функция, проверяющая цикличность файлов
     */
    private void checkInFiles() {
        final List<BusinessFileModel> bookShelf = shelfRepository.getBookShelf();
        for (int i = 0; i < bookShelf.size(); i++) {
            String check = bookShelf.get(i).name;
            for (int j = i + 1; j < bookShelf.size(); j++) {
                int index = bookShelf.get(j).kids.indexOf(check);
                if (index > -1) {
                    String checkStr = bookShelf.get(j).name;
                    if (bookShelf.get(i).kids.contains(checkStr)) {
                        filesIncluded.add(check);
                        filesIncluded.add(checkStr);
                    }
                }
            }
        }
    }

    /**
     * Функция, проверяющая рекурсивный вызов файлов
     */
    private void checkRecursionFiles() {
        final List<BusinessFileModel> bookShelf = shelfRepository.getBookShelf();
        for (BusinessFileModel businessFileModel : bookShelf) {
            String check = businessFileModel.name;
            if (businessFileModel.kids.contains(check)) {
                recursiveFiles.add(check);
            }
        }
    }

    /**
     * Функция, проверяющая выбрасывающая кастомную ошибку в случае нарушения зависимостей файлов
     * @exception FileRelationException выбрасывает ошибку в зависимости файлов
     */
    private void checkErrors() throws HomeworkException {
        if (!filesIncluded.isEmpty() || !recursiveFiles.isEmpty()) {
            throw new FileRelationException();
        }
    }
}
