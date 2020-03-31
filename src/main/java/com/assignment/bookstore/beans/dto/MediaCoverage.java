package com.assignment.bookstore.beans.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class MediaCoverage {
	
	@JsonIgnore
	private String userId;
	
	@JsonIgnore
	private String id;
	
	private String title;
	
	private String body;
}
