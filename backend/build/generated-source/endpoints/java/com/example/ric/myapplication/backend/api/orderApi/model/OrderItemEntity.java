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
 * (build: 2016-05-27 16:00:31 UTC)
 * on 2016-06-02 at 05:06:28 UTC 
 * Modify at your own risk.
 */

package com.example.ric.myapplication.backend.api.orderApi.model;

/**
 * Model definition for OrderItemEntity.
 *
 * <p> This is the Java data model class that specifies how to parse/serialize into the JSON that is
 * transmitted over HTTP when working with the orderApi. For a detailed explanation see:
 * <a href="https://developers.google.com/api-client-library/java/google-http-java-client/json">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>
 * </p>
 *
 * @author Google, Inc.
 */
@SuppressWarnings("javadoc")
public final class OrderItemEntity extends com.google.api.client.json.GenericJson {

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.Integer amount;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.util.List<java.lang.String> ingredientsExcluded;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String menuItemKeyString;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String name;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String specialRequest;

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Integer getAmount() {
    return amount;
  }

  /**
   * @param amount amount or {@code null} for none
   */
  public OrderItemEntity setAmount(java.lang.Integer amount) {
    this.amount = amount;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.util.List<java.lang.String> getIngredientsExcluded() {
    return ingredientsExcluded;
  }

  /**
   * @param ingredientsExcluded ingredientsExcluded or {@code null} for none
   */
  public OrderItemEntity setIngredientsExcluded(java.util.List<java.lang.String> ingredientsExcluded) {
    this.ingredientsExcluded = ingredientsExcluded;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getMenuItemKeyString() {
    return menuItemKeyString;
  }

  /**
   * @param menuItemKeyString menuItemKeyString or {@code null} for none
   */
  public OrderItemEntity setMenuItemKeyString(java.lang.String menuItemKeyString) {
    this.menuItemKeyString = menuItemKeyString;
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
  public OrderItemEntity setName(java.lang.String name) {
    this.name = name;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getSpecialRequest() {
    return specialRequest;
  }

  /**
   * @param specialRequest specialRequest or {@code null} for none
   */
  public OrderItemEntity setSpecialRequest(java.lang.String specialRequest) {
    this.specialRequest = specialRequest;
    return this;
  }

  @Override
  public OrderItemEntity set(String fieldName, Object value) {
    return (OrderItemEntity) super.set(fieldName, value);
  }

  @Override
  public OrderItemEntity clone() {
    return (OrderItemEntity) super.clone();
  }

}
