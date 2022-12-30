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

public class ReadFilesService implements ReadService {
    private String pathToMainDirectory;
    private final List<File> filesInFolder;
    private final ShelfRepository shelfRepository;


    public ReadFilesService(ShelfRepository shelfRepository) {
        this.filesInFolder = new ArrayList<>();
        this.shelfRepository = shelfRepository;
    }

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

    @Override
    public String getPathToMainDirectory() {
        return pathToMainDirectory;
    }

    @Override
    public void clear() {
        filesInFolder.clear();
    }

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
