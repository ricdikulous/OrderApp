/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
/*
 * This code was generated by https://github.com/google/apis-client-generator/
 * (build: 2016-04-08 17:16:44 UTC)
 * on 2016-04-30 at 13:01:13 UTC 
 * Modify at your own risk.
 */

package com.example.ric.myapplication.backend.api.orderApi;

/**
 * Service definition for OrderApi (v1).
 *
 * <p>
 * This is an API
 * </p>
 *
 * <p>
 * For more information about this service, see the
 * <a href="" target="_blank">API Documentation</a>
 * </p>
 *
 * <p>
 * This service uses {@link OrderApiRequestInitializer} to initialize global parameters via its
 * {@link Builder}.
 * </p>
 *
 * @since 1.3
 * @author Google, Inc.
 */
@SuppressWarnings("javadoc")
public class OrderApi extends com.google.api.client.googleapis.services.json.AbstractGoogleJsonClient {

  // Note: Leave this static initializer at the top of the file.
  static {
    com.google.api.client.util.Preconditions.checkState(
        com.google.api.client.googleapis.GoogleUtils.MAJOR_VERSION == 1 &&
        com.google.api.client.googleapis.GoogleUtils.MINOR_VERSION >= 15,
        "You are currently running with version %s of google-api-client. " +
        "You need at least version 1.15 of google-api-client to run version " +
        "1.21.0 of the orderApi library.", com.google.api.client.googleapis.GoogleUtils.VERSION);
  }

  /**
   * The default encoded root URL of the service. This is determined when the library is generated
   * and normally should not be changed.
   *
   * @since 1.7
   */
  public static final String DEFAULT_ROOT_URL = "https://myApplicationId.appspot.com/_ah/api/";

  /**
   * The default encoded service path of the service. This is determined when the library is
   * generated and normally should not be changed.
   *
   * @since 1.7
   */
  public static final String DEFAULT_SERVICE_PATH = "orderApi/v1/";

  /**
   * The default encoded base URL of the service. This is determined when the library is generated
   * and normally should not be changed.
   */
  public static final String DEFAULT_BASE_URL = DEFAULT_ROOT_URL + DEFAULT_SERVICE_PATH;

  /**
   * Constructor.
   *
   * <p>
   * Use {@link Builder} if you need to specify any of the optional parameters.
   * </p>
   *
   * @param transport HTTP transport, which should normally be:
   *        <ul>
   *        <li>Google App Engine:
   *        {@code com.google.api.client.extensions.appengine.http.UrlFetchTransport}</li>
   *        <li>Android: {@code newCompatibleTransport} from
   *        {@code com.google.api.client.extensions.android.http.AndroidHttp}</li>
   *        <li>Java: {@link com.google.api.client.googleapis.javanet.GoogleNetHttpTransport#newTrustedTransport()}
   *        </li>
   *        </ul>
   * @param jsonFactory JSON factory, which may be:
   *        <ul>
   *        <li>Jackson: {@code com.google.api.client.json.jackson2.JacksonFactory}</li>
   *        <li>Google GSON: {@code com.google.api.client.json.gson.GsonFactory}</li>
   *        <li>Android Honeycomb or higher:
   *        {@code com.google.api.client.extensions.android.json.AndroidJsonFactory}</li>
   *        </ul>
   * @param httpRequestInitializer HTTP request initializer or {@code null} for none
   * @since 1.7
   */
  public OrderApi(com.google.api.client.http.HttpTransport transport, com.google.api.client.json.JsonFactory jsonFactory,
      com.google.api.client.http.HttpRequestInitializer httpRequestInitializer) {
    this(new Builder(transport, jsonFactory, httpRequestInitializer));
  }

  /**
   * @param builder builder
   */
  OrderApi(Builder builder) {
    super(builder);
  }

  @Override
  protected void initialize(com.google.api.client.googleapis.services.AbstractGoogleClientRequest<?> httpClientRequest) throws java.io.IOException {
    super.initialize(httpClientRequest);
  }

