/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

/**
 *
 * @author w
 */

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import model.Player;

/**
 *
 * @author w
 */
public class JsonHandler {

    private static final Gson gson = new Gson();
    
    public static String serializeJson(int action, Object... requestArgs) {
        ArrayList<Object> jsonRequest = new ArrayList<>();
        jsonRequest.add(action);

        jsonRequest.addAll(Arrays.asList(requestArgs));

        return gson.toJson(jsonRequest);
    }

    public static <T> T deserializeArray(String jsonResponse, Type arrayType) {
        return gson.fromJson(jsonResponse, arrayType);
    }
}