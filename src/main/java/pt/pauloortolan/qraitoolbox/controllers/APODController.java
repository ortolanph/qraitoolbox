package pt.pauloortolan.qraitoolbox.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pt.pauloortolan.qraitoolbox.pojo.ApodResponse;
import pt.pauloortolan.qraitoolbox.services.APODService;

@Slf4j
@RestController
@RequestMapping("/apod")
@RequiredArgsConstructor
public class APODController {

    private final APODService service;

    @GetMapping
    public ApodResponse getAPODPicture(@RequestParam String prompt) {
        log.info("APODController::getAPODPicture(prompt = {})", prompt);
        return service.getAPODPicture(prompt);
    }
}
