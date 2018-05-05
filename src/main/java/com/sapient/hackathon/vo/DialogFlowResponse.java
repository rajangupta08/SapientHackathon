package com.sapient.hackathon.vo;

public class DialogFlowResponse {

	private String speech;
	private String displayText;

	public DialogFlowResponse() {
	}

	/**
	 * Constructs DialogFlowResponse with same speech and displayText properties.
	 *
	 * @param response
	 */
	public DialogFlowResponse(String response) {
		this.speech = response;
		this.displayText = response;
	}

	/**
	 * Constructs DialogFlowResponse with both properties specified.
	 *
	 * @param speech
	 * @param displayText
	 */
	public DialogFlowResponse(String speech, String displayText) {
		this.speech = speech;
		this.displayText = displayText;
	}

	public String getSpeech() {
		return speech;
	}

	public void setSpeech(String speech) {
		this.speech = speech;
	}

	public String getDisplayText() {
		return displayText;
	}

	public void setDisplayText(String displayText) {
		this.displayText = displayText;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DialogFlowResponse [speech=");
		builder.append(speech);
		builder.append(", displayText=");
		builder.append(displayText);
		builder.append("]");
		return builder.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((displayText == null) ? 0 : displayText.hashCode());
		result = prime * result + ((speech == null) ? 0 : speech.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DialogFlowResponse other = (DialogFlowResponse) obj;
		if (displayText == null) {
			if (other.displayText != null)
				return false;
		} else if (!displayText.equals(other.displayText))
			return false;
		if (speech == null) {
			if (other.speech != null)
				return false;
		} else if (!speech.equals(other.speech))
			return false;
		return true;
	}

	/**
	 * Converts DialogFlowResponse object to Json String
	 *
	 * @return Json String
	 */
	public String toJson() {
		return "toJson";
//		return (new Gson()).toJson(this);
	}

}
