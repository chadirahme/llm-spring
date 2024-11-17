package com.chadicloud.ai.llmspring.controller;
import com.chadicloud.ai.llmspring.request.ChatGenRequest;
import com.chadicloud.ai.llmspring.service.RagService;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.Resource;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.image.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.ai.chat.messages.Message;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class GenAIChatController {

    private final ChatModel chatModel;
    private final ImageModel imageModel;
    private final VectorStore vectorStore;
    private final RagService ragService;
    //private final ChatClient chatClient;

    public GenAIChatController(ChatModel chatModel, ImageModel imageModel, VectorStore vectorStore, RagService ragService) {
        this.chatModel = chatModel;
        this.imageModel = imageModel;
       // this.chatClient = chatClient;
        this.vectorStore = vectorStore;
        this.ragService = ragService;
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        System.out.println("health - STARTED!!! ");
        return ResponseEntity.ok("UP!!");
    }
    @GetMapping("/chatgen")
    public String chatModelGen(@RequestBody ChatGenRequest chatGenRequest) {

        var template = """
                I am a tourist visiting the city of {city}.
                I am mostly interested in {interest}.
                Tell me tips on what to do there.""";
        PromptTemplate promptTemplate = new PromptTemplate(template);

        Map<String, Object> params = Map.of("city", chatGenRequest.city(), "interest", chatGenRequest.interest());
        Prompt prompt = promptTemplate.create(params);

        System.out.println("ChatModel - STARTED!!! - "+chatGenRequest.prompt());
       // String response = chatModel.call(chatGenRequest.prompt());
        String response = chatModel.call(prompt).getResult().getOutput().getContent();;

        //String response = chatClient.prompt(prompt).call().chatResponse().getResult().getOutput().getContent();
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


    @GetMapping("/pdf")
    public String pdf(@RequestBody ChatGenRequest imageGenRequest) throws IOException {
        Resource resource = new ClassPathResource("pdfs/Resume.pdf");
        ragService.textEmbedding(Collections.singletonList(resource));

//        StringBuilder content = new StringBuilder();
//        byte[] data = resource.getContentAsByteArray();
//        PdfDocumentReaderConfig pdfDocumentReaderConfig = PdfDocumentReaderConfig.defaultConfig();
//        PagePdfDocumentReader pdfDocumentReader = new PagePdfDocumentReader(resource, pdfDocumentReaderConfig);
//        List<Document> documentList = pdfDocumentReader.get();
//        content.append(documentList.stream().map(Document::getContent).collect(Collectors.joining("\n")));
//
//       // TokenTextSplitter tokenTextSplitter = new TokenTextSplitter();
//        List<Document> splitDocuments = new TokenTextSplitter().apply(documentList);
//        //List<Document> chunksDocs = chunks.stream().map(Document::new).toList();
//        vectorStore.add(splitDocuments);

        SearchRequest searchRequest = SearchRequest.query(imageGenRequest.prompt()).withTopK(3);
        List<Document> documentList1 = vectorStore.similaritySearch(searchRequest);


        String systemMessageTemplate = """
                Answer the following question based only in the provided CONTEXT
                If the answer is not found respond : "I don't know".
                CONTEXT :
                    {CONTEXT}
                """;


        Message systemMessage = new SystemPromptTemplate(systemMessageTemplate)
                .createMessage(Map.of("CONTEXT", documentList1));
        UserMessage userMessage = new UserMessage(imageGenRequest.prompt());
        Prompt prompt = new Prompt(List.of(systemMessage, userMessage));

        String response = chatModel.call(prompt).getResult().getOutput().getContent();

        return response;
    }

}
