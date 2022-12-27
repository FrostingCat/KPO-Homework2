package ru.iuliia.handler;

import ru.iuliia.entity.BusinessFile;
import ru.iuliia.entity.MessageStorage;
import ru.iuliia.runner.Performance;

import java.util.ArrayList;
import java.util.List;

public class CheckErrors extends Performance {
    private static final List<String> filesIncluded = new ArrayList<>();
    private static final List<String> recursiveFiles = new ArrayList<>();

    public static void checker() {
        checkInFiles();
        checkRecFiles();
    }
    private static void checkInFiles() {
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
    private static void checkRecFiles() {
        for (BusinessFile businessFile : bookShelf) {
            String check = businessFile.name;
            if (businessFile.kids.contains(check)) {
                recursiveFiles.add(check);
            }
        }
    }
    public static boolean checkIfErrors() {
        if (!filesIncluded.isEmpty() || !recursiveFiles.isEmpty()) {
            System.out.println(MessageStorage.ERROR_IN_FILE_RELATIONS);
            return true;
        }
        return false;
    }
    public static void printErrors() {
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
}
