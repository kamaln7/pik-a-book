package example;

import java.io.IOException;

import javax.servlet.ServletResponse;

import com.google.gson.JsonObject;

public class Helpers {
	public static void JSONError(String message, ServletResponse response) throws IOException {
		JsonObject o = new JsonObject();
		o.addProperty("error", true);
		o.addProperty("message", message);
		
		response.getWriter().write(o.toString());
	}
}
