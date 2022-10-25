package persistence;

import model.Board;
import model.Team;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JsonWriterTest {
    @Test
    void testWriterInvalidFile() {
        try {
            Board bd = new Board();
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyWorkroom() {
        try {
            Board bd = new Board();
            JsonWriter writer = new JsonWriter("./data/testWriterNoMovesMade.json");
            writer.open();
            writer.write(bd);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterNoMovesMade.json");
            bd = reader.read();
            assertTrue(bd.getLiteralMoves().isEmpty());
            assertTrue(bd.getMovesMade().isEmpty());
            assertTrue(bd.getCapturedPieces().isEmpty());
            assertEquals(0, bd.getGameState());
            assertEquals(Team.WHITE, bd.getTurn());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralWorkroom() {
        JsonReader reader = new JsonReader("./data/testReaderSomeMovesMade.json");
        try {
            Board bd = reader.read();

            JsonWriter writer = new JsonWriter("./data/testWriterSomeMovesMade.json");

            writer.open();
            writer.write(bd);
            writer.close();

            reader = new JsonReader("./data/testWriterSomeMovesMade.json");
            bd = reader.read();

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
            fail("Exception should not have been thrown");
        }
    }
}
