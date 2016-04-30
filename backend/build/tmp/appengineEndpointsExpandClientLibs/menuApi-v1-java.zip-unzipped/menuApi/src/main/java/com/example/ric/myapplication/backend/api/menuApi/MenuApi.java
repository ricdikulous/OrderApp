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
 * on 2016-04-30 at 04:15:55 UTC 
 * Modify at your own risk.
 */

package com.example.ric.myapplication.backend.api.menuApi;

/**
 * Service definition for MenuApi (v1).
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
 * This service uses {@link MenuApiRequestInitializer} to initialize global parameters via its
 * {@link Builder}.
 * </p>
 *
 * @since 1.3
 * @author Google, Inc.
 */
@SuppressWarnings("javadoc")
public class MenuApi extends com.google.api.client.googleapis.services.json.AbstractGoogleJsonClient {

  // Note: Leave this static initializer at the top of the file.
  static {
    com.google.api.client.util.Preconditions.checkState(
        com.google.api.client.googleapis.GoogleUtils.MAJOR_VERSION == 1 &&
        com.google.api.client.googleapis.GoogleUtils.MINOR_VERSION >= 15,
        "You are currently running with version %s of google-api-client. " +
        "You need at least version 1.15 of google-api-client to run version " +
        "1.21.0 of the menuApi library.", com.google.api.client.googleapis.GoogleUtils.VERSION);
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
  public static final String DEFAULT_SERVICE_PATH = "menuApi/v1/";

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
  public MenuApi(com.google.api.client.http.HttpTransport transport, com.google.api.client.json.JsonFactory jsonFactory,
      com.google.api.client.http.HttpRequestInitializer httpRequestInitializer) {
    this(new Builder(transport, jsonFactory, httpRequestInitializer));
  }

  /**
   * @param builder builder
   */
  MenuApi(Builder builder) {
    super(builder);
  }

  @Override
  protected void initialize(com.google.api.client.googleapis.services.AbstractGoogleClientRequest<?> httpClientRequest) throws java.io.IOException {
    super.initialize(httpClientRequest);
  }

  /**
   * Create a request for the method "getMenuItems".
   *
   * This request holds the parameters needed by the menuApi server.  After setting any optional
   * parameters, call the {@link GetMenuItems#execute()} method to invoke the remote operation.
   *
   * @return the request
   */
  public GetMenuItems getMenuItems() throws java.io.IOException {
    GetMenuItems result = new GetMenuItems();
    initialize(result);
    return result;
  }

  public class GetMenuItems extends MenuApiRequest<com.example.ric.myapplication.backend.api.menuApi.model.MenuItemEntityCollection> {

    private static final String REST_PATH = "menuitementitycollection";

    /**
     * Create a request for the method "getMenuItems".
     *
     * This request holds the parameters needed by the the menuApi server.  After setting any optional
     * parameters, call the {@link GetMenuItems#execute()} method to invoke the remote operation. <p>
     * {@link
     * GetMenuItems#initialize(com.google.api.client.googleapis.services.AbstractGoogleClientRequest)}
     * must be called to initialize this instance immediately after invoking the constructor. </p>
     *
     * @since 1.13
     */
    protected GetMenuItems() {
      super(MenuApi.this, "GET", REST_PATH, null, com.example.ric.myapplication.backend.api.menuApi.model.MenuItemEntityCollection.class);
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
    public GetMenuItems setAlt(java.lang.String alt) {
      return (GetMenuItems) super.setAlt(alt);
    }

    @Override
    public GetMenuItems setFields(java.lang.String fields) {
      return (GetMenuItems) super.setFields(fields);
    }

    @Override
    public GetMenuItems setKey(java.lang.String key) {
      return (GetMenuItems) super.setKey(key);
    }

    @Override
    public GetMenuItems setOauthToken(java.lang.String oauthToken) {
      return (GetMenuItems) super.setOauthToken(oauthToken);
    }

    @Override
    public GetMenuItems setPrettyPrint(java.lang.Boolean prettyPrint) {
      return (GetMenuItems) super.setPrettyPrint(prettyPrint);
    }

    @Override
    public GetMenuItems setQuotaUser(java.lang.String quotaUser) {
      return (GetMenuItems) super.setQuotaUser(quotaUser);
    }

    @Override
    public GetMenuItems setUserIp(java.lang.String userIp) {
      return (GetMenuItems) super.setUserIp(userIp);
    }

    @Override
    public GetMenuItems set(String parameterName, Object value) {
      return (GetMenuItems) super.set(parameterName, value);
    }
  }

  /**
   * Create a request for the method "getMenuTypes".
   *
   * This request holds the parameters needed by the menuApi server.  After setting any optional
   * parameters, call the {@link GetMenuTypes#execute()} method to invoke the remote operation.
   *
   * @return the request
   */
  public GetMenuTypes getMenuTypes() throws java.io.IOException {
    GetMenuTypes result = new GetMenuTypes();
    initialize(result);
    return result;
  }

  public class GetMenuTypes extends MenuApiRequest<com.example.ric.myapplication.backend.api.menuApi.model.MenuTypesEntity> {

    private static final String REST_PATH = "menutypesentity";

    /**
     * Create a request for the method "getMenuTypes".
     *
     * This request holds the parameters needed by the the menuApi server.  After setting any optional
     * parameters, call the {@link GetMenuTypes#execute()} method to invoke the remote operation. <p>
     * {@link
     * GetMenuTypes#initialize(com.google.api.client.googleapis.services.AbstractGoogleClientRequest)}
     * must be called to initialize this instance immediately after invoking the constructor. </p>
     *
     * @since 1.13
     */
    protected GetMenuTypes() {
      super(MenuApi.this, "GET", REST_PATH, null, com.example.ric.myapplication.backend.api.menuApi.model.MenuTypesEntity.class);
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
    public GetMenuTypes setAlt(java.lang.String alt) {
      return (GetMenuTypes) super.setAlt(alt);
    }

    @Override
    public GetMenuTypes setFields(java.lang.String fields) {
      return (GetMenuTypes) super.setFields(fields);
    }

    @Override
    public GetMenuTypes setKey(java.lang.String key) {
      return (GetMenuTypes) super.setKey(key);
    }

    @Override
    public GetMenuTypes setOauthToken(java.lang.String oauthToken) {
      return (GetMenuTypes) super.setOauthToken(oauthToken);
    }

    @Override
    public GetMenuTypes setPrettyPrint(java.lang.Boolean prettyPrint) {
      return (GetMenuTypes) super.setPrettyPrint(prettyPrint);
    }

    @Override
    public GetMenuTypes setQuotaUser(java.lang.String quotaUser) {
      return (GetMenuTypes) super.setQuotaUser(quotaUser);
    }

    @Override
    public GetMenuTypes setUserIp(java.lang.String userIp) {
      return (GetMenuTypes) super.setUserIp(userIp);
    }

    @Override
    public GetMenuTypes set(String parameterName, Object value) {
      return (GetMenuTypes) super.set(parameterName, value);
    }
  }

  /**
   * Create a request for the method "getMenuVersion".
   *
   * This request holds the parameters needed by the menuApi server.  After setting any optional
   * parameters, call the {@link GetMenuVersion#execute()} method to invoke the remote operation.
   *
   * @return the request
   */
  public GetMenuVersion getMenuVersion() throws java.io.IOException {
    GetMenuVersion result = new GetMenuVersion();
    initialize(result);
    return result;
  }

  public class GetMenuVersion extends MenuApiRequest<com.example.ric.myapplication.backend.api.menuApi.model.MenuVersionEntity> {

    private static final String REST_PATH = "menuversionentity";

    /**
     * Create a request for the method "getMenuVersion".
     *
     * This request holds the parameters needed by the the menuApi server.  After setting any optional
     * parameters, call the {@link GetMenuVersion#execute()} method to invoke the remote operation.
     * <p> {@link GetMenuVersion#initialize(com.google.api.client.googleapis.services.AbstractGoogleCl
     * ientRequest)} must be called to initialize this instance immediately after invoking the
     * constructor. </p>
     *
     * @since 1.13
     */
    protected GetMenuVersion() {
      super(MenuApi.this, "GET", REST_PATH, null, com.example.ric.myapplication.backend.api.menuApi.model.MenuVersionEntity.class);
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
    public GetMenuVersion setAlt(java.lang.String alt) {
      return (GetMenuVersion) super.setAlt(alt);
    }

    @Override
    public GetMenuVersion setFields(java.lang.String fields) {
      return (GetMenuVersion) super.setFields(fields);
    }

    @Override
    public GetMenuVersion setKey(java.lang.String key) {
      return (GetMenuVersion) super.setKey(key);
    }

    @Override
    public GetMenuVersion setOauthToken(java.lang.String oauthToken) {
      return (GetMenuVersion) super.setOauthToken(oauthToken);
    }

    @Override
    public GetMenuVersion setPrettyPrint(java.lang.Boolean prettyPrint) {
      return (GetMenuVersion) super.setPrettyPrint(prettyPrint);
    }

    @Override
    public GetMenuVersion setQuotaUser(java.lang.String quotaUser) {
      return (GetMenuVersion) super.setQuotaUser(quotaUser);
    }

    @Override
    public GetMenuVersion setUserIp(java.lang.String userIp) {
      return (GetMenuVersion) super.setUserIp(userIp);
    }

    @Override
    public GetMenuVersion set(String parameterName, Object value) {
      return (GetMenuVersion) super.set(parameterName, value);
    }
  }

  /**
   * Builder for {@link MenuApi}.
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

    /** Builds a new instance of {@link MenuApi}. */
    @Override
    public MenuApi build() {
      return new MenuApi(this);
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
     * Set the {@link MenuApiRequestInitializer}.
     *
     * @since 1.12
     */
    public Builder setMenuApiRequestInitializer(
        MenuApiRequestInitializer menuapiRequestInitializer) {
      return (Builder) super.setGoogleClientRequestInitializer(menuapiRequestInitializer);
    }

    @Override
    public Builder setGoogleClientRequestInitializer(
        com.google.api.client.googleapis.services.GoogleClientRequestInitializer googleClientRequestInitializer) {
      return (Builder) super.setGoogleClientRequestInitializer(googleClientRequestInitializer);
    }
  }
}
