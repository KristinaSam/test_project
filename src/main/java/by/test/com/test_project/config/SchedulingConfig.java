package by.test.com.test_project.config;

import by.test.com.test_project.service.DataImportService;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class SchedulingConfig {
    private final DataImportService dataImportService;

    public SchedulingConfig(DataImportService dataImportService) {
        this.dataImportService = dataImportService;
    }

    @Scheduled(cron = "0 15 */2 * * ?")
    public void runImport() {
        dataImportService.importNotes();
    }
}
