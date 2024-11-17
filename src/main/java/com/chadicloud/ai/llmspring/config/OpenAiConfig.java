package com.chadicloud.ai.llmspring.config;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.image.ImageModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiImageModel;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.openai.api.OpenAiImageApi;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAiConfig {

    @Bean
    ChatModel chatModel(@Value("${spring.ai.openai.api-key}") String apiKey) {
        return new OpenAiChatModel(new OpenAiApi(apiKey));
    }
    @Bean
    ImageModel imageModel(@Value("${spring.ai.openai.api-key}") String apiKey) {
        return new OpenAiImageModel(new OpenAiImageApi(apiKey));
    }

    @Bean
    VectorStore simleVectorStore(EmbeddingModel embeddingModel) {
        return new SimpleVectorStore(embeddingModel);
    }

    //	@Bean
//	public ChatClient chatClient(OpenAiClient openAiClient) {
//		return new OpenAiChatClient(openAiClient);
//	}
}
