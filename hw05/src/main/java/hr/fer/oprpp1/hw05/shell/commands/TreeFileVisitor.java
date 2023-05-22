package hr.fer.oprpp1.hw05.shell.commands;

import hr.fer.oprpp1.hw05.shell.Environment;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

public class TreeFileVisitor extends SimpleFileVisitor<Path> {

    private int depth = 0;

    private Environment environment;

    public TreeFileVisitor(Environment environment){
        this.environment = environment;
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        printPath(dir);
        depth++;
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        printPath(file);

        return  FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
        depth--;

        return FileVisitResult.CONTINUE;
    }

    private void printPath(Path path){
        if(path != null && path.getFileName() != null){
            environment.writeln("  ".repeat(depth) + path.getFileName().toString());
        }
    }
}
