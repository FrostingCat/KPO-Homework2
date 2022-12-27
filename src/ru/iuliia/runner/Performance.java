package ru.iuliia.runner;

import ru.iuliia.entity.BusinessFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static ru.iuliia.handler.CheckErrors.*;
import static ru.iuliia.service.ReadFiles.fillInFiles;
import static ru.iuliia.service.PrintFiles.startPrinting;

public class Performance {
    public static final List<BusinessFile> bookShelf = new ArrayList<>();

    public static void startProgram() throws IOException {
        fillInFiles();
        checker();
        if (checkIfErrors()) {
            printErrors();
        } else {
            startPrinting();
        }
    }
}
