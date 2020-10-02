package fr.iconvoit;

import java.io.IOException;
import java.util.List;

import fr.iconvoit.entity.Localization;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 
 * @author Émilien and Jéremy work together in remote. Émilien back-End, Jéremy
 *         front end (get data from the map)
 *
 */
public class Graph {

	public static void planTraject(Localization start, Localization end, List<Localization> step) {
		/*
		for (Localization anStep : step) {
			url += "point=" + anStep.getLatitude() + "," + anStep.getLongitude();
		}*/
		
		/**
		 * Code from : https://docs.graphhopper.com/#tag/Routing-API little bit edited
		 * for our API Need to add
		 * https://mvnrepository.com/artifact/com.squareup.okhttp3/okhttp
		 */
		OkHttpClient client = new OkHttpClient();
		Request request = new Request.Builder().url(urlTravel(start,end)).get().build();

		try {
			Response response = client.newCall(request).execute();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/* END code from */
	}
	
	/**
	 * 
	 * @param start
	 * @param end
	 * @return the specific url for the travel
	 */
	private static String urlTravel(Localization start, Localization end) {
		String apiKey = "2f30588d-1eeb-409b-a822-b256accb329f";
		String url = "https://graphhopper.com/api/1/route?";
		url += "point=" + start.getLatitude() + "," + start.getLongitude();
		url += "&point=" + end.getLatitude() + "," + end.getLongitude();
		url += "&vehicle=car"+"&locale=en"+"&calc_points=false";
		url += "&key=" + apiKey;
		return url;
	}
	
	public static void main(String[] args){
		/*
		 * Test of trajectPlan
		 */
		Localization carnot = new Localization("UJM Carnot", 45.45218, 4.38656);
		Localization metare = new Localization("UJM Metare", 45.42331, 4.42541);
		System.out.println(urlTravel(carnot, metare));
	}

}
