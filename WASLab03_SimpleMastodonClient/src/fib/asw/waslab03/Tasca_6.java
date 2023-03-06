package fib.asw.waslab03;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import org.apache.hc.client5.http.fluent.Request;
import org.apache.hc.core5.http.ContentType;
import org.json.JSONArray;
import org.json.JSONObject;

public class Tasca_6 {
	
	private static final String LOCALE = "ca";
	
	public static String procesadoHTML(String content) {
		String result = "";
		boolean b = false;
        char[] myChars = content.toCharArray();
        for (int i=0; i<myChars.length; i++){
        	if(b && myChars[i] != '<') {
        		result += myChars[i];
        	}
        	else {
        		if(myChars[i] == '<') b = false;
        		else if(myChars[i] == '>') b = true;
        	}
        }
		return result;
	}

	public static void main(String[] args) {

		JSONObject body = new JSONObject();
		String TOKEN = Token.get();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 'a les' HH:mm:ss", new Locale(LOCALE));
		String now = sdf.format(new Date());
		
		System.out.format("Els 10 tags més populars a Mastodon [" + now + "]");

		//TRENDS
		String URI = "https://mastodont.cat/api/v1/trends/tags/?limit=10";

		try {
			String output = Request.get(URI)
					.bodyString(body.toString(), ContentType.parse("application/json"))
					.addHeader("Authorization","Bearer "+TOKEN)
					.execute()
					.returnContent()
					.asString();

			JSONArray result = new JSONArray(output);
			for (int i = 0; i < 10; i++) {
				JSONObject trend = result.getJSONObject(i);
				String trend_name = trend.getString("name");
				System.out.format("\n\n*************************************************");
				System.out.format("\n * Tag: %s\n",trend_name);
				System.out.format("*************************************************");

					URI = "https://mastodont.cat/api/v1/timelines/tag/"+trend_name+"/?limit=5";

					try {
						String output2 = Request.get(URI)
								.bodyString(body.toString(), ContentType.parse("application/json"))
								.addHeader("Authorization","Bearer "+TOKEN)
								.execute()
								.returnContent()
								.asString();

						JSONArray result2 = new JSONArray(output2);
						for(int j = 0; j < 5; j++) {
							JSONObject tut = result2.getJSONObject(j);
							String tut_id = tut.getString("id");
							URI = "https://mastodont.cat/api/v1/statuses/"+ tut_id;
							try {
								String output3 = Request.get(URI)
										.bodyString(body.toString(), ContentType.parse("application/json"))
										.addHeader("Authorization","Bearer "+TOKEN)
										.execute()
										.returnContent()
										.asString();
								JSONObject result3 = new JSONObject(output3);
								JSONObject account = result3.getJSONObject("account");
								String tut_name = account.getString("display_name");
								String tut_username = account.getString("acct");
								String tut_content = result3.getString("content");
								
								System.out.format("\n - %s (@%s): %s\n",tut_name, tut_username, procesadoHTML(tut_content));
								System.out.format("-------------------------------------------------");
							}
							catch (Exception ex) {
								ex.printStackTrace();
							}

					}
					}
					catch (Exception ex) {
						ex.printStackTrace();
					}
			}

		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		
		//hace falta 5 tuts para cada tag i de esos tuts el nombre de la persona, el contenido no HTML y username

}
}
