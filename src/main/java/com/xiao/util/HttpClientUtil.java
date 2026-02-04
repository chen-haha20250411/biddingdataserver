package com.xiao.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.*;

/**
 * 说明：httpClientUtil 连接池
 *
 * @author 赵增丰
 * @version 1.0 2014-12-15 下午4:11:07
 */
public class HttpClientUtil {
	private final static Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);
	private static PoolingHttpClientConnectionManager connManager = null;
	private static CloseableHttpClient httpclient = null;
	public final static int connectTimeout = 60000;
	private static final String key="c3a8a587809a492882db692ee19d96ac";
	private static final String secretKey="5201A3E33870BF1D47AB11255BA4BE28";
	private static final String keyNew="c617a7d2145a43918f452de3c68cacf9";
	private static final String secretKeyNew="707D28E4EC594B121730395C7AA454CB";
	
	static {
		try {
			connManager = new PoolingHttpClientConnectionManager();

			// Create socket configuration
			// SocketConfig socketConfig =
			// SocketConfig.custom().setTcpNoDelay(true).build();
			// connManager.setDefaultSocketConfig(socketConfig);
			// Create message constraints
			// MessageConstraints messageConstraints =
			// MessageConstraints.custom()
			// .setMaxHeaderCount(200)
			// .setMaxLineLength(2000)
			// .build();
			// Create connection configuration
			// ConnectionConfig connectionConfig = ConnectionConfig.custom()
			// .setMalformedInputAction(CodingErrorAction.IGNORE)
			// .setUnmappableInputAction(CodingErrorAction.IGNORE)
			// .setCharset(Consts.UTF_8)
			// .setMessageConstraints(messageConstraints)
			// .build();
			// connManager.setDefaultConnectionConfig(connectionConfig);
			connManager.setMaxTotal(200);// 设置整个连接池最大连接数 根据自己的场景决定
			connManager.setDefaultMaxPerRoute(connManager.getMaxTotal());
			httpclient = HttpClients.custom().setConnectionManager(connManager).build();
		} catch (Exception e) {
			logger.error("NoSuchAlgorithmException", e);
		}
	}


	/**
	 * 默认超时为5S 发送 post请求
	 *
	 * @param params
	 * @return
	 */
	public static String post(Map<String, String> params, String url) {
		String resultStr = "";
		RequestConfig requestConfig = RequestConfig.custom()
				.setSocketTimeout(connectTimeout)
				.setConnectTimeout(connectTimeout)
				.setConnectionRequestTimeout(connectTimeout).build();
		// 创建httppost
		HttpPost httpPost = new HttpPost(url);
		// 创建参数队列
		List<NameValuePair> formParams = new ArrayList<NameValuePair>();
		// 绑定到请求 Entry
		for (Map.Entry<String, String> entry : params.entrySet()) {
			formParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		}

		UrlEncodedFormEntity uefEntity;
		try {
			uefEntity = new UrlEncodedFormEntity(formParams, "UTF-8");
			logger.info("executing request params" + formParams.toString());
			httpPost.setEntity(uefEntity);
			httpPost.setConfig(requestConfig);
			logger.info("executing request uri：" + httpPost.getURI());
			CloseableHttpResponse response = httpclient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			try {

				if (entity != null) {
					resultStr = EntityUtils.toString(entity, "UTF-8");
					logger.info(" httpClient response string " + resultStr);
				}
			} finally {
				response.close();
				if (entity != null) {
					EntityUtils.consume(entity);// 关闭
				}
			}
		} catch (ClientProtocolException e) {
			logger.error("http post error " + e.getMessage());
		} catch (UnsupportedEncodingException e) {
			logger.error("http post error " + e.getMessage());
		} catch (IOException e) {
			logger.error("http post error " + e.getMessage());
		} finally {
			// 关闭连接,释放资源
			httpPost.releaseConnection();

		}
		return resultStr;
	}

	/**
	 * 获取精确到秒的时间戳
	 * @return
	 */
	public static int getSecondTimestamp(Date date){
		if (null == date) {
			return 0;
		}
		String timestamp = String.valueOf(date.getTime());
		int length = timestamp.length();
		if (length > 3) {
			return Integer.valueOf(timestamp.substring(0,length-3));
		} else {
			return 0;
		}
	}

	/**
	 * 默认超时为5S 发送 post请求
	 *
	 * @param params
	 * @return
	 */
	public static String getForQichacha(Map<String, String> params, String url) {
		String resultStr = "";
		RequestConfig requestConfig = RequestConfig.custom()
				.setSocketTimeout(connectTimeout)
				.setConnectTimeout(connectTimeout)
				.setConnectionRequestTimeout(connectTimeout).build();

		params.put("key",key);
		StringBuffer param = new StringBuffer();
		int i = 0;
		for (String key : params.keySet()) {
			if (i == 0)
				param.append("?");
			else
				param.append("&");
			param.append(key).append("=").append(params.get(key));
			i++;
		}
//		// 创建参数队列
//		List<NameValuePair> formParams = new ArrayList<NameValuePair>();
//		formParams.add(new BasicNameValuePair("key",key));
//		// 绑定到请求 Entry
//		for (Map.Entry<String, String> entry : params.entrySet()) {
//			formParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
//		}
		// 创建httppost
		UrlEncodedFormEntity uefEntity;
		try {
			//uefEntity = new UrlEncodedFormEntity(param.toString(), "UTF-8");
			//logger.info("executing request params" + formParams.toString());

			HttpGet httpGet = new HttpGet(url + param);
			int secondTimestamp = getSecondTimestamp(new Date());
			httpGet.setHeader("Token",MethodUtil.getMD5String(key.concat(secondTimestamp+"").concat(secretKey)));
			httpGet.setHeader("Timespan", secondTimestamp+"");
			httpGet.setConfig(requestConfig);
			logger.info("executing request get uri：" + httpGet.getURI());
			CloseableHttpResponse response = httpclient.execute(httpGet);
			HttpEntity entity = response.getEntity();
			try {
				if (entity != null) {
					resultStr = EntityUtils.toString(entity, "UTF-8");
					logger.info(" httpClient response string " + resultStr);
				}
			} finally {
				response.close();
				if (entity != null) {
					EntityUtils.consume(entity);// 关闭
				}
				httpGet.releaseConnection();
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			logger.error("http get error " + e.getMessage());
		} catch (UnsupportedEncodingException e) {
			logger.error("http get error " + e.getMessage());
		} catch (IOException e) {
			logger.error("http get error " + e.getMessage());
		}
		return resultStr;
	}
	
	/**
	 * 默认超时为5S 发送 post请求
	 * 单独新接口，单独计费
	 *
	 * @param params
	 * @return
	 */
	public static String getForQichachaNew(Map<String, String> params, String url) {
		String resultStr = "";
		RequestConfig requestConfig = RequestConfig.custom()
				.setSocketTimeout(connectTimeout)
				.setConnectTimeout(connectTimeout)
				.setConnectionRequestTimeout(connectTimeout).build();

		params.put("key",keyNew);
		StringBuffer param = new StringBuffer();
		int i = 0;
		for (String key : params.keySet()) {
			if (i == 0)
				param.append("?");
			else
				param.append("&");
			param.append(key).append("=").append(params.get(key));
			i++;
		}
//		// 创建参数队列
//		List<NameValuePair> formParams = new ArrayList<NameValuePair>();
//		formParams.add(new BasicNameValuePair("key",key));
//		// 绑定到请求 Entry
//		for (Map.Entry<String, String> entry : params.entrySet()) {
//			formParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
//		}
		// 创建httppost
		UrlEncodedFormEntity uefEntity;
		try {
			//uefEntity = new UrlEncodedFormEntity(param.toString(), "UTF-8");
			//logger.info("executing request params" + formParams.toString());

			HttpGet httpGet = new HttpGet(url + param);
			int secondTimestamp = getSecondTimestamp(new Date());
			httpGet.setHeader("Token",MethodUtil.getMD5String(keyNew.concat(secondTimestamp+"").concat(secretKeyNew)));
			httpGet.setHeader("Timespan", secondTimestamp+"");
			httpGet.setConfig(requestConfig);
			logger.info("executing request get uri：" + httpGet.getURI());
			CloseableHttpResponse response = httpclient.execute(httpGet);
			HttpEntity entity = response.getEntity();
			try {
				if (entity != null) {
					resultStr = EntityUtils.toString(entity, "UTF-8");
					logger.info(" httpClient response string " + resultStr);
				}
			} finally {
				response.close();
				if (entity != null) {
					EntityUtils.consume(entity);// 关闭
				}
				httpGet.releaseConnection();
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			logger.error("http get error " + e.getMessage());
		} catch (UnsupportedEncodingException e) {
			logger.error("http get error " + e.getMessage());
		} catch (IOException e) {
			logger.error("http get error " + e.getMessage());
		}
		return resultStr;
	}

	/**
	 * 默认超时为5S 发送 post请求
	 *
	 * @param params
	 * @return
	 */
	public static String postForQichacha(Map<String, String> params, String url) {
		String resultStr = "";
		RequestConfig requestConfig = RequestConfig.custom()
				.setSocketTimeout(connectTimeout)
				.setConnectTimeout(connectTimeout)
				.setConnectionRequestTimeout(connectTimeout).build();

		// 创建参数队列
		List<NameValuePair> formParams = new ArrayList<NameValuePair>();
		formParams.add(new BasicNameValuePair("key",key));
		// 绑定到请求 Entry
		for (Map.Entry<String, String> entry : params.entrySet()) {
			formParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		}
		// 创建httppost
		UrlEncodedFormEntity uefEntity;
		try {
			uefEntity = new UrlEncodedFormEntity(formParams, "UTF-8");
			logger.info("executing request params" + formParams.toString());
			HttpPost httppost = new HttpPost(url+"?"+EntityUtils.toString(uefEntity));
			int secondTimestamp = getSecondTimestamp(new Date());
			httppost.addHeader("Token",MethodUtil.getMD5String(key.concat(secondTimestamp+"").concat(secretKey)));
			httppost.addHeader("Timespan", secondTimestamp+"");
			httppost.setConfig(requestConfig);
			logger.info("executing request post uri：" + httppost.getURI());
			CloseableHttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			try {
				if (entity != null) {
					resultStr = EntityUtils.toString(entity, "UTF-8");
					logger.info(" httpClient response string " + resultStr);
				}
			} finally {
				response.close();
				if (entity != null) {
					EntityUtils.consume(entity);// 关闭
				}
				httppost.releaseConnection();
			}
		} catch (ClientProtocolException e) {
			logger.error("http post error " + e.getMessage());
		} catch (UnsupportedEncodingException e) {
			logger.error("http post error " + e.getMessage());
		} catch (IOException e) {
			logger.error("http post error " + e.getMessage());
		}
		return resultStr;
	}
	
	public static String getHttpSb(String url,String appKey,String sign,String requestTime,String jsonstr,String charset) {
		HttpGet httpGet = null;
		String result = null;
		try{
			//httpClient = HttpClient01.createSSLClientDefault();
			httpGet = new HttpGet(url);
			httpGet.addHeader("Accept", "application/json");
			httpGet.addHeader("Content-Type", "application/json;charset=UTF-8");
			httpGet.addHeader("appKey", appKey);
			httpGet.addHeader("sign", sign);
			httpGet.addHeader("requestTime", requestTime);
			Header[] headers = httpGet.getAllHeaders();
			for (Header header : headers) {
				logger.info(header.getName()+":"+header.getValue());
			}
			StringEntity se = new StringEntity(jsonstr);
			se.setContentEncoding("UTF-8");
			se.setContentType("application/json");
//			httpGet.setEntity(se);
			HttpResponse response = httpclient.execute(httpGet);
			if(response != null){
				HttpEntity resEntity = response.getEntity();
				if(resEntity != null){
					result = EntityUtils.toString(resEntity,charset);
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return result;
	}
	
	public static String getHttp(String url,String jsonstr,String charset) {
		HttpGet httpGet = null;
		String result = null;
		try{
			//httpClient = HttpClient01.createSSLClientDefault();
			httpGet = new HttpGet(url);
			httpGet.addHeader("Accept", "application/json");
			httpGet.addHeader("Content-Type", "application/json;charset=UTF-8");
			Header[] headers = httpGet.getAllHeaders();
			for (Header header : headers) {
				logger.info(header.getName()+":"+header.getValue());
			}
			StringEntity se = new StringEntity(jsonstr);
			se.setContentEncoding("UTF-8");
			se.setContentType("application/json");
//			httpGet.setEntity(se);
			HttpResponse response = httpclient.execute(httpGet);
			if(response != null){
				HttpEntity resEntity = response.getEntity();
				if(resEntity != null){
					result = EntityUtils.toString(resEntity,charset);
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return result;
	}

	public static String postHttp(String url,String jsonstr,String charset) {
		HttpPost httpPost = null;
		String result = null;
		try{
			//httpClient = HttpClient01.createSSLClientDefault();
			httpPost = new HttpPost(url);
			logger.info(url);
			httpPost.addHeader("Accept", "application/json");
			httpPost.addHeader("Content-Type", "application/json;charset=UTF-8");
			Header[] headers = httpPost.getAllHeaders();
			for (Header header : headers) {
				logger.info(header.getName()+":"+header.getValue());
			}
			StringEntity se = new StringEntity(jsonstr, Charset.forName("UTF-8"));//new StringEntity(jsonstr);
			se.setContentEncoding("UTF-8");
			se.setContentType("application/json");
			httpPost.setEntity(se);
			HttpResponse response = httpclient.execute(httpPost);
			if(response != null){
				HttpEntity resEntity = response.getEntity();
				if(resEntity != null){
					result = EntityUtils.toString(resEntity,charset);
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return result;
	}
	
	public static String putHttp(String url, String jsonstr, String charset) {
	    HttpPut httpPut = null;
	    String result = null;
	    try {
	      httpPut = new HttpPut(url);
	      httpPut.addHeader("Accept", "application/json");
	      httpPut.addHeader("Content-Type", "application/json;charset=UTF-8");
	      Header[] headers = httpPut.getAllHeaders();
	      for (Header header : headers)
	        logger.info(header.getName() + ":" + header.getValue()); 
	      StringEntity se = new StringEntity(jsonstr, Charset.forName("UTF-8"));
	      se.setContentEncoding("UTF-8");
	      se.setContentType("application/json");
	      httpPut.setEntity((HttpEntity)se);
	      CloseableHttpResponse closeableHttpResponse = httpclient.execute((HttpUriRequest)httpPut);
	      if (closeableHttpResponse != null) {
	        HttpEntity resEntity = closeableHttpResponse.getEntity();
	        if (resEntity != null)
	          result = EntityUtils.toString(resEntity, charset); 
	      } 
	    } catch (Exception ex) {
	      ex.printStackTrace();
	    } 
	    return result;
	}

	public static String postHttps(String url,String jsonstr,String charset) {
		HttpClient httpClient = null;
		HttpPost httpPost = null;
		String result = null;
		try{
			httpClient = new SSLClient();
			//httpClient = HttpClient01.createSSLClientDefault();
			httpPost = new HttpPost(url);
			httpPost.addHeader("Accept", "application/json");
			httpPost.addHeader("Content-Type", "application/json;charset=UTF-8");
			Header[] headers = httpPost.getAllHeaders();
			for (Header header : headers) {
				logger.info(header.getName()+":"+header.getValue());
			}
			StringEntity se = new StringEntity(jsonstr);
			se.setContentEncoding("UTF-8");
			se.setContentType("application/json");
			httpPost.setEntity(se);
			HttpResponse response = httpClient.execute(httpPost);
			if(response != null){
				HttpEntity resEntity = response.getEntity();
				if(resEntity != null){
					result = EntityUtils.toString(resEntity,charset);
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return result;
	}


	/**
	 * 默认超时为5S 发送 post请求  GTS
	 *
	 * @param params
	 * @return
	 */
	public static String postGTS(String params, String url) {
		String resultStr = "";
		RequestConfig requestConfig = RequestConfig.custom()
				.setSocketTimeout(connectTimeout)
				.setConnectTimeout(connectTimeout)
				.setConnectionRequestTimeout(connectTimeout).build();
		// 创建httppost
		HttpPost httpPost = new HttpPost(url);

		try {
			StringEntity uefEntity = new StringEntity(params,"UTF-8");
			//uefEntity = new UrlEncodedFormEntity(params, "UTF-8");
			httpPost.setEntity(uefEntity);
			httpPost.setConfig(requestConfig);
			logger.info("executing request " + httpPost.getURI());
			CloseableHttpResponse response = httpclient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			try {

				if (entity != null) {
					resultStr = EntityUtils.toString(entity, "UTF-8");
					logger.info(" httpClient response string " + resultStr);
				}
			} finally {
				response.close();
				if (entity != null) {
					EntityUtils.consume(entity);// 关闭
				}
			}
		} catch (ClientProtocolException e) {
			logger.error("http post error " + e.getMessage());
		} catch (UnsupportedEncodingException e) {
			logger.error("http post error " + e.getMessage());
		} catch (IOException e) {
			logger.error("http post error " + e.getMessage());
		} finally {
			// 关闭连接,释放资源
			httpPost.releaseConnection();

		}
		return resultStr;
	}


	/**
	 * <p>
	 * 通过转发节点获取token的url.
	 * </P>
	 */
	public static String REQUEST_TOKEN_URL_SUFFIX = "/api/token/getToken";

	/**
	 * 获取token
	 * @param authorizationCode
	 * @param requestTokenUrlInfo
	 * @return
	 */
	public static String getForJiaShiCangToken(String authorizationCode, String requestTokenUrlInfo) {
		String resultStr = "";
		RequestConfig requestConfig = RequestConfig.custom()
				.setSocketTimeout(connectTimeout)
				.setConnectTimeout(connectTimeout)
				.setConnectionRequestTimeout(connectTimeout).build();
		//String requestTokenUrl = requestTokenUrlInfo + REQUEST_TOKEN_URL_SUFFIX + "?authorizationCode=" + authorizationCode;
		String requestTokenUrl = requestTokenUrlInfo + REQUEST_TOKEN_URL_SUFFIX + "?appId=IRS_Workspace&code="+authorizationCode;
		try {
			logger.info("getForJiaShiCangToken请求路径："+requestTokenUrl);
			HttpGet httpGet = new HttpGet(requestTokenUrl);
			CloseableHttpResponse response = httpclient.execute(httpGet);
			HttpEntity entity = response.getEntity();
			try {
				if (entity != null) {
					resultStr = EntityUtils.toString(entity, "UTF-8");
					logger.info("getForJiaShiCangToken返回结果："+resultStr);
					JSONObject jsonObject = JSON.parseObject(resultStr);
					if (!jsonObject.getBoolean("success")) {
						logger.error("http post error ");
					}
					JSONObject tokenInfo = jsonObject.getJSONObject("data");
					if (null != tokenInfo.getBoolean("success") && !tokenInfo.getBoolean("success")) {
						logger.error("http post error ");
					}

					logger.info(" httpClient response string " + tokenInfo.getString("token"));
					return tokenInfo.getString("token");
				}
			} finally {
				response.close();
				if (entity != null) {
					EntityUtils.consume(entity);// 关闭
				}
				httpGet.releaseConnection();
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			logger.error("http get error " + e.getMessage());
		} catch (UnsupportedEncodingException e) {
			logger.error("http get error " + e.getMessage());
		} catch (IOException e) {
			logger.error("http get error " + e.getMessage());
		}
		return resultStr;
	}

	/**
	 * 默认超时为5S 发送 post请求
	 *
	 * @param params
	 * @return
	 */
	public static String getForJiaShiCang(String authorizationCode,String requestTokenUrlInfo,Map<String, String> params, String url,String h3cId,String h3cAk) {
		String resultStr = "";
		RequestConfig requestConfig = RequestConfig.custom()
				.setSocketTimeout(connectTimeout)
				.setConnectTimeout(connectTimeout)
				.setConnectionRequestTimeout(connectTimeout).build();
		try {
			String token = "";
			if(!"2801".equals(authorizationCode)){
				token = getForJiaShiCangToken(authorizationCode, requestTokenUrlInfo);
			}
			HttpGet httpGet = new HttpGet(url);
			if (params != null && params.size() > 0) {
				Set<String> keySet = params.keySet();
				for (String key : keySet) {
					httpGet.setHeader(key, params.get(key));
				}
			}
			httpGet.setHeader("contentType", "application/json");
			httpGet.setHeader("X-H3C-TOKEN", token);//请求token
			httpGet.setHeader("X-H3C-ID", h3cId);//请求ID
			httpGet.setHeader("X-H3C-APPKEY", h3cAk);//请求ak
			httpGet.setConfig(requestConfig);
			logger.info("executing request get uri：" + httpGet.getURI());
			CloseableHttpResponse response = httpclient.execute(httpGet);
			HttpEntity entity = response.getEntity();
			try {
				if (entity != null) {
					resultStr = EntityUtils.toString(entity, "UTF-8");
					logger.info(" httpClient response string " + resultStr);
				}
			} finally {
				response.close();
				if (entity != null) {
					EntityUtils.consume(entity);// 关闭
				}
				httpGet.releaseConnection();
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			logger.error("http get error " + e.getMessage());
		} catch (UnsupportedEncodingException e) {
			logger.error("http get error " + e.getMessage());
		} catch (IOException e) {
			logger.error("http get error " + e.getMessage());
		}
		return resultStr;
	}

	/**
	 * 默认超时为5S 发送 post请求
	 *
	 * @param params
	 * @return
	 */
	public static String postForJiaShiCang(String authorizationCode,String requestTokenUrlInfo,Map<String, String> params, String url) {
		String resultStr = "";
		RequestConfig requestConfig = RequestConfig.custom()
				.setSocketTimeout(connectTimeout)
				.setConnectTimeout(connectTimeout)
				.setConnectionRequestTimeout(connectTimeout).build();
		try {
			HttpPost httppost = new HttpPost(url);
			final String token = getForJiaShiCangToken(authorizationCode, requestTokenUrlInfo);
			if (params != null && params.size() > 0) {
				Set<String> keySet = params.keySet();
				for (String key : keySet) {
					httppost.setHeader(key, params.get(key));
				}
			}
			httppost.setHeader("contentType", "application/json");
			httppost.setHeader("cmp_token", token);//请求token
			httppost.setConfig(requestConfig);
			logger.info("executing request post uri：" + httppost.getURI());
			CloseableHttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			try {
				if (entity != null) {
					resultStr = EntityUtils.toString(entity, "UTF-8");
					logger.info(" httpClient response string " + resultStr);
				}
			} finally {
				response.close();
				if (entity != null) {
					EntityUtils.consume(entity);// 关闭
				}
				httppost.releaseConnection();
			}
		} catch (ClientProtocolException e) {
			logger.error("http post error " + e.getMessage());
		} catch (UnsupportedEncodingException e) {
			logger.error("http post error " + e.getMessage());
		} catch (IOException e) {
			logger.error("http post error " + e.getMessage());
		}
		return resultStr;
	}


	/**
	 * 默认超时为5S 发送 post请求
	 *
	 * @return
	 */
	public static String postForDianzizang(String appId,String timestamp,String sign,String conentMd5,String jsonParams, String url) {
		String resultStr = "";
		RequestConfig requestConfig = RequestConfig.custom()
				.setSocketTimeout(connectTimeout)
				.setConnectTimeout(connectTimeout)
				.setConnectionRequestTimeout(connectTimeout).build();
		try {
			HttpPost httppost = new HttpPost(url);
			httppost.setHeader("X-Seal-App-Id", appId);
			httppost.setHeader("X-Seal-Ca-Timestamp", timestamp);
			httppost.setHeader("X-Accept", "*/*");
			httppost.setHeader("Content-Type", "application/json;charset=UTF-8");
			httppost.setHeader("X-Seal-Ca-Signature", sign);
			httppost.setHeader("X-Content-MD5", conentMd5);
			httppost.setConfig(requestConfig);
			httppost.setEntity(new StringEntity(jsonParams, ContentType.create("application/json", "utf-8")));
			logger.info("发送的参数：" + jsonParams);
			//logger.info("Entity：" + httppost.getEntity().toString());
			logger.info("executing request post uri：" + httppost.getURI());
			Header[] headers = httppost.getAllHeaders();
			for (Header header : headers) {
				logger.info(header.getName()+":"+header.getValue());
			}
			CloseableHttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			try {
				if (entity != null) {
					resultStr = EntityUtils.toString(entity, "UTF-8");
					logger.info(" httpClient response string " + resultStr);
				}
			} finally {
				response.close();
				if (entity != null) {
					EntityUtils.consume(entity);// 关闭
				}
				httppost.releaseConnection();
			}
		} catch (ClientProtocolException e) {
			logger.error("http post error " + e.getMessage());
		} catch (UnsupportedEncodingException e) {
			logger.error("http post error " + e.getMessage());
		} catch (IOException e) {
			logger.error("http post error " + e.getMessage());
		}
		return resultStr;
	}
}
