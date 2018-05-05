/**
 * 
 */
package com.sapient.hackathon.utility;

import com.sapient.hackathon.vo.DialogFlowResponse;

/**
 * @author mkum17
 */
public class EngineHelper {

	public static String getDialogFlowJsonResponse(DialogFlowResponse dialogFlowResponse) {
		return dialogFlowResponse.toJson();
	}

	public static String getDialogFlowJsonResponse(String response) {
		return getDialogFlowJsonResponse(response, response);
	}
	
	public static String getDialogFlowJsonResponse(String speech, String displayText) {
//		Gson gson = new Gson();
//		return gson.toJson(new DialogFlowResponse(speech, displayText));
		return null;
	}
}
