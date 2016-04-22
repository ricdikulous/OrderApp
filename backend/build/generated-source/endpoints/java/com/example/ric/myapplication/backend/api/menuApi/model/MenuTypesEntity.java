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
 * on 2016-04-22 at 03:42:10 UTC 
 * Modify at your own risk.
 */

package com.example.ric.myapplication.backend.api.menuApi.model;

/**
 * Model definition for MenuTypesEntity.
 *
 * <p> This is the Java data model class that specifies how to parse/serialize into the JSON that is
 * transmitted over HTTP when working with the menuApi. For a detailed explanation see:
 * <a href="https://developers.google.com/api-client-library/java/google-http-java-client/json">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>
 * </p>
 *
 * @author Google, Inc.
 */
@SuppressWarnings("javadoc")
public final class MenuTypesEntity extends com.google.api.client.json.GenericJson {

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private JsonMap menuTypes;

  /**
   * @return value or {@code null} for none
   */
  public JsonMap getMenuTypes() {
    return menuTypes;
  }

  /**
   * @param menuTypes menuTypes or {@code null} for none
   */
  public MenuTypesEntity setMenuTypes(JsonMap menuTypes) {
    this.menuTypes = menuTypes;
    return this;
  }

  @Override
  public MenuTypesEntity set(String fieldName, Object value) {
    return (MenuTypesEntity) super.set(fieldName, value);
  }

  @Override
  public MenuTypesEntity clone() {
    return (MenuTypesEntity) super.clone();
  }

}
