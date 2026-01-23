package dd.task.device.manager

import org.apache.hc.client5.http.classic.HttpClient
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory

import java.util.function.Supplier

@TestConfiguration
class TestConfig {

    @Bean
    RestTemplateBuilder restTemplateBuilder() {
        HttpClient httpClient = HttpClientBuilder.create().build()
        def factory = new HttpComponentsClientHttpRequestFactory(httpClient)

        return new RestTemplateBuilder()
                .requestFactory({ -> factory } as Supplier)
    }
}
