package com.assignment.bookstore.beans.dto;

import lombok.*;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthorDTO {

    @NotEmpty(message = "Author name is mandatory")
    private String authorName;

    private @EqualsAndHashCode.Exclude String description;
}
