package com.medici.app;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public final class WindowsFolderTree {

    private WindowsFolderTree() {
    }

    public static FolderNode build(Path root) throws IOException {
        Objects.requireNonNull(root, "root must not be null");

        if (!Files.exists(root, LinkOption.NOFOLLOW_LINKS)) {
            throw new IOException("Path does not exist: " + root);
        }

        return buildNode(root.toAbsolutePath().normalize());
    }

    private static FolderNode buildNode(Path path) throws IOException {
        boolean directory = Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS);
        List<FolderNode> children = null;

        if (directory) {

            children = new ArrayList<>();

            try (Stream<Path> entries = Files.list(path)) {
                List<Path> sortedEntries = entries
                        .sorted(Comparator
                                .comparing((Path entry) ->
                                        !Files.isDirectory(entry, LinkOption.NOFOLLOW_LINKS))
                                .thenComparing(entry ->
                                                entry.getFileName().toString(),
                                        String.CASE_INSENSITIVE_ORDER))
                        .toList();

                for (Path entry : sortedEntries) {
                    children.add(buildNode(entry));
                }
            }
        }

        return new FolderNode(
                path.getFileName() == null ? path.toString() : path.getFileName().toString(),
                path,
                directory,
                children);
    }

    public record FolderNode(
            String name,
            Path path,
            boolean directory,
            List<FolderNode> children) {

        public FolderNode {
            Objects.requireNonNull(name, "name must not be null");
            Objects.requireNonNull(path, "path must not be null");
            children = directory ? List.copyOf(children) : null;
        }

        public boolean isFile() {
            return !directory;
        }
    }

    public static void main(String[] args) throws IOException {
        Path root = new File("C:\\Users\\a73s\\dev\\NODE\\calendar").toPath();
        FolderNode result = build(root);
        result.children().stream().forEach(folderNode -> {
            System.out.println(folderNode.toString());
        });

    }
}
