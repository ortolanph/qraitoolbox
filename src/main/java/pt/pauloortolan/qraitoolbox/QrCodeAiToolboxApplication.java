package pt.pauloortolan.qraitoolbox;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class QrCodeAiToolboxApplication {

    static void main(String[] args) {
        SpringApplication.run(QrCodeAiToolboxApplication.class, args);
    }

}
