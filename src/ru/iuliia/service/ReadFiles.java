package ru.iuliia.service;

import ru.iuliia.entity.MessageStorage;
import ru.iuliia.runner.Performance;

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

import static ru.iuliia.repository.FillShelf.addToChild;
import static ru.iuliia.repository.FillShelf.addToShelf;

public class ReadFiles extends Performance {
    protected static String pathToMainDirectory;
    private static List<File> filesInFolder;

    public static void fillInFiles() throws FileNotFoundException {
        Scanner in = new Scanner(System.in);
        System.out.print(MessageStorage.WRITE_ABSOLUTE_PATH);
        pathToMainDirectory = in.nextLine();
        try (Stream<Path> files = Files.walk(Paths.get(pathToMainDirectory))) {
            filesInFolder = files
                    .filter(Files::isRegularFile)
                    .map(Path::toFile)
                    .toList();

        } catch (IOException e) {
            System.out.println(MessageStorage.NO_DIRECTORY);
            System.exit(0);
        }

        for (File file : filesInFolder) {
            readFile(file);
        }
    }
    private static void readFile(File item) throws FileNotFoundException {
        List<String> text = new ArrayList<>();
        try (Scanner scanner = new Scanner(item)) {
            while (scanner.hasNextLine()) {
                text.add(scanner.nextLine());
            }
        }
        String itemStr = item.toString();
        int dirLen = pathToMainDirectory.length();
        addToShelf(itemStr, text, dirLen); // добавляем файл и его содержимое
        for (String s : text) {
            int index = s.indexOf("require");
            if (index > -1) {
                String str = s.substring(index + 9, s.length() - 1);
                addToChild(itemStr, str, dirLen);  // добавляем потомков файла
            }
        }
    }
}