  /**
   * Create a request for the method "advanceStatus".
   *
   * This request holds the parameters needed by the orderApi server.  After setting any optional
   * parameters, call the {@link AdvanceStatus#execute()} method to invoke the remote operation.
   *
   * @param orderKeyString
   * @param currentStatusString
   * @return the request
   */
  public AdvanceStatus advanceStatus(java.lang.String orderKeyString, java.lang.String currentStatusString) throws java.io.IOException {
    AdvanceStatus result = new AdvanceStatus(orderKeyString, currentStatusString);
    initialize(result);
    return result;
  }

  public class AdvanceStatus extends OrderApiRequest<com.example.ric.myapplication.backend.api.orderApi.model.OrderReceiptEntity> {

    private static final String REST_PATH = "advanceStatus/{orderKeyString}/{currentStatusString}";

    /**
     * Create a request for the method "advanceStatus".
     *
     * This request holds the parameters needed by the the orderApi server.  After setting any
     * optional parameters, call the {@link AdvanceStatus#execute()} method to invoke the remote
     * operation. <p> {@link AdvanceStatus#initialize(com.google.api.client.googleapis.services.Abstra
     * ctGoogleClientRequest)} must be called to initialize this instance immediately after invoking
     * the constructor. </p>
     *
     * @param orderKeyString
     * @param currentStatusString
     * @since 1.13
     */
    protected AdvanceStatus(java.lang.String orderKeyString, java.lang.String currentStatusString) {
      super(OrderApi.this, "POST", REST_PATH, null, com.example.ric.myapplication.backend.api.orderApi.model.OrderReceiptEntity.class);
      this.orderKeyString = com.google.api.client.util.Preconditions.checkNotNull(orderKeyString, "Required parameter orderKeyString must be specified.");
      this.currentStatusString = com.google.api.client.util.Preconditions.checkNotNull(currentStatusString, "Required parameter currentStatusString must be specified.");
    }

    @Override
    public AdvanceStatus setAlt(java.lang.String alt) {
      return (AdvanceStatus) super.setAlt(alt);
    }

    @Override
    public AdvanceStatus setFields(java.lang.String fields) {
      return (AdvanceStatus) super.setFields(fields);
    }

    @Override
    public AdvanceStatus setKey(java.lang.String key) {
      return (AdvanceStatus) super.setKey(key);
    }

    @Override
    public AdvanceStatus setOauthToken(java.lang.String oauthToken) {
      return (AdvanceStatus) super.setOauthToken(oauthToken);
    }

    @Override
    public AdvanceStatus setPrettyPrint(java.lang.Boolean prettyPrint) {
      return (AdvanceStatus) super.setPrettyPrint(prettyPrint);
    }

    @Override
    public AdvanceStatus setQuotaUser(java.lang.String quotaUser) {
      return (AdvanceStatus) super.setQuotaUser(quotaUser);
    }

    @Override
    public AdvanceStatus setUserIp(java.lang.String userIp) {
      return (AdvanceStatus) super.setUserIp(userIp);
    }

    @com.google.api.client.util.Key
    private java.lang.String orderKeyString;

    /**

     */
    public java.lang.String getOrderKeyString() {
      return orderKeyString;
    }

    public AdvanceStatus setOrderKeyString(java.lang.String orderKeyString) {
      this.orderKeyString = orderKeyString;
      return this;
    }

    @com.google.api.client.util.Key
    private java.lang.String currentStatusString;

    /**

     */
    public java.lang.String getCurrentStatusString() {
      return currentStatusString;
    }

    public AdvanceStatus setCurrentStatusString(java.lang.String currentStatusString) {
      this.currentStatusString = currentStatusString;
      return this;
    }

    @Override
    public AdvanceStatus set(String parameterName, Object value) {
      return (AdvanceStatus) super.set(parameterName, value);
    }
  }

  /**
   * Create a request for the method "getOrders".
   *
   * This request holds the parameters needed by the orderApi server.  After setting any optional
   * parameters, call the {@link GetOrders#execute()} method to invoke the remote operation.
   *
   * @return the request
   */
  public GetOrders getOrders() throws java.io.IOException {
    GetOrders result = new GetOrders();
    initialize(result);
    return result;
  }

  public class GetOrders extends OrderApiRequest<com.example.ric.myapplication.backend.api.orderApi.model.OrderEntityCollection> {

    private static final String REST_PATH = "orderentitycollection";

