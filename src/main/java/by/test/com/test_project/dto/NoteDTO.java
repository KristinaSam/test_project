package by.test.com.test_project.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public record NoteDTO(
        @JsonProperty("comments")
        String comments,
        @JsonProperty("guid")
        String guid,
        @JsonProperty("clientGuid")
        String clientGuid,
        @JsonProperty("loggedUser")
        String loggedUser,
        @JsonProperty("createdDateTime")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime createdDateTime,
        @JsonProperty("modifiedDateTime")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime modifiedDateTime
) {
}
