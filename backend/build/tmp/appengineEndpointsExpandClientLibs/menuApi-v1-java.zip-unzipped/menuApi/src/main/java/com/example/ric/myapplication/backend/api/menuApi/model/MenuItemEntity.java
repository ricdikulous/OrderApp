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
 * on 2016-04-21 at 06:44:07 UTC 
 * Modify at your own risk.
 */

package com.example.ric.myapplication.backend.api.menuApi.model;

/**
 * Model definition for MenuItemEntity.
 *
 * <p> This is the Java data model class that specifies how to parse/serialize into the JSON that is
 * transmitted over HTTP when working with the menuApi. For a detailed explanation see:
 * <a href="https://developers.google.com/api-client-library/java/google-http-java-client/json">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>
 * </p>
 *
 * @author Google, Inc.
 */
@SuppressWarnings("javadoc")
public final class MenuItemEntity extends com.google.api.client.json.GenericJson {

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.util.List<java.lang.String> allergens;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String description;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.util.List<java.lang.String> ingredients;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String keyString;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String name;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key @com.google.api.client.json.JsonString
  private java.lang.Long price;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String servingUrl;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key @com.google.api.client.json.JsonString
  private java.lang.Long type;

  /**
   * @return value or {@code null} for none
   */
  public java.util.List<java.lang.String> getAllergens() {
    return allergens;
  }

  /**
   * @param allergens allergens or {@code null} for none
   */
  public MenuItemEntity setAllergens(java.util.List<java.lang.String> allergens) {
    this.allergens = allergens;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getDescription() {
    return description;
  }

  /**
   * @param description description or {@code null} for none
   */
  public MenuItemEntity setDescription(java.lang.String description) {
    this.description = description;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.util.List<java.lang.String> getIngredients() {
    return ingredients;
  }

  /**
   * @param ingredients ingredients or {@code null} for none
   */
  public MenuItemEntity setIngredients(java.util.List<java.lang.String> ingredients) {
    this.ingredients = ingredients;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getKeyString() {
    return keyString;
  }

  /**
   * @param keyString keyString or {@code null} for none
   */
  public MenuItemEntity setKeyString(java.lang.String keyString) {
    this.keyString = keyString;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getName() {
    return name;
  }

  /**
   * @param name name or {@code null} for none
   */
  public MenuItemEntity setName(java.lang.String name) {
    this.name = name;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Long getPrice() {
    return price;
  }

  /**
   * @param price price or {@code null} for none
   */
  public MenuItemEntity setPrice(java.lang.Long price) {
    this.price = price;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getServingUrl() {
    return servingUrl;
  }

  /**
   * @param servingUrl servingUrl or {@code null} for none
   */
  public MenuItemEntity setServingUrl(java.lang.String servingUrl) {
    this.servingUrl = servingUrl;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Long getType() {
    return type;
  }

  /**
   * @param type type or {@code null} for none
   */
  public MenuItemEntity setType(java.lang.Long type) {
    this.type = type;
    return this;
  }

  @Override
  public MenuItemEntity set(String fieldName, Object value) {
    return (MenuItemEntity) super.set(fieldName, value);
  }

  @Override
  public MenuItemEntity clone() {
    return (MenuItemEntity) super.clone();
  }

}
