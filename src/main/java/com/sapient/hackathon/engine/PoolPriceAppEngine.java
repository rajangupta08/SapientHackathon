package com.sapient.hackathon.engine;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.logging.Logger;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sapient.hackathon.utility.EngineHelper;

@WebServlet(name = "PoolPriceAppEngine", urlPatterns = { "/poolPrice" })
public class PoolPriceAppEngine extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(PoolPriceAppEngine.class.getName());
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

		log.info("GET request received!");
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");


		// JsonObject jsonObject = new
		// JsonObject(IOUtils.toString(request.getInputStream());
//		response.getWriter().print("{\"speech\": \"" + responseStr + "\", \"displayText\": \"" + responseStr + "\"}");

		String responseStr = "Current pool price is " + getCurrentPoolPrice();
		response.getWriter().print(EngineHelper.getDialogFlowJsonResponse(responseStr));
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		log.info("POST request received!");
		response.setContentType("application/json");
//		response.setCharacterEncoding("UTF-8");
		LocalDateTime localDateTime = LocalDateTime.now();
		String responseStr = "Current Pool Price is " + localDateTime.getMinute() + "." + localDateTime.getSecond();
		
		// JsonObject jsonObject = new
		response.getWriter().print("{\"speech\": \"" + responseStr + "\", \"displayText\": \"" + responseStr + "\"}");
		log.info("POST request completed with response: " + responseStr);
	}
	
	private Double getCurrentPoolPrice() {
		Double poolPrice = 0.0d;
		LocalDateTime localDateTime = LocalDateTime.now();
		String poolPriceStr = localDateTime.getMinute() + "." + localDateTime.getSecond();
		return Double.parseDouble(poolPriceStr);
	}
}