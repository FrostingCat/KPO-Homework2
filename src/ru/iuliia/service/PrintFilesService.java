package ru.iuliia.service;

import ru.iuliia.exception.FileRelationException;
import ru.iuliia.exception.HomeworkException;
import ru.iuliia.exception.IgnoredException;
import ru.iuliia.model.BusinessFileModel;
import ru.iuliia.model.MessageStorage;
import ru.iuliia.repository.ShelfRepository;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс для вывода полученного списка и содержимого файлов
 */
public class PrintFilesService implements PrintService {
    /** Имя файла, с которого начинается вывод */
    private String startFile;
    /** Список файлов для вывода */
    private final List<String> finalList;
    /** Список проверенных файлов */
    private final List<String> checkedFiles;
    /** Кол-во всех файлов */
    private int startingSize;
    /** Поток для вывода */
    private FileOutputStream out;
    /** Переменная для хранения доступа к интерфейсу readService */
    private final ReadService readService;
    /** Список всех файлов */
    final List<BusinessFileModel> bookShelf;

    /**
     * Конструктор - получение доступа к ShelfRepository и ReadService, создание пустого списка для файлов и списка проверенных файлов
     * @param shelfRepository переменная для хранения доступа к интерфейсу ShelfRepository
     * @param readService переменная для хранения доступа к интерфейсу ReadService
     */
    public PrintFilesService(ShelfRepository shelfRepository, ReadService readService) {
        this.finalList = new ArrayList<>();
        this.readService = readService;
        bookShelf = shelfRepository.getBookShelf();
        checkedFiles = new ArrayList<>();
    }

    /**
     * Функция, подготавливающая поток для вывода файлов и запускающая процесс их вывода
     * @exception IgnoredException выбрасывает ошибку в случае ошибки создания файла для вывода или ошибки вывода содержимого файла
     */
    @Override
    public void startPrinting() throws HomeworkException {
        String pathToMainDirectory = readService.getPathToMainDirectory();
        try {
            out = new FileOutputStream(pathToMainDirectory + (MessageStorage.RESULTS_OUTPUT));
        } catch (FileNotFoundException e) {
            throw new IgnoredException(e);
        }
        startingSize = bookShelf.size();
        try {
            out.write(MessageStorage.RESULTS_TEXT.getBytes());
            printingFiles();
            out.write(MessageStorage.RESULTS_LIST.getBytes());
            printList();
            out.close();
        } catch (IOException e) {
            throw new IgnoredException(e);
        }
    }

    /**
     * Функция очищения выделенной памяти
     */
    @Override
    public void clear() {
        finalList.clear();
        bookShelf.clear();
    }

    /**
     * Функция для поиска начального файла для вывода и запуска процесса печатания
     * @exception FileRelationException выбрасывает ошибку в зависимости файлов
     */
    private void printingFiles() throws HomeworkException {
        while (finalList.size() != startingSize) {
            findStartFile();
            if (startFile == null) {  // если файл не нашли
                throw new FileRelationException();
            }
            BusinessFileModel nextFile = findNextFile(startFile);
            if (nextFile != null) {
                recursionPrint(false, nextFile);
            }
            if (finalList.size() > startingSize) {
                throw new FileRelationException();
            }
        }
    }

    /**
     * Рекурсивная функция для вывода содержимого файла
     * @param isAKid переменная, показывающая, проверяем ли мы потомка
     * @param checkingFile проверяемый файл
     * @exception FileRelationException выбрасывает ошибку в зависимости файлов
     * @exception IgnoredException ошибка при выводе текста
     */
    private void recursionPrint(boolean isAKid, BusinessFileModel checkingFile) throws HomeworkException {
        if (checkingFile == null) {
            return;
        }
        if (!checkingFile.kids.isEmpty()) {
            for (int i = 0; i < checkingFile.kids.size(); i++) { // проверяем потомков
                if (!finalList.contains(checkingFile.kids.get(i)) && !checkedFiles.contains(checkingFile.kids.get(i))) {
                    checkedFiles.add(checkingFile.kids.get(i));
                    recursionPrint(true, checkingFile.findFile(bookShelf, checkingFile.kids.get(i)));
                }
            }
        }
        if (finalList.contains(checkingFile.name)) {
            throw new FileRelationException();
        }
        for (String obj : checkingFile.text) { // печатаем
            try {
                out.write(obj.getBytes());
                out.write('\n');
            } catch (IOException e) {
                throw new IgnoredException(e);
            }
        }
        finalList.add(checkingFile.name); // добавляем в list
        if (!isAKid) {
            recursionPrint(false, findNextFile(checkingFile.name)); // проверяем предков
        }
    }

    /**
     * Функция для поиска родителя для файла без потомков
     * @param startFile начальный файл
     * @return возвращает найденный файл или null, если файл не был найден
     * @exception FileRelationException выбрасывает ошибку в зависимости файлов
     */
    private BusinessFileModel findNextFile(String startFile) throws HomeworkException {
        for (BusinessFileModel businessFileModel : bookShelf) {
            List<String> fileKids = businessFileModel.kids;
            if (fileKids.contains(startFile)) {
                return businessFileModel;
            }
        }
        return null;
    }

    /**
     * Функция для поиска файла без потомков
     * @exception FileRelationException выбрасывает ошибку в зависимости файлов
     * @exception IgnoredException ошибка при выводе текста
     */
    private void findStartFile() throws HomeworkException {
        for (BusinessFileModel businessFileModel : bookShelf) {
            if (businessFileModel.kids.isEmpty() && !finalList.contains(businessFileModel.name)) {
                startFile = businessFileModel.name;
                if (finalList.contains(startFile)) {
                    throw new FileRelationException();
                }
                finalList.add(startFile);
                for (String obj : businessFileModel.text) {
                    try {
                        out.write(obj.getBytes());
                        out.write('\n');
                    } catch (IOException e) {
                        throw new IgnoredException(e);
                    }
                }
                break;
            }
        }
    }

    /**
     * Функция для вывода полученного списка файлов
     * @exception IgnoredException ошибка при выводе текста
     */
    private void printList() throws HomeworkException {
        for (String obj: finalList) {
            try {
                out.write(obj.getBytes());
                out.write('\n');
            } catch (IOException e) {
                throw new IgnoredException(e);
            }
        }
    }
}
