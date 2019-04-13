package com.example.aws.lambda.apiproxy;

import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

public class LambdaFunctionHandler
		implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

	public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent event, Context context) {

		// Using the standard AWS lambda logger
		LambdaLogger logger = context.getLogger();

		// Declare and instance the response
		APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();

		// Declare and instance the JSON parser
		JSONParser parser = new JSONParser();

		// Request
		String message = null;
		String bodyStr = event.getBody();

		// Response
		Map<String, String> responseBody = new HashMap<String, String>();
		String responseBodyString = null;

		// Just for diagnosis
		logger.log("Received event body: " + bodyStr);

		// Input must be JSON and must contain Keys.RQST_MESSAGE field
		if (bodyStr != null) {
			JSONObject body;

			try {
				body = (JSONObject) parser.parse(bodyStr);
				if (body.get(Keys.RQST_MESSAGE) != null) {
					message = (String) body.get(Keys.RQST_MESSAGE);
					response.setStatusCode(200);
					responseBody.put(Keys.RESP_MESSAGE, "Received message: '" + message + "'");
				}
				else
				{
					response.setStatusCode(400);
					responseBody.put(Keys.RESP_ERROR,"Body missing field: "+Keys.RQST_MESSAGE);
				}
				
			} catch (ParseException e) {
				logger.log("ERROR parsing JSON: " + e.toString());
				response.setStatusCode(400);
				responseBody.put(Keys.RESP_ERROR,"ERROR parsing JSON: " + e.toString());
			}
		}

		response.setHeaders(Collections.singletonMap("x-time-received", Calendar.getInstance().toString()));
		responseBodyString = JSONObject.toJSONString(responseBody);
		response.setBody(responseBodyString);

		return response;
	}

}