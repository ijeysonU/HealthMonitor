package ws;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONObject;

public class statics {
    public static String urlGeneral = "http://192.168.0.106:8080/Pi8_v4/webresources/wsHMonitor/";
    public static String usuario;
    public static String paciente;
    public static String npaciente;
    public static String rol;

    public static String JsonToString(JsonObject jso, String param, String defaulx) {
        try {
            JsonElement res = securGetJSON(jso, param);
            if (res != null) {
                String result = res.getAsString();
                result = result.trim().replace("\n", "\\n").replace("\t", "\\t").replace("'", "''");
                return result;
            } else {
                return defaulx;
            }
        } catch (Exception e) {
//            System.out.println("erro json a string");
            return defaulx;
        }
    }
        public static JsonElement securGetJSON(JsonObject jso, String param) {
            try {
                JsonElement res = jso.get(param);//request.getParameter(param);
                return res;
            } catch (Exception e) {
                return null;
            }
        }
    }