package com.assignment.bookstore.beans.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MediaCoverage {
	
	@JsonIgnore
	private String userId;
	
	@JsonIgnore
	private String id;
	
	private String title;
	
	private String body;
}
