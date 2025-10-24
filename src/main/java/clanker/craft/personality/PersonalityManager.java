package clanker.craft.personality;

import net.fabricmc.loader.api.FabricLoader;
import clanker.craft.config.Config;
import clanker.craft.i18n.LanguageManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public final class PersonalityManager {
    private PersonalityManager() {}

    private static final String DEFAULT_PERSONALITY_NAME = "clanker";
    private static volatile String cachedName;
    private static volatile String cachedText;
    private static volatile String cachedCapabilities;
    private static volatile String runtimePersonalityOverride; // In-game personality override

    public static String getActivePersonality() {
        try {
            String name = loadPersonalityName();
            if (name == null || name.isBlank()) name = DEFAULT_PERSONALITY_NAME;
            if (name.equals(cachedName) && cachedText != null) return cachedText;

            String text = loadPersonalityText(name);
            if (text == null || text.isBlank()) text = builtInFallback(name);
            
            // Append language instruction to ensure LLM responds in the correct language
            String languageInstruction = LanguageManager.getLanguageInstruction();
            text = text + " " + languageInstruction;
            
            // Append capabilities so the Clanker knows what it can do
            String capabilities = loadCapabilities();
            if (capabilities != null && !capabilities.isBlank()) {
                text = text + "\n\n" + capabilities;
            }
            
            cachedName = name;
            cachedText = text;
            return text;
        } catch (Throwable t) {
            return builtInFallback(DEFAULT_PERSONALITY_NAME);
        }
    }

    // Load personality name from the properties file
    private static String loadPersonalityName() {
        // Check runtime override first (set by @personality command)
        if (runtimePersonalityOverride != null && !runtimePersonalityOverride.isBlank()) {
            return runtimePersonalityOverride;
        }
        
        // Read from: clankercraft-llm.properties --> CLANKER_PERSONALITY
        Path cfgFile = FabricLoader.getInstance().getConfigDir().resolve("clankercraft-llm.properties");
        if (Files.exists(cfgFile)) {
            Properties props = new Properties();
            try (InputStream in = Files.newInputStream(cfgFile)) {
                props.load(in);
                String v = Config.personalityName();
                if (v == null) v = props.getProperty("CLANKER_PERSONALITY");
                if (v != null) return v.trim();
            } catch (IOException ignored) { }
        }
        return null;
    }

    // Load personality text from file (saved in assets)
    private static String loadPersonalityText(String name) {
        // 1) Try user-provided file: config/clankercraft/personalities/<Name>.txt
        Path folder = FabricLoader.getInstance().getConfigDir().resolve("clankercraft").resolve("personalities");
        Path file = folder.resolve(name + ".txt");
        if (Files.exists(file)) {
            try { return Files.readString(file, StandardCharsets.UTF_8); } catch (IOException ignored) { }
        }
        // 2) Try bundled defaults: assets/clankercraft/personalities/<Name>.txt
        String cp = "/assets/clankercraft/personalities/" + name + ".txt";
        try (InputStream in = PersonalityManager.class.getResourceAsStream(cp)) {
            if (in != null) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) sb.append(line).append('\n');
                    return sb.toString();
                }
            }
        } catch (IOException ignored) { }
        return null;
    }

    private static String builtInFallback(String name) {
        // Default to Excited
        return "System instruction: You are Clanker, an excitable, upbeat companion. " +
                "Respond with enthusiasm, positivity, and helpful energy. Keep responses concise but lively.";
    }

    // Load capabilities documentation from bundled resource
    private static String loadCapabilities() {
        // Return cached if already loaded
        if (cachedCapabilities != null) return cachedCapabilities;
        
        // Try to load from bundled resource: assets/clankercraft/capabilities.txt
        String cp = "/assets/clankercraft/capabilities.txt";
        try (InputStream in = PersonalityManager.class.getResourceAsStream(cp)) {
            if (in != null) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) sb.append(line).append('\n');
                    cachedCapabilities = sb.toString();
                    return cachedCapabilities;
                }
            }
        } catch (IOException e) {
            // If capabilities file is missing, return empty string (non-critical)
            cachedCapabilities = "";
        }
        return cachedCapabilities;
    }

    /**
     * Set personality at runtime (in-game). This overrides config file settings.
     * @param personalityName The name of the personality (case-insensitive, without .txt extension)
     */
    public static void setPersonality(String personalityName) {
        if (personalityName == null || personalityName.isBlank()) {
            runtimePersonalityOverride = null;
        } else {
            runtimePersonalityOverride = personalityName.trim();
        }
        // Clear cache to force reload with new personality
        cachedName = null;
        cachedText = null;
    }

    /**
     * Get list of available personality names from bundled resources and custom folder.
     * @return List of personality names (without .txt extension)
     */
    public static java.util.List<String> getAvailablePersonalities() {
        java.util.Set<String> personalities = new java.util.LinkedHashSet<>();
        
        // 1) Add bundled personalities from resources
        // We know these exist: Excited, Grumpy, Robotic
        String[] bundled = {"Excited", "Grumpy", "Robotic"};
        for (String name : bundled) {
            String cp = "/assets/clankercraft/personalities/" + name + ".txt";
            try (InputStream in = PersonalityManager.class.getResourceAsStream(cp)) {
                if (in != null) {
                    personalities.add(name);
                }
            } catch (IOException ignored) { }
        }
        
        // 2) Add custom personalities from config folder
        Path folder = FabricLoader.getInstance().getConfigDir().resolve("clankercraft").resolve("personalities");
        if (Files.exists(folder) && Files.isDirectory(folder)) {
            try {
                Files.list(folder)
                    .filter(p -> p.toString().endsWith(".txt"))
                    .forEach(p -> {
                        String fileName = p.getFileName().toString();
                        String name = fileName.substring(0, fileName.length() - 4);
                        personalities.add(name);
                    });
            } catch (IOException ignored) { }
        }
        
        return new java.util.ArrayList<>(personalities);
    }

    /**
     * Get the current active personality name (either override or config default).
     * @return The name of the currently active personality
     */
    public static String getCurrentPersonalityName() {
        String name = loadPersonalityName();
        if (name == null || name.isBlank()) name = DEFAULT_PERSONALITY_NAME;
        return name;
    }
}

