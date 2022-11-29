package persistence;

import model.Board;
import org.json.*;
import ui.Game;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

// Class and method structure taken from https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git
// Represents a reader that reads board and moves from JSON data stored in file
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read to destination file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads workroom from file and returns it;
    // throws IOException if an error occurs reading data from file
    public Board read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseBoard(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(contentBuilder::append);
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses board from JSON object and returns it
    private Board parseBoard(JSONObject jsonObject) {
        Game game = new Game();
        JSONArray jsonArray = jsonObject.getJSONArray("moves made");
        for (Object json : jsonArray) {
            String move = String.valueOf(json);
            game.interpret(move);
        }
        game.setBoardTextState(jsonObject.getInt("text style"));
        return game.getBoard();
    }
}
