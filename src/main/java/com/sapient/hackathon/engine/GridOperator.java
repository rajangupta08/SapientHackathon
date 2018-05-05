/**
 * 
 */
package com.sapient.hackathon.engine;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.logging.Logger;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.annotation.WebServlet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import ai.api.model.Fulfillment;
import ai.api.web.AIWebhookServlet;

/**
 * Performs Grid operations.
 *
 * @author mkum17
 *
 */
@WebServlet(name = "GridOperator", urlPatterns = { "/operator" })
public class GridOperator extends AIWebhookServlet {

	private static final Logger log = Logger.getLogger(GridOperator.class.getName());

	private static final long serialVersionUID = 60455718070851894L;

	private static final String BASE_URL = "https://transmission.cfapps.io";
	private static final String ACTION_RPA_URL = "https://transmission.cfapps.io/action/rpa";

	@Override
	protected void doWebhook(AIWebhookRequest request, Fulfillment response) {
		log.info("Webhook invoked with session:" + request.getSessionId());
		String responseStr = "No matching action found for session " + request.getSessionId() + ". Please try again!";
		String action = request.getResult().getAction();
		log.finer("Action: " + action);
		if (null != action) {
			responseStr = " Action received = " + action;
			if (action.equals(DialogFlowAction.GET_POOL_PRICE.toString())) {
				log.info("GET_POOL_PRICE");
//				responseStr = "Current Pool Price is " + Double.toString(getCurrentPoolPrice());
				responseStr = getLastRecordedPoolPrice();
			} else if (action.equals("GET_LINE_CAPACITY")) {
				log.info("GET_LINE_CAPACITY");
				String lineName = request.getResult().getStringParameter("lineName");
				log.fine("lineName:" + lineName);
				responseStr = getLineCapacity(lineName);
			} else if (action.equals("GET_LINE_GENERATION")) {
				log.info("GET_LINE_GENERATION");
				String lineName = request.getResult().getStringParameter("lineName");
				log.fine("lineName:" + lineName);
				responseStr = getLineGeneration(lineName);
			}  else if (action.equals("CHECK_FLOW_ON_LINE")) {
				responseStr = getStatusofAllLines();
			}  else if (action.equals("APPLY_CONSTRAINT")) {
				responseStr = applyConstraint();
			} else if (action.equals("ACCEPT_DISPATCH")) {
				responseStr = acceptDispatch();
			} else if (action.equals("SEND_DISPATCH")) {
				responseStr = dispatchRecommendations();
			}
		} else {
			responseStr = "null action received for session " + request.getSessionId();
		}
		response.setDisplayText(responseStr);
		response.setSpeech(responseStr);
		log.info("Webhook session completed: " + request.getSessionId());
	}

	public String dispatchRecommendations() {
		fetchGetRequestHTTPS(ACTION_RPA_URL + "/fire/SEND_DISPATCH");
		return "Alright, dispatching the recommendations";
	}
	
	public String acceptDispatch() {
		fetchGetRequestHTTPS(ACTION_RPA_URL + "/fire/ACCEPT_DISPATCH");
		return "Ok, acknowledging dispatches on adams";
	}

	public String applyConstraint() {
		fetchGetRequestHTTPS(ACTION_RPA_URL + "/fire/APPLY_CONSTRAINT");
		return "Ok, applying constraint";
	}

	public String getStatusofAllLines() {
		Response response = fetchGetRequestHTTPS(BASE_URL + "/action/cheklines");
		if(HttpsURLConnection.HTTP_OK == response.getStatusCode()) {
			return response.getValue();
		}
		else {
			return "Unable to determine status of all lines";
		}
	}

	private Double getCurrentPoolPriceFake() {
		LocalDateTime localDateTime = LocalDateTime.now();
		String poolPriceStr = localDateTime.getMinute() + "." + localDateTime.getSecond();
		log.exiting("GridOperator", "getCurrentPoolPrice", poolPriceStr);
		return Double.parseDouble(poolPriceStr);
	}
	
	public String getLastRecordedPoolPrice() {
		Response response = fetchGetRequestHTTPS(BASE_URL + "/rpa/fetchPoolprice");
		String responeString;
		log.info("Response from Cloud:: " + response);
		if (HttpsURLConnection.HTTP_OK == response.getStatusCode()) {
			Double price = 0d;
			JSONObject jsonObj = new JSONObject(response.getValue());
			log.info("jsonObj:: " + jsonObj);
			if (!jsonObj.isNull("currentpoolPrice")) {
				price = jsonObj.getDouble("currentpoolPrice");
				log.info("price ::" + price);
				return "Current System Price is " + String.valueOf(price) + "$!";
			} else {
				log.info("Current System Price is 28.72$!");
				return "Current System Price is 28.72$!";
			}

		} else {
			return "Unable to determine current System Price!";
		}

		/*
		 * Double price = 1000d; return "Current Pool Price is " +
		 * String.valueOf(price) + "$!";
		 */
	}

