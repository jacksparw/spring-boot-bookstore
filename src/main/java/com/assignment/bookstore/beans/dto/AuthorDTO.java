package com.assignment.bookstore.beans.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthorDTO {

    private @JsonProperty("name") String authorName;
    private @EqualsAndHashCode.Exclude String description;
}
