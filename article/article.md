# Tools, Tools, Tools

Imagine that you have a toolbox. You need to assemble a furniture, and no tool has been provided together. What do you
do? Certainly you get your toolbox. In your toolbox you have:

* Screwdrivers
* Hammer
* Wrenches
* Handsaw
* Spirit Level
* Needle nose pliers
* Lineman's pliers
* Tape measure
* etc

You are a Object-Oriented programmer and to get one tool from the Toolbox, you think:

```java
Tool tool = ToolBox.getInstance().getTool("screewdriver-5mm");
```

There's a ToolBox class (probably a Singleton), in which contains a map of Tool indexed by its name. It will return an
implementation of the Tool interface which is the tool that I want to get.

And it's good when you are learning Object-Oriented Programming. In the advent of AI, people are thinking of using
natural language:

```
English: Give me a 5 mm screwdriver
Portuguese: Me dê uma chave de fenda de 5 mm
Spanish: Dame un destornillador de 5 mm
```

And with that you can get a 5mm Screwdriver anywhere where is spoken in English, Portuguese, and Spanish.

In this article I will show the development of two AI Toolbox with SpringBoot AI, a QR Code Toolbox and a NASA
Astronomic Picture of the Day (APOD) Toolbox.

---

## The QR Code tool box

Let's begin with a QR Code Tool box. With a QR Code it's possible to:

* Encode a text message
* Encode a URL
* Encode a WiFi network credentials
* Activate WhatsApp to send a message to a certain number
* Create a code for start a phone call
* Pre-fill a SMS QR Code
* Encode a PDF

For this article, I created the first six tools.

In Spring Boot AI, you create a service class and each method is a tool annotated with the @Tool annotation. Each
parameter of a tool method must be annotated with @ToolParam. A simple example is:

```java

@Tool(description = "Generates a QR Code for a given text content")
public byte[] generateTextQRCode(
        @ToolParam(description = "The text to encode into QRCode") String text) {
    log.info("QRTools::generateTextQRCode(text = {})", text);

    log.info("Calling the integration with QRCode Generator - Simple Text");
    return client.createQrCode(text, size, charsetSource, ecc, format, margin);
}
```

This tool gets an input text, calls the QR Code integration and returns the QR Code for the given text. But the service
alone does not make magic, it's needed to link with the ChatClient:

```java
    private final ChatClient.Builder chatClientBuilder;

private final QRTools qrToolbox;

@Bean
public ChatClient qrChatClient() {
    return chatClientBuilder
            .defaultTools(qrToolbox)
            .defaultSystem("""
                    You are a QR code assistant. When the user asks to generate a QR code,
                    call the appropriate tool and return ONLY the raw base64 string from the tool result.
                    No explanation, no markdown, no extra text. Just the base64 string.
                    """)
            .defaultAdvisors(new SimpleLoggerAdvisor())
            .build();
}
```

The QRTools class is my service class that will be linked with the QR Code chatClient. The defaultSystem method is a way
to avoid repeating the text in runtime code. This way, everytime that this chatClient is called, the AI will include
this prompt.

Now we link the chat client with the QR Service:

```java

@Slf4j
@Service
@RequiredArgsConstructor
public class QRService {

    private final ChatClient qrChatClient;

    public byte[] generateQRCode(String prompt) {
        log.info("QRService::generateQRCode(prompt = {})", prompt);
        String base64 = qrChatClient
                .prompt()
                .user(prompt)
                .call()
                .content();

        assert base64 != null;
        return Base64.getDecoder().decode(base64.trim());
    }

}
```

This is the whole class (removing the package instruction and the imports). Note that I am injecting the qrChatClient
configured on the configuration class. Note that it's a single entry point to the QR integration.

The integration is a simple, but powerful, [QR Code generate](https://goqr.me/). It offers a single entry where you put
your data and generate the QR Code.

The rest controller is pretty simple too. It gets the prompt from the REST API call, calls the service, on which calls
the AI integration that will use the tools according to the descriptions.

An example of call is:

```http request
GET http://localhost:9020/qr?prompt=%22If%20you%20scanned%20this%20code,%20you%20are%20smart!%22 
```

It will generate the following image:

![test_qr_code.png](test_qr_code.png)

The table below shows how to generate a QR Code for each tool:

| Prompt                                         | Tool called               |
|------------------------------------------------|---------------------------|
| Generate a QR code for Hello World             | `generateTextQrCode`      |
| QR code for https://google.com                 | `generateLinkQrCode`      |
| QR for Wi-Fi SSID=MyNet password=1234 type=WPA | `generateWifiQrCode`      |
| WhatsApp QR for ++351912345678                 | `generateWhatsAppQrCode`  |
| Phone call QR for +351912345678                | `generatePhoneCallQrCode` |
| SMS QR to +351912345678 saying Hello           | `generateSmsQrCode`       |

## Astronomic Picture of the Day (APOD)

NASA has a lot of open APIs. You can use the site [NASA Open APIs](https://api.nasa.gov/) to browse them. There are some
instructions for creating an API Key and the limits of usage.

For this tool, I'll use the APOD API on which provides daily astronomic pictures. The client configuration is very
different from the QR Code. Let's see the code:

```java

@Bean
public ChatClient apodChatClient() {
    return chatClientBuilder
            .defaultTools(apodTools)
            .defaultSystem("""
                    You are an astronomy assistant with access to NASA's Astronomy Picture of the Day (APOD).
                    When asked about a picture, fetch it and describe:
                    - The title and date
                    - What is shown in the image and its astronomical significance
                    - Whether it's an image or video
                    - The image URL so the user can view it
                    - Copyright information if present
                    Always be enthusiastic and educational about space!
                    """)
            .build();
}
```

It was needed to create a better defaultSystem prompt on which I describe with details what I want. The service and the
rest controllers are simple classes, just getting a prompt, written in natural language to get the information. An
example request is:

```http request
GET http://localhost:9020/apod?prompt="What is NASA's picture today?"
```

And a response is:

```json
{
  "date": "2026-06-09",
  "title": "Thor's Helmet",
  "url": "https://apod.nasa.gov/apod/image/2606/Thor_Drudis_960.jpg",
  "hdUrl": "null",
  "media_type": "image",
  "copyright": "Josep Drudis,\nChristian Sasse",
  "service_version": "v1"
}
```

The available prompts for that services are:

| Prompt                                       | Tool invoked        |
|----------------------------------------------|---------------------|
| What is NASA's picture today?                | `getTodayApod`      |
| Show me the APOD from July 4th 2020          | `getApodByDate`     |
| What was the astronomy picture one year ago? | `getApodOneYearAgo` |

## Conclusions

The tool annotation is a powerful resource for AI on which, with natural language (in this case I used only English),
links with internal services to retrieve all kinds of data. It can be used in a shop where the customers can select,
using a pre-defined prompt, products of a certain family or with certain characteristics. It will depend on the business
needs. 
