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
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author w
 */
public class JsonHandler {

    private static final Gson GSON = new Gson();
    
    public static String serializeJson(int action, Object... requestArgs) {
        ArrayList<Object> jsonRequest = new ArrayList<>();
        jsonRequest.add(action);

        jsonRequest.addAll(Arrays.asList(requestArgs));

        return GSON.toJson(jsonRequest);
    }

    public static <T> T deserializeArray(String jsonResponse, Type arrayType) {
        return GSON.fromJson(jsonResponse, arrayType);
    }
}