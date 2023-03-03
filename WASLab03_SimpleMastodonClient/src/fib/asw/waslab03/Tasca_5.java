package fib.asw.waslab03;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.hc.client5.http.fluent.Request;
import org.apache.hc.core5.http.ContentType;
import org.json.JSONArray;
import org.json.JSONObject;

public class Tasca_5 {

	public static void main(String[] args) {

		JSONObject body = new JSONObject();

		//ID PERSONA
		String URI = "https://mastodont.cat/api/v1/accounts/search/?q=fib_asw";
		String TOKEN = Token.get();
		String id_user = "";

		try {
			String output = Request.get(URI)
					.bodyString(body.toString(), ContentType.parse("application/json"))
					.addHeader("Authorization","Bearer "+TOKEN)
					.execute()
					.returnContent()
					.asString();

			JSONArray result = new JSONArray(output);
			JSONObject user = result.getJSONObject(0);
			id_user = user.getString("id");
			System.out.format("\n Id fib-asw: %s\n", id_user);

		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		
		//ID + TEXT DARRER TUT
		URI = "https://mastodont.cat/api/v1/accounts/"+ id_user + "/statuses";
		String id_lastTut = "";

		try {
			String output = Request.get(URI)
					.bodyString(body.toString(), ContentType.parse("application/json"))
					.addHeader("Authorization","Bearer "+TOKEN)
					.execute()
					.returnContent()
					.asString();

			JSONArray result = new JSONArray(output);
			JSONObject tut = result.getJSONObject(0);
			String txt_tut = tut.getString("content");
			id_lastTut = tut.getString("id");
			System.out.format("Text tut: %s", txt_tut);
			System.out.format("\n Id last tut fib-asw: %s\n", id_lastTut);

		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		
		//ID DARRER TUT
		URI = "https://mastodont.cat/api/v1/statuses/"+ id_lastTut + "/reblog";

		try {
			String output = Request.post(URI)
					.bodyString(body.toString(), ContentType.parse("application/json"))
					.addHeader("Authorization","Bearer "+TOKEN)
					.execute()
					.returnContent()
					.asString();
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
