package com.assignment.bookstore.beans.dto.book;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

import static com.assignment.bookstore.util.MessageConstants.ErrorMessage.*;

@Data
@NoArgsConstructor
public class BookRequestDTO extends BookDTO implements Serializable {

	public BookRequestDTO(BookDTO bookDTO){
		super(bookDTO.getTitle(),bookDTO.getIsbn(),bookDTO.getPrice());
	}

	@NotNull(message = AUTHOR_ID_MISSING) @Min(value = 1, message = INVALID_AUTHOR_ID)
	private Long authorId;

	@NotNull(message = STOCK_COUNT_MISSING) @Min(value = 1, message = INVALID_STOCK)
	private Integer count;
}
