package com.assignment.bookstore.beans.dto.book;

import com.assignment.bookstore.beans.dto.AuthorDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

import static com.assignment.bookstore.util.MessageConstants.ErrorMessage.INVALID_STOCK;
import static com.assignment.bookstore.util.MessageConstants.ErrorMessage.STOCK_COUNT_MISSING;

@Data
@NoArgsConstructor
public class BookRequestDTO extends BookDTO implements Serializable {

	public BookRequestDTO(BookDTO bookDTO){
		super(bookDTO.getTitle(),bookDTO.getIsbn(),bookDTO.getPrice());
	}

	@NotNull(message = "Author details is missing")
	@Valid
	@JsonProperty("author")
	private AuthorDTO authorDTO;

	@NotNull(message = STOCK_COUNT_MISSING) @Min(value = 1, message = INVALID_STOCK)
	private Integer count;
}
