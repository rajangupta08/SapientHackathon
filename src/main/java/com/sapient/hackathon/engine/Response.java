/**
 * 
 */
package com.sapient.hackathon.engine;

import javax.net.ssl.HttpsURLConnection;

/**
 * Custom class to record response of REST calls.
 *
 * @author mkumar
 *
 */
public class Response {

	private String value;
	private int statusCode = HttpsURLConnection.HTTP_OK;
	private String status;

	public Response() {
		super();
	}

	public Response(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public int getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Response [value=");
		builder.append(value);
		builder.append(", statusCode=");
		builder.append(statusCode);
		builder.append(", status=");
		builder.append(status);
		builder.append("]");
		return builder.toString();
	}
}
