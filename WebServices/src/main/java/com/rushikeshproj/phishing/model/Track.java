package com.rushikeshproj.phishing.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Track implements Serializable{

	@JsonProperty("title")
	String title;
	@JsonProperty("singer")
	String singer;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@JsonProperty
	public String getSinger() {
		return singer;
	}

	@JsonProperty
	public void setSinger(String singer) {
		this.singer = singer;
	}

	@Override
	public String toString() {
		return "Track [title=" + title + ", singer=" + singer + "]";
	}

}
