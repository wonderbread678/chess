package client;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class Serializer {

    public String serialize(String item){
        return new Gson().toJson(item);
    }

    public <T> T deserialize(String item, Class<T> classType){
        return new Gson().fromJson(item, classType);
    }
}
