package com.chadicloud.ai.llmspring.service;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RagService {

    private final VectorStore vectorStore;

    public RagService(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    public void textEmbedding(List<Resource> pdfs) {

        PdfDocumentReaderConfig pdfDocumentReaderConfig = PdfDocumentReaderConfig.defaultConfig();
       // StringBuilder content = new StringBuilder();
        var tokenTextSplitter = new TokenTextSplitter();
        for (Resource pdf : pdfs) {
            PagePdfDocumentReader pdfDocumentReader = new PagePdfDocumentReader(pdf, pdfDocumentReaderConfig);
            List<Document> documentList = pdfDocumentReader.get();
            //content.append(documentList.stream().map(Document::getContent).collect(Collectors.joining("\n")));
            List<Document> splitDocuments = new TokenTextSplitter().apply(documentList);
            vectorStore.add(splitDocuments);
           // this.vectorStore.accept(tokenTextSplitter.apply(pdfReader.get()));
        }
    }
}
