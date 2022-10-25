package persistence;

import org.json.JSONObject;

// modeled from the persistence file given
public interface Writable {
    // EFFECTS: returns this as JSON object
    JSONObject toJson();
}
