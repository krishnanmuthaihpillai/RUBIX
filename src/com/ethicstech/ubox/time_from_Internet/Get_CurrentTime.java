package com.ethicstech.ubox.time_from_Internet;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.json.JSONObject;

public class Get_CurrentTime {
	public String mytime(final String url) {
		String timeAsString = "";
		try {
			JsonParser jParser = new JsonParser();
			JSONObject json = jParser.getJSONFromUrl(url);
			String tmzone = json.getString("timezone");
			DateTimeZone denverTimeZone = DateTimeZone.forID(tmzone);
			DateTime denverDateTime = new DateTime(denverTimeZone);
			timeAsString = denverDateTime.toLocalTime().toString();
			 String[] tem1 = timeAsString.split(":");
             String hr = tem1[0];
             String mt = tem1[1];
             StringBuilder content = new StringBuilder();
             content.append(hr+":"+ mt);
              timeAsString =content.toString();
              SimpleDateFormat m24HourSDF = new SimpleDateFormat("HH:mm");
              SimpleDateFormat m12HourSDF = new SimpleDateFormat("hh:mm a");
              Date m24HourDt = m24HourSDF.parse(timeAsString);
              timeAsString=m12HourSDF.format(m24HourDt);           
		} catch (Exception e) {
			timeAsString="";
//			e.printStackTrace();
		}
		return timeAsString;
	}
}
