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

public final class ApplicationRunner {
    private final ReadService readService;
    private final ErrorsHandler errorsHandler;
    private final PrintService printService;
    final ShelfRepository shelfRepository = new DefaultShelfRepository();

    public ApplicationRunner() {
        readService = new ReadFilesService(shelfRepository);
        errorsHandler = new CheckErrorsHandler(shelfRepository);
        printService = new PrintFilesService(shelfRepository, readService);
    }
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

    private void startProgram() {
        readService.fill();
        errorsHandler.check();
        printService.startPrinting();
    }
    private void runOneMoreTime() {
        System.out.print(MessageStorage.RUN_ONE_MORE_TIME);
        Scanner in = new Scanner(System.in);
        String answer = in.nextLine();
        if (Objects.equals(answer, "y")) {
            run();
        }
    }

    private void clearMemory() {
        shelfRepository.clear();
        errorsHandler.clear();
        printService.clear();
        readService.clear();
    }
}
