package com.eka.supplierconnect.factory;

import java.net.URI;

import javax.annotation.PostConstruct;

import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.eka.supplierconnect.http.HttpProperties;

@Component
public class RestTemplateGetRequestBodyFactory {
	
	@Autowired
	private HttpProperties httpProperties;
	
	private RestTemplate restTemplate = new RestTemplate();

	@PostConstruct
	public void init() {
		this.restTemplate.setRequestFactory(getClientHttpRequestFactory());
	}

	private static final class HttpComponentsClientHttpRequestWithBodyFactory
			extends HttpComponentsClientHttpRequestFactory {
		@Override
		protected HttpUriRequest createHttpUriRequest(HttpMethod httpMethod, URI uri) {
			if (httpMethod == HttpMethod.GET) {
				return new HttpGetRequestWithEntity(uri);
			}
			return super.createHttpUriRequest(httpMethod, uri);
		}
	}

	private static final class HttpGetRequestWithEntity extends HttpEntityEnclosingRequestBase {
		public HttpGetRequestWithEntity(final URI uri) {
			super.setURI(uri);
		}

		@Override
		public String getMethod() {
			return HttpMethod.GET.name();
		}
	}

	private ClientHttpRequestFactory getClientHttpRequestFactory() 
	{
		HttpComponentsClientHttpRequestWithBodyFactory clientHttpRequestFactory
	                      = new HttpComponentsClientHttpRequestWithBodyFactory();
		
	    //Connect timeout
	    clientHttpRequestFactory.setConnectTimeout(httpProperties.getHttpConnectionTimeOut());
	     
	    //Read timeout
	    clientHttpRequestFactory.setReadTimeout(httpProperties.getHttpReadTimeOut());
		
	    return clientHttpRequestFactory;
	}
	
	public RestTemplate getRestTemplate() {
		return restTemplate;
	}
}
