package com.jobtracker.jobtrackerbackend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jobtracker.jobtrackerbackend.config.OpenAIConfig;
import com.jobtracker.jobtrackerbackend.dto.AnalyseOffreResponse;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OffreIAService {

    private final OpenAIConfig config;
    private final ObjectMapper mapper = new ObjectMapper();

    public OffreIAService(OpenAIConfig config) {
        this.config = config;
    }

    public AnalyseOffreResponse analyser(String url) throws Exception {

        String prompt = """
        Tu es un assistant qui analyse des offres d’emploi à partir d’une URL.

        Retourne UNIQUEMENT un JSON STRICT avec EXACTEMENT ce schéma :

        {
          "entreprise": string | null,
          "titrePoste": string | null,
          "typeContrat": string | null,
          "localisation": string | null,
          "description": string | null,
          "stack": string[] | null
        }

        ❌ Aucun texte avant ou après
        ❌ Pas de markdown
        ❌ Pas de commentaire

        URL :
        """ + url;

        // ✅ Construction JSON SAFE
        Map<String, Object> body = new HashMap<>();
        body.put("model", config.model);
        body.put("temperature", 0);

        Map<String, String> message = new HashMap<>();
        message.put("role", "user");
        message.put("content", prompt);

        body.put("messages", List.of(message));

        String jsonBody = mapper.writeValueAsString(body);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.openai.com/v1/chat/completions"))
                .header("Authorization", "Bearer " + config.apiKey)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpResponse<String> response =
                HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

        // 🔎 LOG RÉPONSE BRUTE
        System.out.println("🔵 OPENAI RAW RESPONSE:");
        System.out.println(response.body());

        JsonNode root = mapper.readTree(response.body());

        if (root.has("error")) {
            throw new RuntimeException("Erreur OpenAI : " + root.get("error").toString());
        }

        JsonNode choices = root.path("choices");
        if (!choices.isArray() || choices.isEmpty()) {
            throw new RuntimeException("Réponse OpenAI invalide : choices vide");
        }

        JsonNode contentNode = choices
                .get(0)
                .path("message")
                .path("content");

        if (contentNode.isMissingNode() || contentNode.isNull()) {
            throw new RuntimeException("Réponse OpenAI invalide : content manquant");
        }

        String contenuIA = contentNode.asText().trim();

        System.out.println("🟢 CONTENU IA PARSÉ:");
        System.out.println(contenuIA);

        if (!contenuIA.startsWith("{")) {
            throw new RuntimeException("Réponse IA non JSON : " + contenuIA);
        }

        return mapper.readValue(contenuIA, AnalyseOffreResponse.class);
    }
}
