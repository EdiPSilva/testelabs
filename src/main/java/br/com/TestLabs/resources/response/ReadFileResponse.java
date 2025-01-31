package br.com.TestLabs.resources.response;

public record ReadFileResponse(String fileName,
                               int totalRows,
                               int totalRowsError,
                               String message) {
}
