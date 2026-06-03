package pt.pauloortolan.qraitoolbox.integration.apod;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pt.pauloortolan.qraitoolbox.pojo.ApodResponse;

@FeignClient(name = "apod-client", url = "${integrations.nasa.api.url}")
public interface APODClient {

    @GetMapping("/planetary/apod")
    ApodResponse getPlanetaryAPODToday(@RequestParam(name = "thumbs") boolean thumbs,
                                       @RequestParam(name = "api_key") String apiKey);

    @GetMapping("/planetary/apod")
    ApodResponse getPlanetaryAPODByDate(@RequestParam(name = "date") String date,
                                        @RequestParam(name = "thumbs") boolean thumbs,
                                        @RequestParam(name = "api_key") String apiKey);

}