    /**
     * Create a request for the method "getOrders".
     *
     * This request holds the parameters needed by the the orderApi server.  After setting any
     * optional parameters, call the {@link GetOrders#execute()} method to invoke the remote
     * operation. <p> {@link
     * GetOrders#initialize(com.google.api.client.googleapis.services.AbstractGoogleClientRequest)}
     * must be called to initialize this instance immediately after invoking the constructor. </p>
     *
     * @since 1.13
     */
    protected GetOrders() {
      super(OrderApi.this, "GET", REST_PATH, null, com.example.ric.myapplication.backend.api.orderApi.model.OrderEntityCollection.class);
    }

    @Override
    public com.google.api.client.http.HttpResponse executeUsingHead() throws java.io.IOException {
      return super.executeUsingHead();
    }

    @Override
    public com.google.api.client.http.HttpRequest buildHttpRequestUsingHead() throws java.io.IOException {
      return super.buildHttpRequestUsingHead();
    }

    @Override
    public GetOrders setAlt(java.lang.String alt) {
      return (GetOrders) super.setAlt(alt);
    }

    @Override
    public GetOrders setFields(java.lang.String fields) {
      return (GetOrders) super.setFields(fields);
    }

    @Override
    public GetOrders setKey(java.lang.String key) {
      return (GetOrders) super.setKey(key);
    }

    @Override
    public GetOrders setOauthToken(java.lang.String oauthToken) {
      return (GetOrders) super.setOauthToken(oauthToken);
    }

    @Override
    public GetOrders setPrettyPrint(java.lang.Boolean prettyPrint) {
      return (GetOrders) super.setPrettyPrint(prettyPrint);
    }

    @Override
    public GetOrders setQuotaUser(java.lang.String quotaUser) {
      return (GetOrders) super.setQuotaUser(quotaUser);
    }

    @Override
    public GetOrders setUserIp(java.lang.String userIp) {
      return (GetOrders) super.setUserIp(userIp);
    }

    @Override
    public GetOrders set(String parameterName, Object value) {
      return (GetOrders) super.set(parameterName, value);
    }
  }

  /**
   * Create a request for the method "putOrder".
   *
   * This request holds the parameters needed by the orderApi server.  After setting any optional
   * parameters, call the {@link PutOrder#execute()} method to invoke the remote operation.
   *
   * @param content the {@link com.example.ric.myapplication.backend.api.orderApi.model.OrderEntity}
   * @return the request
   */
  public PutOrder putOrder(com.example.ric.myapplication.backend.api.orderApi.model.OrderEntity content) throws java.io.IOException {
    PutOrder result = new PutOrder(content);
    initialize(result);
    return result;
  }

  public class PutOrder extends OrderApiRequest<com.example.ric.myapplication.backend.api.orderApi.model.OrderReceiptEntity> {

    private static final String REST_PATH = "putOrder";

    /**
     * Create a request for the method "putOrder".
     *
     * This request holds the parameters needed by the the orderApi server.  After setting any
     * optional parameters, call the {@link PutOrder#execute()} method to invoke the remote operation.
     * <p> {@link
     * PutOrder#initialize(com.google.api.client.googleapis.services.AbstractGoogleClientRequest)}
     * must be called to initialize this instance immediately after invoking the constructor. </p>
     *
     * @param content the {@link com.example.ric.myapplication.backend.api.orderApi.model.OrderEntity}
     * @since 1.13
     */
    protected PutOrder(com.example.ric.myapplication.backend.api.orderApi.model.OrderEntity content) {
      super(OrderApi.this, "POST", REST_PATH, content, com.example.ric.myapplication.backend.api.orderApi.model.OrderReceiptEntity.class);
    }

    @Override
    public PutOrder setAlt(java.lang.String alt) {
      return (PutOrder) super.setAlt(alt);
    }

    @Override
    public PutOrder setFields(java.lang.String fields) {
      return (PutOrder) super.setFields(fields);
    }

    @Override
    public PutOrder setKey(java.lang.String key) {
      return (PutOrder) super.setKey(key);
    }

    @Override
    public PutOrder setOauthToken(java.lang.String oauthToken) {
      return (PutOrder) super.setOauthToken(oauthToken);
    }

    @Override
    public PutOrder setPrettyPrint(java.lang.Boolean prettyPrint) {
      return (PutOrder) super.setPrettyPrint(prettyPrint);
    }

