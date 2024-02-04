package com.example.DOA;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public interface CustomHttpClient {
    CompletableFuture<HttpResponse<String>> sendAsync(HttpRequest request, HttpResponse.BodyHandler<String> responseBodyHandler);
}

