package com.assignment.bookstore.beans.dto;

import lombok.Data;

@Data
public class GenericResponseDTO {
	private String status;
	private String message;
	private Object data;
}
