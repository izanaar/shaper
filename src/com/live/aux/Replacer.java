package com.live.aux;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class Replacer {

    private File workDir;
    private String processing;
    private File replaceable;
    private File shape;

    private String replaceableText;
    private String shapeText;

    public Replacer(File workDir, String processing, File replaceable, File shape) {
        this.workDir = workDir;
        this.processing = processing;
        this.replaceable = replaceable;
        this.shape = shape;
    }

    public void replace(){
        if(!initReplace()){
            return;
        }

        Arrays.asList(workDir.listFiles(File::isDirectory)).forEach(this::tryReplaceFileContent);
    }

    private boolean initReplace() {
        Path replaceablePath = replaceable.toPath();
        Path shapePath = shape.toPath();

        try {
            if(Files.size(replaceablePath) == 0){
                System.out.println("Error. Replaceable file is empty.");
                return false;
            }
            replaceableText = collectText(replaceablePath);

            if(Files.size(shapePath) == 0){
                System.out.println("Error. Replaceable file is empty.");
                return false;
            }
            shapeText = collectText(shapePath);

            return true;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    private String collectText(Path filePath) throws IOException {
        StringBuilder buffer = new StringBuilder();
        Files.readAllLines(filePath, Charset.forName("UTF-8")).forEach(text ->{
            if(buffer.length() != 0){
                buffer.append(System.getProperty("line.separator"));
            }
            buffer.append(text.trim());
        });
        return buffer.toString();
    }

    private void tryReplaceFileContent(File rootDir){
        System.out.print("Processing directory " + rootDir.getName() +". ");
        Path currentProcessingFile = Paths.get(rootDir.getAbsolutePath(),processing);
        if(Files.exists(currentProcessingFile)){
            checkFileForReadability(currentProcessingFile);
        }else{
            System.out.println("The directory does not contain the file.");
        }
    }

    private void checkFileForReadability(Path currentProcessingFile) {
        System.out.print("File was found. Attempting to replace... ");
        if(Files.isReadable(currentProcessingFile) && Files.isWritable(currentProcessingFile)){
            replaceFileContent(currentProcessingFile);
        }else{
            System.out.print("Error. Cannot read or write the file.");
        }
    }

    private void replaceFileContent(Path currentProcessingFile) {
        try {
            StringBuilder builder = new StringBuilder(collectText(currentProcessingFile));

            int replaceableStartIndex = builder.indexOf(replaceableText);

            if(replaceableStartIndex > 0){
                builder.replace(replaceableStartIndex, replaceableStartIndex + replaceableText.length(), shapeText);
                Files.write(currentProcessingFile, builder.toString().getBytes());
                System.out.println("Replaced.");
            }else {
                System.out.println("File does not contains text we're looking for.");
            }
        } catch (IOException e) {
            System.out.println("Unexpected exception occurred while file reading.");
        }
    }
}
