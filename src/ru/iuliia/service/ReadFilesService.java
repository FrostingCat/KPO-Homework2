package ru.iuliia.service;

import ru.iuliia.exception.HomeworkException;
import ru.iuliia.exception.IgnoredException;
import ru.iuliia.exception.IncorrectDirectoryException;
import ru.iuliia.model.MessageStorage;
import ru.iuliia.repository.ShelfRepository;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

/**
 * Класс для чтения файлов и записи их в список
 */
public class ReadFilesService implements ReadService {
    /** Путь к файлу */
    private String pathToMainDirectory;
    /** Список файлов в папке */
    private final List<File> filesInFolder;
    /** Переменная для хранения доступа к интерфейсу ShelfRepository */
    private final ShelfRepository shelfRepository;

    /**
     * Конструктор - инициализация списка для всех файлов и получения доступа к ShelfRepository
     * @param shelfRepository переменная для хранения доступа к интерфейсу ShelfRepository
     */
    public ReadFilesService(ShelfRepository shelfRepository) {
        this.filesInFolder = new ArrayList<>();
        this.shelfRepository = shelfRepository;
    }

    /**
     * Функция, считывающая файлы из папки и помещающая их в список
     * @exception IncorrectDirectoryException выбрасывается ошибка в случае неправильной директории
     */
    @Override
    public void fill() throws HomeworkException {
        Scanner in = new Scanner(System.in);
        System.out.print(MessageStorage.WRITE_ABSOLUTE_PATH);
        pathToMainDirectory = in.nextLine();
        filesInFolder.clear();

        try (Stream<Path> files = Files.walk(Paths.get(pathToMainDirectory))) {
            filesInFolder.addAll(files
                    .filter(Files::isRegularFile)
                    .map(Path::toFile)
                    .toList());

        } catch (IOException e) {
            throw new IncorrectDirectoryException(MessageStorage.NO_DIRECTORY, e);
        }

        for (File file : filesInFolder) {
            readFile(file);
        }
    }

    /**
     * Функция для доступа к пути к папке
     * @return возвращает путь к папке
     */
    @Override
    public String getPathToMainDirectory() {
        return pathToMainDirectory;
    }

    /**
     * Функция очищения выделенной памяти
     */
    @Override
    public void clear() {
        filesInFolder.clear();
    }

    /**
     * Функция для создания списка файлов, их содержимого и потомков
     * @param item название файла
     * @exception IgnoredException выбрасывает ошибку в случае отсутствия файла в папке
     */
    private void readFile(File item) {
        List<String> text = new ArrayList<>();
        try (Scanner scanner = new Scanner(item)) {
            while (scanner.hasNextLine()) {
                text.add(scanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            throw new IgnoredException(e);
        }
        String itemStr = item.toString();
        int dirLen = pathToMainDirectory.length();
        shelfRepository.addToShelf(itemStr, text, dirLen); // добавляем файл и его содержимое
        for (String s : text) {
            int index = s.indexOf("require");
            if (index > -1) {
                String str = s.substring(index + 9, s.length() - 1);
                shelfRepository.addToChild(itemStr, str, dirLen);  // добавляем потомков файла
            }
        }
    }
}
