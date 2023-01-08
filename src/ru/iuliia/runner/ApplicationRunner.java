package ru.iuliia.runner;

import ru.iuliia.exception.FileRelationException;
import ru.iuliia.exception.IgnoredException;
import ru.iuliia.exception.IncorrectDirectoryException;
import ru.iuliia.handler.CheckErrorsHandler;
import ru.iuliia.handler.ErrorsHandler;
import ru.iuliia.model.MessageStorage;
import ru.iuliia.repository.DefaultShelfRepository;
import ru.iuliia.repository.ShelfRepository;
import ru.iuliia.service.PrintFilesService;
import ru.iuliia.service.PrintService;
import ru.iuliia.service.ReadFilesService;
import ru.iuliia.service.ReadService;

import java.util.Objects;
import java.util.Scanner;

/**
 * Класс для чтения, поиска ошибок, вывода содержимого файлов и составление конечного списка
 */
public final class ApplicationRunner {
    /** Переменная для хранения доступа к интерфейсу readService */
    private final ReadService readService;
    /** Переменная для хранения доступа к интерфейсу errorsHandler */
    private final ErrorsHandler errorsHandler;
    /** Переменная для хранения доступа к интерфейсу printService */
    private final PrintService printService;
    /** Переменная для хранения всех файлов */
    final ShelfRepository shelfRepository = new DefaultShelfRepository();

    /**
     * Конструктор - вызов конструкторов в классах
     */
    public ApplicationRunner() {
        readService = new ReadFilesService(shelfRepository);
        errorsHandler = new CheckErrorsHandler(shelfRepository);
        printService = new PrintFilesService(shelfRepository, readService);
    }

    /**
     * Функция запуска работы программы
     */
    public void run() {
        try {
            startProgram();
        } catch (IncorrectDirectoryException | IgnoredException e) {
            System.out.println(e.getMessage());
        } catch (FileRelationException e) {
            System.out.println(e.getMessage());
            errorsHandler.print();
        }
        clearMemory();
        runOneMoreTime();
    }

    /**
     * Функция для считывания файла, поиска ошибок и вывода содержимого файлов
     */
    private void startProgram() {
        readService.fill();
        errorsHandler.check();
        printService.startPrinting();
    }

    /**
     * Функция повторного запуска программы
     */
    private void runOneMoreTime() {
        System.out.print(MessageStorage.RUN_ONE_MORE_TIME);
        Scanner in = new Scanner(System.in);
        String answer = in.nextLine();
        if (Objects.equals(answer, "y")) {
            run();
        }
    }

    /**
     * Функция очищения выделенной памяти
     */
    private void clearMemory() {
        shelfRepository.clear();
        errorsHandler.clear();
        printService.clear();
        readService.clear();
    }
}
