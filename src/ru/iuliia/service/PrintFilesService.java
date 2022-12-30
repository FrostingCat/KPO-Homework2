package ru.iuliia.service;

import ru.iuliia.exception.FileRelationException;
import ru.iuliia.exception.IgnoredException;
import ru.iuliia.model.BusinessFileModel;
import ru.iuliia.model.MessageStorage;
import ru.iuliia.repository.ShelfRepository;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PrintFilesService implements PrintService {
    private String startFile;
    private final List<String> finalList;
    private int startingSize;
    private FileOutputStream out;
    private final ShelfRepository shelfRepository;
    private final ReadService readService;
    public PrintFilesService(ShelfRepository shelfRepository, ReadService readService) {
        this.finalList = new ArrayList<>();
        this.shelfRepository = shelfRepository;
        this.readService = readService;
    }


    @Override
    public void startPrinting() {
        String pathToMainDirectory = readService.getPathToMainDirectory();
        try {
            out = new FileOutputStream(pathToMainDirectory + (MessageStorage.RESULTS_OUTPUT));
        } catch (FileNotFoundException e) {
            throw new IgnoredException(e);
        }

        final List<BusinessFileModel> bookShelf = shelfRepository.getBookShelf();
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

    @Override
    public void clear() {
        finalList.clear();
    }

    private void printingFiles() {
        while (finalList.size() != startingSize) {
            findStartFile();
            if (finalList.contains(startFile)) {
                throw new FileRelationException();
            }
            BusinessFileModel nextFile = findNextFile(startFile);
            if (nextFile != null) {
                recursionPrint(false, nextFile);
            }
            if (finalList.size() != startingSize) {
                throw new FileRelationException();
            }
        }
    }
    private void recursionPrint(boolean isAKid, BusinessFileModel checkingFile) {
        if (checkingFile == null) {
            return;
        }
        if (!checkingFile.kids.isEmpty()) {
            for (int i = 0; i < checkingFile.kids.size(); i++) { // проверяем потомков
                if (!finalList.contains(checkingFile.kids.get(i))) {
                    final List<BusinessFileModel> bookShelf = shelfRepository.getBookShelf();
                    recursionPrint(true, checkingFile.findFile(bookShelf, checkingFile.kids.get(i)));
                }
            }
            for (String obj : checkingFile.text) { // печатаем
                try {
                    out.write(obj.getBytes());
                    out.write('\n');
                } catch (IOException e) {
                    throw new IgnoredException(e);
                }
            }
            if (finalList.contains(checkingFile.name)) {
                throw new FileRelationException();
            }
            finalList.add(checkingFile.name); // добавляем в list
            if (!isAKid) {
                recursionPrint(false, findNextFile(checkingFile.name));
            }
        }
    }
    private BusinessFileModel findNextFile(String startFile) {
        final List<BusinessFileModel> bookShelf = shelfRepository.getBookShelf();
        for (BusinessFileModel businessFileModel : bookShelf) {
            List<String> fileKids = businessFileModel.kids;
            if (fileKids.contains(startFile)) {
                return businessFileModel;
            }
        }
        return null;
    }
    private void findStartFile() {
        final List<BusinessFileModel> bookShelf = shelfRepository.getBookShelf();
        for (BusinessFileModel businessFileModel : bookShelf) {
            if (businessFileModel.kids.isEmpty() && !businessFileModel.colour) {
                startFile = businessFileModel.name;
                finalList.add(startFile);
                for (String obj : businessFileModel.text) {
                    try {
                        out.write(obj.getBytes());
                        out.write('\n');
                    } catch (IOException e) {
                        throw new IgnoredException(e);
                    }
                }
                businessFileModel.colour = true;
                break;
            }
        }
    }
    private void printList()  {
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
