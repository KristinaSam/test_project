package by.test.com.test_project.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ClientDTO(
        @JsonProperty("agency")
        String agency,
        @JsonProperty("guid")
        String guid,
        @JsonProperty("firstName")
        String firstName,
        @JsonProperty("lastName")
        String lastName,
        @JsonProperty("status")
        String status,
        @JsonProperty("dob")
        String dob,
        @JsonProperty("createDateTime")
        String createDateTime) {
}
