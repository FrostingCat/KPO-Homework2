package ru.iuliia.service;

import ru.iuliia.entity.BusinessFile;
import ru.iuliia.entity.MessageStorage;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PrintFiles extends ReadFiles {
    static String startFile;
    static List<String> finalList = new ArrayList<>();
    static int startingSize;
    static FileOutputStream out;

    public static void startPrinting() throws IOException {
        out = new FileOutputStream(pathToMainDirectory + (MessageStorage.RESULTS_OUTPUT));
        startingSize = bookShelf.size();
        out.write(MessageStorage.RESULTS_TEXT.getBytes());
        printingFiles();
        out.write(MessageStorage.RESULTS_LIST.getBytes());
        printList();
        out.close();
    }

    private static void printingFiles() throws IOException {
        while (finalList.size() != startingSize) {
            findStartFile();
            BusinessFile nextFile = findNextFile(startFile);
            if (nextFile != null) {
                recursionPrint(false, nextFile);
            }
        }
    }
    private static void recursionPrint(boolean isAKid, BusinessFile checkingFile) throws IOException {
        if (checkingFile == null) {
            return;
        }
        if (!checkingFile.kids.isEmpty()) {
            for (int i = 0; i < checkingFile.kids.size(); i++) { // проверяем потомков
                if (!finalList.contains(checkingFile.kids.get(i))) {
                    recursionPrint(true, checkingFile.findFile(bookShelf, checkingFile.kids.get(i)));
                }
            }
            for (String obj : checkingFile.text) { // печатаем
                out.write(obj.getBytes());
                out.write('\n');
                System.out.println(obj);
            }
            finalList.add(checkingFile.name); // добавляем в list
            if (!isAKid) {
                recursionPrint(false, findNextFile(checkingFile.name));
            }
        }
    }
    private static BusinessFile findNextFile(String startFile) {
        for (BusinessFile businessFile : bookShelf) {
            List<String> fileKids = businessFile.kids;
            if (fileKids.contains(startFile)) {
                return businessFile;
            }
        }
        return null;
    }
    private static void findStartFile() throws IOException {
        for (BusinessFile businessFile : bookShelf) {
            if (businessFile.kids.isEmpty() && !businessFile.colour) {
                startFile = businessFile.name;
                finalList.add(startFile);
                for (String obj : businessFile.text) {
                    out.write(obj.getBytes());
                    out.write('\n');
                    System.out.println(obj);
                }
                businessFile.colour = true;
                break;
            }
        }
    }
    private static void printList() throws IOException {
        for (String obj: finalList) {
            out.write(obj.getBytes());
            out.write('\n');
        }
    }
}
