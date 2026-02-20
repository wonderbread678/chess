package model;

import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.Collection;

public record UserData(String username, String password, String email) {

    public String toString() {
        return new Gson().toJson(this);
    }
}
