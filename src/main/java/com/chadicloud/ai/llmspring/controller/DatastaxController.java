package com.chadicloud.ai.llmspring.controller;

import com.chadicloud.ai.llmspring.config.AstraConfig;
import com.chadicloud.ai.llmspring.service.RagService;
//import com.datastax.astra.client.Collection;
//import com.datastax.astra.client.DataAPIClient;
//import com.datastax.astra.client.Database;
//import com.datastax.astra.client.model.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("data")
public class DatastaxController {

    private static final Logger logger = LoggerFactory.getLogger(DatastaxController.class);

    @Autowired
    AstraConfig astraConfig;
    private final RagService ragService;
    private final EmbeddingModel embeddingModel;

    public DatastaxController(RagService ragService, EmbeddingModel embeddingModel) {
        this.ragService = ragService;
        this.embeddingModel = embeddingModel;
    }

//    @GetMapping("/health")
//    public ResponseEntity<String> health() {
//        System.out.println("health - STARTED!!! ");
//
//        DataAPIClient client = new DataAPIClient("AstraCS:QkGcdtMFGLbJSaoWxxRZMfbG:ce9c3293ab2fc0a249931bda34adf713aee91e8d98bf4be7f1bc73539f1d83a6");
//        Database db = client.getDatabase("https://ccc01c37-fc99-45ac-9076-4502474c6ece-us-east-2.apps.astra.datastax.com");
//        System.out.println("Connected to AstraDB " + db.listCollectionNames());
//        db.listCollectionNames().forEach(System.out::println);
//        db.listCollectionNames().map(db::getCollection).forEach(System.out::println);
//
//
//       Document doc= db.getCollection("vector1").findById("a734238e-cce2-485f-ba91-33a855482f32").orElse(null);
//       Document doc1= db.getCollection("vector1").findById("a734238e-cce2-485f-ba91").orElse(null);
//       if(doc!=null)
//       logger.info(doc.toJson());
//        return ResponseEntity.ok("UP!!");
//    }
//
//    // @GetMapping("/chatgen")
//    //to read the pdf and get the text then save in astradb vector1 collection
//    @GetMapping("/chatgen")
//    public ResponseEntity<String> chatModelGen() {
//        System.out.println("chatgen - STARTED!!! ");
//        Resource resource = new ClassPathResource("pdfs/Resume.pdf");
//       // ragService.textEmbedding(Collections.singletonList(resource));
//        try {
//           Database database= astraConfig.astraDb();
//           //check if collection exists
//              if(!database.listCollectionNames().toList().contains("vector2"))
//                  database.createCollection("vector2");
//
//                  database.getCollection("vector2").insertOne(
//                          new Document()
//                                  .append("age", 25)
//                                  .append("address", new Document("1")
//                                          .append("city", "Springfield")
//                                          .append("state", "IL")
//                                          .append("zip", 62701)));
//
//                  database.getCollection("vector2").find().forEach(System.out::println);
//
//        } catch (Exception e) {
//            logger.error("Error in chatgen", e);
//        }
//        return ResponseEntity.ok("UP!!");
//    }
//
//    @GetMapping("/addPdf")
//public ResponseEntity<String> listDatabases() {
//    System.out.println("listDatabases - STARTED!!! ");
//
//    Resource resource = new ClassPathResource("pdfs/Resume.pdf");
//        try {
//            Database database = astraConfig.astraDb();
//            if (!database.listCollectionNames().toList().contains("vector2")) {
//                database.createCollection("vector2");
//            }
//            // Read PDF content
//            PdfDocumentReaderConfig pdfDocumentReaderConfig = PdfDocumentReaderConfig.defaultConfig();
//            PagePdfDocumentReader pdfDocumentReader = new PagePdfDocumentReader(resource, pdfDocumentReaderConfig);
//            List<org.springframework.ai.document.Document> documentList = pdfDocumentReader.get();
//            List<org.springframework.ai.document.Document> splitDocuments = new TokenTextSplitter().apply(documentList);
//
//
//            // Save PDF content to vector2 collection
//            for (org.springframework.ai.document.Document doc : splitDocuments) {
//                database.getCollection("vector2").insertOne(Document.create(doc));
//            }
//
//            database.getCollection("vector2").find().forEach(System.out::println);
//
//        } catch (Exception e) {
//            logger.error("Error in chatgen", e);
//        }
//        return ResponseEntity.ok("UP!!");
//}
////i want to use this code in java
////    const remoteVectorStore = new AstraDBVectorStore(
////    new OpenAIEmbeddings({
////        modelName: 'text-embedding-3-small',
////                openAIApiKey: process.env.openAIApiKey,
//
////    }),
////    astraConfig,
////            );
//    //write a new rest endpoint to read the data from astradb and return it
//    @GetMapping("/getPdf")
//    public ResponseEntity<String> getPdf(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
//        System.out.println("getPdf - STARTED!!! ");
//        String vector = "vector3";
//        try {
//            EmbeddingResponse embeddingResponse = this.embeddingModel.embedForResponse(List.of(message));
//
//
//            logger.info(embeddingResponse.toString());
//            Database database = astraConfig.astraDb();
//            //create AstraDBVectorStore
//            if (!database.listCollectionNames().toList().contains(vector)) {
//                database.createCollection(vector);
//            }
//
//            Collection<Document> collection = database.getCollection(vector);
//            Document doc = new Document().append("message", message);
//            collection.insertOne(doc);
//
//           // Document doc2 =new Document().append("message",embeddingResponse.getResults().get(0).toString());
//           // collection.insertOne(doc, float[] embeddings);
//
//           // database.getCollection(vector).insertMany(embeddingResponse.getResults().stream().map(Document::create).toList());
//            database.getCollection(vector).find().forEach(System.out::println);
//
//
//        } catch (Exception e) {
//            logger.error("Error in chatgen", e);
//        }
//        return ResponseEntity.ok("UP!!");
//    }

}
