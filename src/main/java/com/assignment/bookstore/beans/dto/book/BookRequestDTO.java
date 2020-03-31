package com.assignment.bookstore.beans.dto.book;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class BookRequestDTO extends BookDTO implements Serializable {

	@NotNull @Min(value = 1,message = "invalid authorId")
	private Long authorId;
}