    @Override
    public PutOrder setQuotaUser(java.lang.String quotaUser) {
      return (PutOrder) super.setQuotaUser(quotaUser);
    }

    @Override
    public PutOrder setUserIp(java.lang.String userIp) {
      return (PutOrder) super.setUserIp(userIp);
    }

    @Override
    public PutOrder set(String parameterName, Object value) {
      return (PutOrder) super.set(parameterName, value);
    }
  }

  /**
   * Builder for {@link OrderApi}.
   *
   * <p>
   * Implementation is not thread-safe.
   * </p>
   *
   * @since 1.3.0
   */
  public static final class Builder extends com.google.api.client.googleapis.services.json.AbstractGoogleJsonClient.Builder {

    /**
     * Returns an instance of a new builder.
     *
     * @param transport HTTP transport, which should normally be:
     *        <ul>
     *        <li>Google App Engine:
     *        {@code com.google.api.client.extensions.appengine.http.UrlFetchTransport}</li>
     *        <li>Android: {@code newCompatibleTransport} from
     *        {@code com.google.api.client.extensions.android.http.AndroidHttp}</li>
     *        <li>Java: {@link com.google.api.client.googleapis.javanet.GoogleNetHttpTransport#newTrustedTransport()}
     *        </li>
     *        </ul>
     * @param jsonFactory JSON factory, which may be:
     *        <ul>
     *        <li>Jackson: {@code com.google.api.client.json.jackson2.JacksonFactory}</li>
     *        <li>Google GSON: {@code com.google.api.client.json.gson.GsonFactory}</li>
     *        <li>Android Honeycomb or higher:
     *        {@code com.google.api.client.extensions.android.json.AndroidJsonFactory}</li>
     *        </ul>
     * @param httpRequestInitializer HTTP request initializer or {@code null} for none
     * @since 1.7
     */
    public Builder(com.google.api.client.http.HttpTransport transport, com.google.api.client.json.JsonFactory jsonFactory,
        com.google.api.client.http.HttpRequestInitializer httpRequestInitializer) {
      super(
          transport,
          jsonFactory,
          DEFAULT_ROOT_URL,
          DEFAULT_SERVICE_PATH,
          httpRequestInitializer,
          false);
    }

    /** Builds a new instance of {@link OrderApi}. */
    @Override
    public OrderApi build() {
      return new OrderApi(this);
    }

    @Override
    public Builder setRootUrl(String rootUrl) {
      return (Builder) super.setRootUrl(rootUrl);
    }

    @Override
    public Builder setServicePath(String servicePath) {
      return (Builder) super.setServicePath(servicePath);
    }

    @Override
    public Builder setHttpRequestInitializer(com.google.api.client.http.HttpRequestInitializer httpRequestInitializer) {
      return (Builder) super.setHttpRequestInitializer(httpRequestInitializer);
    }

    @Override
    public Builder setApplicationName(String applicationName) {
      return (Builder) super.setApplicationName(applicationName);
    }

    @Override
    public Builder setSuppressPatternChecks(boolean suppressPatternChecks) {
      return (Builder) super.setSuppressPatternChecks(suppressPatternChecks);
    }

    @Override
    public Builder setSuppressRequiredParameterChecks(boolean suppressRequiredParameterChecks) {
      return (Builder) super.setSuppressRequiredParameterChecks(suppressRequiredParameterChecks);
    }

    @Override
    public Builder setSuppressAllChecks(boolean suppressAllChecks) {
      return (Builder) super.setSuppressAllChecks(suppressAllChecks);
    }

    /**
     * Set the {@link OrderApiRequestInitializer}.
     *
     * @since 1.12
     */
    public Builder setOrderApiRequestInitializer(
        OrderApiRequestInitializer orderapiRequestInitializer) {
      return (Builder) super.setGoogleClientRequestInitializer(orderapiRequestInitializer);
    }

    @Override
    public Builder setGoogleClientRequestInitializer(
        com.google.api.client.googleapis.services.GoogleClientRequestInitializer googleClientRequestInitializer) {
      return (Builder) super.setGoogleClientRequestInitializer(googleClientRequestInitializer);
    }
  }
}
