package pt.pauloortolan.qraitoolbox.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pt.pauloortolan.qraitoolbox.integration.apod.APODClient;
import pt.pauloortolan.qraitoolbox.pojo.ApodResponse;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
@RequiredArgsConstructor
public class APODTools {

    private final APODClient client;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Value("${integrations.nasa.api.api-key}")
    private String apiKey;

    @Tool(description = "Fetches NASA's Astronomy Picture of the Day for today")
    public ApodResponse getTodayApod() {
        log.info("APODController::getTodayApod()");
        return client.getPlanetaryAPODToday(true, apiKey);
    }

    @Tool(description = """
            Fetches NASA's Astronomy Picture of the Day for a specific date.
            The date must be in YYYY-MM-DD format and no earlier than 1995-06-16.
            """)
    public ApodResponse getApodByDate(
            @ToolParam(description = "Date in YYYY-MM-DD format, e.g. 2023-07-04") String date) {
        log.info("APODController::getApodByDate(date = {})", date);
        return client.getPlanetaryAPODByDate(date, false, date);
    }

    @Tool(description = """
            Fetches NASA's Astronomy Picture of the Day for exactly one year ago from today.
            Useful for 'on this day last year' or anniversary comparisons.
            The APOD API does not provide future dates, so this looks back one year instead.
            """)
    public ApodResponse getApodOneYearAgo() {
        log.info("APODController::getApodOneYearAgo()");
        String oneYearAgo = LocalDate.now().minusYears(1).format(FORMATTER);
        return client.getPlanetaryAPODByDate(oneYearAgo, false, oneYearAgo);
    }
}
