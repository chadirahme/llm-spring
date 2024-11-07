package com.chadicloud.ai.llmspring.controller;
import com.chadicloud.ai.llmspring.request.ChatGenRequest;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.image.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GenAIChatController {

    private final ChatModel chatModel;
    private final ImageModel imageModel;

    public GenAIChatController(ChatModel chatModel, ImageModel imageModel) {
        this.chatModel = chatModel;
        this.imageModel = imageModel;
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        System.out.println("health - STARTED!!! ");
        return ResponseEntity.ok("UP!!");
    }
    @GetMapping("/chatgen")
    public String chatModelGen(@RequestBody ChatGenRequest chatGenRequest) {

        System.out.println("ChatModel - STARTED!!! - "+chatGenRequest.prompt());
        String response = chatModel.call(chatGenRequest.prompt());
        System.out.println("ChatModel - DONE!!! "+response);

        return response;
    }

    @GetMapping("/imagegen")
    public String imageGen(@RequestBody ChatGenRequest imageGenRequest) {

        ImageOptions options = ImageOptionsBuilder.builder()
                .withModel("dall-e-3")
                .withHeight(1024)
                .withWidth(1024)
                .build();
        ImagePrompt imagePrompt = new ImagePrompt(imageGenRequest.prompt(), options);

        System.out.println("ImageModel - STARTED!!! - " + imageGenRequest.prompt());
        ImageResponse response = imageModel.call(imagePrompt);
        System.out.println("ImageModel - DONE!!! " + response);

        return "redirect:" + response.getResult().getOutput().getUrl();
    }
}
