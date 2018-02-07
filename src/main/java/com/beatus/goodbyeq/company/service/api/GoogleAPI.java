package com.beatus.goodbyeq.company.service.api;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.beatus.goodbyeq.company.exception.ResponseEntityException;
import com.beatus.goodbyeq.company.model.SearchResultsDTO;
import com.beatus.goodbyeq.users.utils.UsersConstants;

/**
 * Contains the Google API calls to fetch near by stores for the given input area or coordinates
 * @author Pavan Kumar Poduri
 */
public class GoogleAPI {
	final static Logger logger = LoggerFactory.getLogger(GoogleAPI.class);
	static String CLASS_NAME = "GoogleAPIUtility";

	@SuppressWarnings("unused")
	public static void main(String args[]) throws ResponseEntityException, IOException, JSONException{
		//Double[] searchCoordinates = getCoordinates("Chroma+Kondapur");
		//ArrayList<SearchResultsBean> searchResultsByCoordinates = findNearestStores(searchCoordinates[1], searchCoordinates[0]);
		ArrayList<SearchResultsDTO> searchResultsByCoordinates = getCoordinates("Chroma+Kondapur"); 		 
	}	
	
	/**
	 * Searches the near by stores for the given latitude and longitude
	 * @param lat - latitude
	 * @param lng - longitude
	 * @return - List of near by stores
	 * @throws ResponseEntityException
	 * @throws IOException
	 * @throws JSONException
	 */
	public static ArrayList<SearchResultsDTO> findNearestStores(double lat, double lng) throws ResponseEntityException, IOException, JSONException{
		String METHOD_NAME = "findNearestStores()";
	    ArrayList<SearchResultsDTO> resultList = null;
	    StringBuilder jsonResults = new StringBuilder();
	    
        StringBuilder sb = new StringBuilder(UsersConstants.PLACES_API_BASE);
        sb.append(UsersConstants.TYPE_NEAR_BY_SEARCH);
        sb.append(UsersConstants.OUT_JSON);
        sb.append("?location=" + String.valueOf(lat) + "," + String.valueOf(lng));
        sb.append("&radius=" + String.valueOf(UsersConstants.RADIUS));
        sb.append("&key=" + UsersConstants.API_KEY);
        sb.append("&type=grocery_or_supermarket");
        logger.debug(CLASS_NAME + "::" + METHOD_NAME + "::URL:-\n"+String.valueOf(sb));
        
        jsonResults = fetchJSONResults(sb);
        // Create a JSON object hierarchy from the results
        JSONObject jsonObj = new JSONObject(jsonResults.toString());
        JSONArray predsJsonArray = jsonObj.getJSONArray("results");
        
        // Extract the Place descriptions from the results
        resultList = new ArrayList<SearchResultsDTO>(predsJsonArray.length());
        logger.debug(CLASS_NAME + "::" + METHOD_NAME + "::Results:-\n");
        for (int i = 0; i < predsJsonArray.length(); i++) {
            SearchResultsDTO place = new SearchResultsDTO();
            place.vicinity = predsJsonArray.getJSONObject(i).getString("vicinity");
            //place.formatted_address = getFormattedAddress(place.vicinity); 
            place.name = predsJsonArray.getJSONObject(i).getString("name");
            JSONObject locationObj = predsJsonArray.getJSONObject(i).getJSONObject("geometry").getJSONObject("location");
            place.longitude = locationObj.getDouble("lng");
            place.latitude = locationObj.getDouble("lat");
            try{
            	place.rating = String.valueOf(predsJsonArray.getJSONObject(i).getInt("rating"));	
            }
            catch(JSONException jsonException){
            	place.rating = "No";
            }
            logger.debug(place.name + ", " + place.vicinity + " with " + place.rating + " rating.");
            resultList.add(place);
        }
        if(resultList.size() == 0){
        	System.err.println("No Search Results found!\n");
        }
	 
	    return resultList;
	}
	
	/**
	 * Fetches the formatted address for the given address returned from Google API
	 * @param vicinity - Complete address
	 * @return - Formatted address
	 */
	private static String getFormattedAddress(String vicinity) {
		String[] vicinityArray = vicinity.split(",");
		int vicinityArraylength = vicinityArray.length;
		return vicinityArray[vicinityArraylength-2] + "," + vicinityArray[vicinityArraylength-1]; 
	}

	/**
	 * Fetches the results from Google API for the given URL
	 * @param sb - StringBuffer object of API URL
	 * @return - JSON response for the given input
	 * @throws IOException
	 */
	private static StringBuilder fetchJSONResults(StringBuilder urlStringBufferObj) throws IOException {
		StringBuilder jsonResults =  new StringBuilder();
		HttpURLConnection conn = null;
		URL url = new URL(urlStringBufferObj.toString());
	     conn = (HttpURLConnection) url.openConnection();
	     InputStreamReader in = new InputStreamReader(conn.getInputStream());
	
	     int read;
	     char[] buff = new char[1024];
	     while ((read = in.read(buff)) != -1) {
	         jsonResults.append(buff, 0, read);
	     }
	     
	     if (conn != null) {
	         conn.disconnect();
	     }
	     
		return jsonResults;
	}
	
	/**
	 * Fetches the stores details for the given input location through Google API
	 * @param inputLocation - input area details
	 * @return - List of near by stores
	 * @throws ResponseEntityException
	 * @throws IOException
	 * @throws JSONException
	 */
	public static ArrayList<SearchResultsDTO> getCoordinates(String inputLocation) throws ResponseEntityException ,IOException, JSONException {
		String METHOD_NAME = "getCoordinates()";
		ArrayList<SearchResultsDTO> searchResultsList = null;
		Double latitude = 0.0;
		Double longitude = 0.0;
		StringBuilder jsonResults = new StringBuilder();
        StringBuilder sb = new StringBuilder(UsersConstants.PLACES_API_BASE);
        sb.append(UsersConstants.TYPE_TEXT_SEARCH);
        sb.append(UsersConstants.OUT_JSON);
        sb.append("?query=" + inputLocation);
        sb.append("&key=" + UsersConstants.API_KEY);
        
        logger.debug(CLASS_NAME + "::" + METHOD_NAME + "::" +"getCoordinates()::Google API URL:- " + sb.toString());
        jsonResults = fetchJSONResults(sb);
    
        // Create a JSON object hierarchy from the results
        JSONObject jsonObj = new JSONObject(jsonResults.toString());
        JSONArray predsJsonArray = jsonObj.getJSONArray("results");

        // Extract the Place descriptions from the results
        for (int i = 0; i < predsJsonArray.length(); i++) {
            JSONObject locationObj = predsJsonArray.getJSONObject(i).getJSONObject("geometry").getJSONObject("location");
            longitude = locationObj.getDouble("lng");
            latitude = locationObj.getDouble("lat");
            logger.debug(CLASS_NAME + "::" + METHOD_NAME + "::" +"Coordinates of " + inputLocation + " is " + latitude + "," + longitude);
        }
    
        searchResultsList = findNearestStores(latitude, longitude);
        return searchResultsList;
	}
}