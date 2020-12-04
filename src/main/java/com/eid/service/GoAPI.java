package com.eid.service;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ResourceBundle;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class GoAPI {

	private static final Logger log = LogManager.getLogger(GoAPI.class);

	private static ResourceBundle rb = ResourceBundle.getBundle("url");
	private static final String GET_CERT_URL = rb.getString("getCerts_url");
	private static final String DO_SIGN_URL = rb.getString("helperDoSign_url");

	public String getCert() throws IOException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
		// String returnValue;
		try (CloseableHttpClient httpclient = createAcceptSelfSignedCertificateClient()) {
			HttpGet httpget = new HttpGet(GET_CERT_URL);
			log.error("Executing request " + httpget.getRequestLine());

			CloseableHttpResponse response = httpclient.execute(httpget);
			StatusLine statusLine = response.getStatusLine();
			log.error(statusLine.getStatusCode() + " " + statusLine.getReasonPhrase());
			HttpEntity entity = response.getEntity();
			return EntityUtils.toString(entity);

		}
		// return returnValue;

	}

	public String doSign(String dtbsB64)
			throws IOException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
		try (CloseableHttpClient httpclient = createAcceptSelfSignedCertificateClient()) {
			HttpPost httpPost = new HttpPost(DO_SIGN_URL);
			String json = new JSONObject().put("dtbsB64", dtbsB64).toString();
			httpPost.setEntity(new StringEntity(json));
			log.error("Executing request " + httpPost.getRequestLine());

			CloseableHttpResponse response = httpclient.execute(httpPost);
			log.error("----------------------------------------");
			StatusLine statusLine = response.getStatusLine();
			log.error(statusLine.getStatusCode() + " " + statusLine.getReasonPhrase());
			HttpEntity entity = response.getEntity();
			return EntityUtils.toString(entity);
		}

	}

	private static CloseableHttpClient createAcceptSelfSignedCertificateClient()
			throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {

		// use the TrustSelfSignedStrategy to allow Self Signed Certificates
		SSLContext sslContext = SSLContextBuilder.create().loadTrustMaterial(new TrustSelfSignedStrategy()).build();

		// we can optionally disable hostname verification.
		// if you don't want to further weaken the security, you don't have to include
		// this.
		HostnameVerifier allowAllHosts = new NoopHostnameVerifier();

		// create an SSL Socket Factory to use the SSLContext with the trust self signed
		// certificate strategy
		// and allow all hosts verifier.
		SSLConnectionSocketFactory connectionFactory = new SSLConnectionSocketFactory(sslContext, allowAllHosts);

		// finally create the HttpClient using HttpClient factory methods and assign the
		// ssl socket factory
		return HttpClients.custom().setSSLSocketFactory(connectionFactory).build();
	}

}
