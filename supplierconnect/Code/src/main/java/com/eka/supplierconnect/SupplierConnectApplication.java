package com.eka.supplierconnect;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.eka.supplierconnect.http.HttpProperties;

@SpringBootApplication
@EnableAspectJAutoProxy
public class SupplierConnectApplication {
	
	@Autowired
	private HttpProperties httpProperties;

	public static void main(String[] args) {
		SpringApplication.run(SupplierConnectApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate() {

		RestTemplate restTemplate = new RestTemplate();

		// Do any additional configuration here
		MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
		mappingJackson2HttpMessageConverter.setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_JSON,MediaType.APPLICATION_JSON_UTF8,
				MediaType.APPLICATION_OCTET_STREAM, MediaType.MULTIPART_FORM_DATA, MediaType.TEXT_PLAIN));

		restTemplate.getMessageConverters().add(mappingJackson2HttpMessageConverter);

		restTemplate.setRequestFactory(getClientHttpRequestFactory(httpProperties.getHttpConnectionTimeOut(),
				httpProperties.getHttpReadTimeOut()));
		
		return restTemplate;
	}
	
	// https://stackoverflow.com/questions/45713767/spring-rest-template-readtimeout
		public static ClientHttpRequestFactory getClientHttpRequestFactory(
				int httpConnectionTimeOut, int httpReadTimeOut) {
			HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
			// Connect timeout
			clientHttpRequestFactory.setConnectTimeout(httpConnectionTimeOut);

			// Read timeout
			clientHttpRequestFactory.setReadTimeout(httpReadTimeOut);
			return clientHttpRequestFactory;
		}
}