package com.example.aws.lambda.apiproxy;

import java.io.IOException;

import org.json.simple.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.example.aws.lambda.apiproxy.Keys;
import com.example.aws.lambda.apiproxy.LambdaFunctionHandler;

@RunWith(MockitoJUnitRunner.class)
public class LambdaFunctionHandlerTest {

	

	@Before
	public void setUp() throws IOException {
	}

	private Context createContext() {
		TestContext ctx = new TestContext();

		ctx.setFunctionName("LambdaFunction");
		
		return ctx;
	}

	@Test
	public void testLambdaFunctionHandlerSuccess() {
		LambdaFunctionHandler handler = new LambdaFunctionHandler();
		Context ctx = createContext();
		APIGatewayProxyRequestEvent rqst = new APIGatewayProxyRequestEvent();
		JSONObject rqstJsonObj = new JSONObject();

		rqstJsonObj.put(Keys.RQST_MESSAGE, "BLAH BLAH");
		rqst.setBody(rqstJsonObj.toJSONString());

		APIGatewayProxyResponseEvent output = handler.handleRequest(rqst, ctx);
		System.out.println("Return code: " + output.getStatusCode());
		System.out.println("Response body: " + output.getBody());
		
		Assert.assertEquals("Success return code", new Integer(200), output.getStatusCode());
	}

	@Test
	public void testLambdaFunctionHandlerBadJson() {
		LambdaFunctionHandler handler = new LambdaFunctionHandler();
		Context ctx = createContext();
		APIGatewayProxyRequestEvent rqst = new APIGatewayProxyRequestEvent();

		rqst.setBody("NOTJSON");

		APIGatewayProxyResponseEvent output = handler.handleRequest(rqst, ctx);
		System.out.println("Return code: " + output.getStatusCode());
		System.out.println("Response body: " + output.getBody());

		Assert.assertEquals("Success return code", new Integer(400),output.getStatusCode());
	}

}
