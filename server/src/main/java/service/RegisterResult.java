package service;

public record RegisterResult(boolean didItWork, String message, String authToken) {
}
