package it.eng.dome.search.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Base64;
import java.util.Map;

public class VCDecoderBasic {

    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * Decodifica un Verifiable Credential (VC) in formato JWT/JSON senza verificare la signature.
     *
     * @param vcToken il token VC (JWT)
     * @return Map<String, Object> con i dati decodificati
     * @throws Exception se la decodifica fallisce
     */
    public static Map<String, Object> decode(String vcToken) throws Exception {
        if (vcToken == null || vcToken.isEmpty()) {
            throw new IllegalArgumentException("VC token non può essere vuoto");
        }

        // Splitta il JWT in tre parti: header, payload, signature
        String[] parts = vcToken.split("\\.");
        if (parts.length < 2) {
            throw new IllegalArgumentException("Token VC malformato");
        }

        // Il payload è la seconda parte
        String payload = parts[1];

        // Decodifica Base64URL
        byte[] decodedBytes = Base64.getUrlDecoder().decode(payload);
        String json = new String(decodedBytes);

        // Converte il JSON in Map
        Map<String, Object> vcData = mapper.readValue(json, Map.class);

        return vcData;
    }

    /**
     * Estrae il valore gx:labelLevel da un VC token.
     *
     * @param vcToken il token VC (JWT)
     * @return il valore di gx:labelLevel oppure null se non presente
     * @throws Exception se la decodifica fallisce
     */
    public static String extractLabelLevel(String vcToken) throws Exception {
        Map<String, Object> decoded = decode(vcToken);

        Map<String, Object> vcMap = (Map<String, Object>) decoded.get("vc");
        if (vcMap == null) return null;

        Map<String, Object> credentialSubject = (Map<String, Object>) vcMap.get("credentialSubject");
        if (credentialSubject == null) return null;

        return (String) credentialSubject.get("gx:labelLevel");
    }
}
