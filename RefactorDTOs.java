import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class RefactorDTOs {
    public static void main(String[] args) throws Exception {
        String baseDir = "src/main/java/com/app/backend/";
        File reqDir = new File(baseDir + "dtos/request");
        File[] reqFiles = reqDir.listFiles((dir, name) -> name.startsWith("Create") && name.endsWith("Request.java"));
        
        if (reqFiles == null || reqFiles.length == 0) {
            System.out.println("No CreateXXXRequest files found.");
            return;
        }

        for (File cFile : reqFiles) {
            String name = cFile.getName();
            String entityName = name.substring("Create".length(), name.length() - "Request.java".length());

            // Read CreateXXXRequest
            String content = Files.readString(cFile.toPath());
            
            // Refactor class name
            content = content.replace("class Create" + entityName + "Request", "class " + entityName + "Request");
            
            // Write to new XXXRequest.java
            Files.writeString(Paths.get(baseDir + "dtos/request/" + entityName + "Request.java"), content);

            // Delete old Create and Update files
            Files.deleteIfExists(cFile.toPath());
            Files.deleteIfExists(Paths.get(baseDir + "dtos/request/Update" + entityName + "Request.java"));

            // 1. Refactor Service Interface
            Path servicePath = Paths.get(baseDir + "service/" + entityName + "Service.java");
            if (Files.exists(servicePath)) {
                String svcContent = Files.readString(servicePath);
                svcContent = svcContent.replaceAll("Create" + entityName + "Request", entityName + "Request");
                svcContent = svcContent.replaceAll("Update" + entityName + "Request", entityName + "Request");
                Files.writeString(servicePath, svcContent);
            }

            // 2. Refactor Service Implementation
            Path implPath = Paths.get(baseDir + "service/impl/" + entityName + "ServiceImpl.java");
            if (Files.exists(implPath)) {
                String implContent = Files.readString(implPath);
                implContent = implContent.replaceAll("Create" + entityName + "Request", entityName + "Request");
                implContent = implContent.replaceAll("Update" + entityName + "Request", entityName + "Request");
                Files.writeString(implPath, implContent);
            }

            // 3. Refactor Controller
            Path ctrlPath = Paths.get(baseDir + "controller/" + entityName + "Controller.java");
            if (Files.exists(ctrlPath)) {
                String ctrlContent = Files.readString(ctrlPath);
                ctrlContent = ctrlContent.replaceAll("Create" + entityName + "Request", entityName + "Request");
                ctrlContent = ctrlContent.replaceAll("Update" + entityName + "Request", entityName + "Request");
                Files.writeString(ctrlPath, ctrlContent);
            }
        }
        System.out.println("DTO refactoring completed successfully.");
    }
}
