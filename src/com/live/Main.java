package com.live;

import com.live.aux.Replacer;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        try {
            File workDir = Paths.get(args[0]).toFile();
            String processing = args[1];
            File replaceable = Paths.get(args[2]).toFile();
            File shape = Paths.get(args[3]).toFile();

            System.out.println("Working directory: " + workDir.getAbsolutePath());
            System.out.println("File, being processed in subdirectories: " + processing);

            if(!replaceable.exists() || !replaceable.canRead()){
                throw new NullPointerException("Cannot read replaceable file.");
            }
            System.out.println("File, containing replaceable text: " + replaceable.getAbsolutePath());

            if(!shape.exists() || !shape.canRead()){
                throw new NullPointerException("Cannot read shape file.");
            }
            System.out.println("File, containing shape text: " + shape.getAbsolutePath());

            System.out.println("Are these params correct? Press ENTER to continue...");
            new BufferedReader(new InputStreamReader(System.in)).readLine();

            Replacer replacer = new Replacer(workDir, processing, replaceable, shape);
            replacer.replace();
        }  catch (NullPointerException e){
            System.out.println("Invalid param. " + e.getMessage());
        }catch (Exception e) {
            System.out.println("Error. Command line arguments are invalid.");
        }
    }
}
