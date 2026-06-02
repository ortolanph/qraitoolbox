package pt.pauloortolan.qraitoolbox.integration.goqr;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "goqr-client", url = "${integrations.goqr.api.url}")
public interface GoQRClient {

    @GetMapping("/create-qr-code")
    byte[] createQrCode(@RequestParam String data,
                        @RequestParam String size,
                        @RequestParam(name = "charset-source") String charsetSource,
                        @RequestParam String ecc,
                        @RequestParam String format,
                        @RequestParam int margin);
}
