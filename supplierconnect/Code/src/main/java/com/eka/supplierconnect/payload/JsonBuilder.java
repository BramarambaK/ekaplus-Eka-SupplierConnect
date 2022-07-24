package com.eka.supplierconnect.payload;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * @author Vijayalakshmi.Nair
 *Utility class for jsonobject creation
 */
public class JsonBuilder {
    public final JsonObject json = new JsonObject();
    

    public String toJson() {
        return json.toString();
    }

    public JsonBuilder add(String key, String value) {
        json.addProperty(key, value);
        return this;
    }
   
    public JsonBuilder add(String key, JsonArray value) {
        json.add(key, value);
        return this;
    }

    public JsonBuilder add(String key, JsonBuilder value) {
        json.add(key, value.json);
        return this;
    }   
}