	public String getCurrentPoolPriceFromRPA() {
//		Response response = sendPostRequestHTTPS(ACTION_RPA_URL + "/fire");
		Response response = fetchGetRequestHTTPS(ACTION_RPA_URL + "/fire/GET_POOL_PRICE");
		if(HttpsURLConnection.HTTP_OK == response.getStatusCode()) {
//			Double price = (new JSONObject(response.getValue())).getDouble("currentpoolPrice");
			return "Request sent successfully!";
		}
		else {
			return "Unable to determine current Pool Price!";
		}
	}

	public String getLineGeneration(String lineName) {
		Response response = fetchGetRequestHTTPS(BASE_URL + "/lines/" + lineName + "/generations");
		log.info("WS Response: " +response.toString());
		
		if(HttpsURLConnection.HTTP_OK == response.getStatusCode()) {
			StringBuilder generatorsData = new StringBuilder();
			JSONArray jArray = (JSONArray) new JSONTokener(response.getValue()).nextValue();
			generatorsData.append("There are " + jArray.length() + " generators connected. ");
			jArray.forEach(generator -> generatorsData.append(getGeneratorText2((JSONObject) generator)));
			return generatorsData.toString();
		}
		else {
			return "Unable to determine generation on line " + lineName + ". Status: " + response.getStatus();
		}
	}

	public String getLineCapacity(String lineName) {
		Response response = fetchGetRequestHTTPS("https://transmission.cfapps.io/lines/" + lineName);
		if(HttpsURLConnection.HTTP_OK == response.getStatusCode()) {
			return "Current capacity of line " + lineName + " is " + extractCapacity(response.getValue()) + "MW!";
		}
		return "Unable to determine capacity of line " + lineName + ". Status: " + response.getStatus();
	}


	private Double extractCapacity(String capacityResponse) {
		return new JSONObject(capacityResponse).getDouble("capacity");
	}

	/**
	 * Fetches data from supplied HTTPS URL and returns Response object with HTTP
	 * status code and message. Caller should verify the status code before
	 * processing value.
	 *
	 * @param urlStr HTTPS Url string
	 * @return
	 */
	private Response fetchGetRequestHTTPS(String urlStr) {
		log.info("fetchGetRequestHTTPS:" + urlStr);
		Response response = new Response("Failed to load data!");
		URL url;
		try {
			url = new URL(urlStr);
			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("GET");
			conn.connect();

			int respCode = conn.getResponseCode();
			if(respCode == HttpsURLConnection.HTTP_OK) {
				StringBuilder stringBuilder = new StringBuilder();
				String line = "";
				BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
				while ((line = br.readLine()) != null) {
					stringBuilder.append(line);
				}
				br.close();
				response.setValue(stringBuilder.toString());
			}
			else {
				response.setStatusCode(conn.getResponseCode());
				response.setStatus(conn.getResponseMessage());
			}
			conn.disconnect();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return response;
	}

	private Response sendPostRequestHTTPS(String urlStr) {
		log.info("sendPostRequestHTTPS: " + urlStr);
		Response response = new Response("Failed to load data!");
		URL url;
		try {
			url = new URL(urlStr);
			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
//			conn.connect();

			JSONObject requestParameter = new JSONObject();
			requestParameter.put("action", "GET_POOL_PRICE");

			DataOutputStream writer = new DataOutputStream(conn.getOutputStream());
			writer.writeBytes(requestParameter.toString());

//			OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
//			writer.write(URLEncoder.encode(requestParameter.toString(), "UTF-8"));
//			writer.write(requestParameter.toString());
			writer.flush();
			writer.close();

			conn.connect();
			int respCode = conn.getResponseCode();
			if(respCode == HttpsURLConnection.HTTP_OK) {
				StringBuilder stringBuilder = new StringBuilder();
				String line = "";
				BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
				while ((line = br.readLine()) != null) {
					stringBuilder.append(line);
				}
				br.close();
				response.setValue(stringBuilder.toString());
			}
			else {
				response.setStatusCode(conn.getResponseCode());
				response.setStatus(conn.getResponseMessage());
			}
			conn.disconnect();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return response;
	}

	private String getGeneratorText2(JSONObject jsonGeneratorObject) {
		return "Generator " + jsonGeneratorObject.getString("name") + " is at "
				+ jsonGeneratorObject.getDouble("generationLevel") + " MW. ";
	}

}
