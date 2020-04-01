package com.assignment.bookstore.beans.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthorDTO {

    private @JsonProperty("name") String authorName;
    private @EqualsAndHashCode.Exclude String description;
}
