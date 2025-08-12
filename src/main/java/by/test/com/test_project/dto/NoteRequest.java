package by.test.com.test_project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NoteRequest {
    private String agency;
    private String clientGuid;
    private String dateFrom;
    private String dateTo;
}
