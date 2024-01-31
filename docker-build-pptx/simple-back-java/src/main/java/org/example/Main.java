package org.example;

import com.google.gson.Gson;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static spark.Spark.*;

public class Main {
    public static void main(String[] args) {
        new Main().start(new ArrayList<>(Arrays.asList(args)));
    }

    private Path dataDir;

    private void start(List<String> args) {
        var state = "";
        while (!args.isEmpty()) {
            var arg = args.remove(0);
            switch (state) {
                case "" -> {
                    switch (arg) {
                        case "-data" -> {
                            state = "-data";
                        }
                        case "-static" -> {
                            state = "-static";
                        }
                        case "-index" -> {
                            state = "-index";
                        }
                        default -> {
                        }
                    }
                }
                case "-data" -> {
                    state = "";
                    dataDir = Path.of(arg);
                }
                case "-static" -> {
                    state = "";
                    externalStaticFileLocation(arg);
                }
                case "-index" -> {
                    state = "";
                    var idxFile = Path.of(arg);
                    get("/", (req, res) -> {
                        res.type("text/html");
                        return Files.readString(idxFile, StandardCharsets.UTF_8);
                    });
                }
                default -> {
                }
            }
        }

        if (dataDir == null) {
            System.err.println("dataDir not set !!!");
            System.exit(1);
        }

        Gson gson = new Gson();
        get("/note", (req, res) -> listNote(), gson::toJson);
        get("/note/:name", (req, res) -> this.readNote(req.params(":name")));
        post("/note/:name", (req, res) -> {
            putNote(req.params(":name"), req.body());
            return "ok";
        });
        delete("/note/:name", (req, res) -> {
            deleteNote(req.params(":name"));
            return "ok";
        });
    }

    private record ListResponse(List<String> notes) {
    }

    private String fname2note(Path path) {
        return path.getFileName().toString().replaceAll("(?is)\\.txt$", "");
    }

    private Path fnameOfNote(String name) {
        return dataDir.resolve(name + ".txt");
    }

    private ListResponse listNote() {
        try {
            return new ListResponse(
                Files
                    .list(dataDir)
                    .filter(Files::isRegularFile)
                    .filter(f -> f.toString().matches("(?is).*\\.txt"))
                    .map(this::fname2note)
                    .toList()
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void putNote(String name, String content) {
        try {
            Files.writeString(fnameOfNote(name), content, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void deleteNote(String name) {
        try {
            Files.deleteIfExists(fnameOfNote(name));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String readNote(String name) {
        try {
            return Files.readString(fnameOfNote(name), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}