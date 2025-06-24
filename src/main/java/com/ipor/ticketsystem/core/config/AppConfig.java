package com.ipor.ticketsystem.core.config;

import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;


import org.apache.hc.core5.util.Timeout;


@Configuration
public class AppConfig {

    @Bean
    public RestTemplate restTemplate() {
        int connectTimeout = 2000; // milisegundos (2 segundos)
        int readTimeout = 2000;

        // Crear objetos Timeout para la configuración
        Timeout connectTimeoutValue = Timeout.ofMilliseconds(connectTimeout);
        Timeout readTimeoutValue = Timeout.ofMilliseconds(readTimeout);

        // Configuración de RequestConfig con los timeouts adecuados
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(connectTimeoutValue) // Conexión
                .setConnectionRequestTimeout(connectTimeoutValue) // Solicitud de conexión
                .setResponseTimeout(readTimeoutValue) // Lectura de respuesta
                .build();

        // Crear el cliente HTTP con Apache HttpClient 5.x
        CloseableHttpClient client = HttpClients.custom()
                .setDefaultRequestConfig(config)
                .setConnectionManager(new PoolingHttpClientConnectionManager())
                .build();

        // Crear y devolver el RestTemplate con el cliente HTTP configurado
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(client);
        return new RestTemplate(factory);
    }
}
