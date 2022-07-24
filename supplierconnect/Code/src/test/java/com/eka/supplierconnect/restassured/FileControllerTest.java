package com.eka.supplierconnect.restassured;

import static io.restassured.RestAssured.given;

import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.springframework.util.ResourceUtils;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.eka.supplierconnect.model.AuthenicateRequest;
import com.eka.supplierconnect.model.MailInfo;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

public class FileControllerTest {
	String token = null;
	String tenant = null;
	String userName = null;
	String password = null;
	Map<String, Object> requestPayload = new HashMap<String, Object>();
	private static final String tokenGenerationApiPath = "/api/authenticate";
	MailInfo mailInfo = new MailInfo();
	Properties prop = new Properties();
	String sc_appUUID = null;

	/**
	 * @throws Exception
	 */
	@BeforeTest
	public void setUp() throws Exception {

		prop.load(new FileInputStream(ResourceUtils.getFile("classpath:restassured.properties")));
		tenant = prop.getProperty("tenant");
		URL connectUrl = new URL((String) prop.get("eka_connect_host"));
		userName = prop.getProperty("userName");
		password = prop.getProperty("password");		
		token = authenticateUser(userName, password, connectUrl);		
		URL utilityUrl = new URL((String) prop.get("utility_host"));
		RestAssured.baseURI = "http://" + utilityUrl.getHost();
		RestAssured.port = utilityUrl.getPort();
		sc_appUUID = prop.getProperty("sc_appUUID", "467a28cc-bc93-4e38-8ff5-0a56ae128f3b");

	}

	/**
	 * Test case to verify sendEmail with correct input values
	 */
	@Test
	public void whenEmailAPICalledWithCorrectValues_ThenSuccess() {
		String toAddr = prop.getProperty("mail_to_addr");
		mailInfo.setToAddress(new String[] { toAddr });
		ValidatableResponse response = given().log().all().header("Authorization", token).header("X-TenantID", tenant)
				.header("Content-Type", "application/json").with().body(prepareEmailPayload()).when()
				.request("POST", "/supplierconnect/" + sc_appUUID + "/sendEmail").then().assertThat().statusCode(202);
	}

	/**
	 * Test case to verify sendEmail api with wrong input
	 */
	@Test
	public void whenEmailAPICalledWithInCorrectValues_ThenFailure() {
		mailInfo.setToAddress(new String[] { "incorrectEmail" });

		ValidatableResponse response = given().log().all().header("Authorization", token).header("X-TenantID", tenant)
				.header("Content-Type", "application/json").with().body(prepareEmailPayload()).when()
				.request("POST", "/supplierconnect/" + sc_appUUID + "/sendEmail").then().assertThat().statusCode(500);
	}

	/**
	 * @param username
	 * @param password
	 * @param connectURL
	 * @return
	 * @throws UnsupportedEncodingException
	 * 
	 * Method to get the token
	 */
	private String authenticateUser(String username, String password, URL connectURL)
			throws UnsupportedEncodingException {
		AuthenicateRequest req = new AuthenicateRequest();
		req.setUsername(username);
		req.setPassword(password);
		RestAssured.baseURI = "http://" + connectURL.getHost();
		RestAssured.port = connectURL.getPort();
		String base64encodedUsernamePassword = Base64.getEncoder()
				.encodeToString((username + ":" + password).getBytes("utf-8"));
		Response response = given().header("Content-Type", "application/json")
				.header("Authorization", "Basic " + base64encodedUsernamePassword).header("X-TenantID", tenant)
				.body(req).when().post(tokenGenerationApiPath);
		JsonPath jsonPath = new JsonPath(response.asString());
		return jsonPath.getString("auth2AccessToken.access_token");
	}

	/**
	 * Method to prepare the payload for email endpoint
	 * @return
	 */
	private MailInfo prepareEmailPayload() {
		mailInfo.setFromAddress("noreply@ekaplus.com");
		mailInfo.setSubject("Calling from Restassured");
		mailInfo.setMessage("Calling from Restassured");
		return mailInfo;
	}

}
