package aula.iceibank.dto;

public record LoginResponse(String token, String type, long expiresIn, int agencyId) {
}
