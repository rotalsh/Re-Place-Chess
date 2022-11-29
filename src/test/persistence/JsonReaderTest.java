package persistence;

import model.Board;
import model.Team;
import model.piece.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JsonReaderTest {
    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            Board bd = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyWorkRoom() {
        JsonReader reader = new JsonReader("./data/testReaderNoMovesMade.json");
        try {
            Board bd = reader.read();
            assertTrue(bd.getLiteralMoves().isEmpty());
            assertTrue(bd.getMovesMade().isEmpty());
            assertTrue(bd.getCapturedPieces().isEmpty());
            assertEquals(0, bd.getGameState());
            assertEquals(Team.WHITE, bd.getTurn());
            assertEquals(0, bd.getTextState());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralWorkRoom() {
        JsonReader reader = new JsonReader("./data/testReaderSomeMovesMade.json");
        try {
            Board bd = reader.read();
            assertEquals(7, bd.getLiteralMoves().size());
            assertEquals(Arrays.asList("Pb3", "Bb3", "Bb2", "Bc4", "Bc3", "Kc3", "@Pa2"),
                    bd.getLiteralMoves());
            assertEquals(7, bd.getMovesMade().size());
            assertEquals(Arrays.asList("Pxb3", "Bxb3", "Bb2", "Bc4", "Bc3", "Kxc3", "@Pa2"),
                    bd.getMovesMade());
            assertEquals(2, bd.getCapturedPieces().size());
            assertEquals(0, bd.getGameState());
            assertEquals(Team.BLACK, bd.getTurn());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
}
