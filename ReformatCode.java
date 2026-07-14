import java.io.File;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;

public class ReformatCode {
    public static void main(String[] args) throws Exception {
        Path startPath = Paths.get("src/main/java/com/app/backend");
        Files.walk(startPath)
             .filter(Files::isRegularFile)
             .filter(p -> p.toString().endsWith(".java"))
             .forEach(ReformatCode::formatFile);
        System.out.println("Formatting completed!");
    }

    private static void formatFile(Path path) {
        try {
            List<String> lines = Files.readAllLines(path);
            List<String> newLines = new ArrayList<>();
            List<String> imports = new ArrayList<>();
            
            String fullContent = String.join("\n", lines);
            
            for (String line : lines) {
                // Remove unused imports
                if (line.startsWith("import ")) {
                    if (line.contains("java.time.LocalDateTime") && !fullContent.contains("LocalDateTime ")) continue;
                    if (line.contains("java.time.*") && !fullContent.contains("LocalDateTime ")) continue;
                    if (line.contains("java.util.*") && !fullContent.contains("List<")) continue;
                    if (line.contains("java.util.List") && !fullContent.contains("List<")) continue;
                    if (line.contains("java.util.stream.Collectors") && !fullContent.contains("Collectors.")) continue;
                    
                    imports.add(line);
                    continue;
                }
                
                // Put each annotation on its own line
                Matcher m = Pattern.compile("^([ \\t]*)(@.+)$").matcher(line);
                if (m.matches() && !line.startsWith("package ") && !line.startsWith("import ")) {
                    String indent = m.group(1);
                    String annos = m.group(2);
                    
                    // Split by space followed by @
                    String[] parts = annos.split(" (?=@)");
                    for (int i = 0; i < parts.length - 1; i++) {
                        newLines.add(indent + parts[i].trim());
                    }
                    String lastAnno = parts[parts.length - 1].trim();
                    
                    // Split if the annotation is on the same line as class/field/method declaration
                    Matcher m2 = Pattern.compile("^(@[A-Za-z0-9_]+(?:\\([^)]*\\))?)\\s+(public |private |protected |class |interface ).*$").matcher(lastAnno);
                    if (m2.matches()) {
                        String annoOnly = m2.group(1);
                        String rest = lastAnno.substring(annoOnly.length()).trim();
                        newLines.add(indent + annoOnly);
                        newLines.add(indent + rest);
                    } else {
                        newLines.add(indent + lastAnno);
                    }
                } else if (line.startsWith("package ")) {
                    newLines.add(line);
                    newLines.add(""); // blank line after package
                } else {
                    newLines.add(line);
                }
            }
            
            // Organize imports
            Collections.sort(imports, (a, b) -> {
                int wA = getImportWeight(a);
                int wB = getImportWeight(b);
                if (wA != wB) return Integer.compare(wA, wB);
                return a.compareTo(b);
            });
            
            if (!imports.isEmpty()) {
                newLines.addAll(2, imports);
                newLines.add(2 + imports.size(), ""); // blank line after imports
            }
            
            // Ensure no double blank lines
            List<String> finalLines = new ArrayList<>();
            for (int i = 0; i < newLines.size(); i++) {
                String l = newLines.get(i);
                if (i > 0 && l.trim().isEmpty() && finalLines.get(finalLines.size() - 1).trim().isEmpty()) {
                    continue;
                }
                finalLines.add(l);
            }
            
            Files.writeString(path, String.join("\n", finalLines) + "\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static int getImportWeight(String imp) {
        if (imp.startsWith("import java.")) return 1;
        if (imp.startsWith("import javax.") || imp.startsWith("import jakarta.")) return 2;
        if (imp.startsWith("import org.")) return 3;
        if (imp.startsWith("import com.")) return 4;
        return 5;
    }
}
