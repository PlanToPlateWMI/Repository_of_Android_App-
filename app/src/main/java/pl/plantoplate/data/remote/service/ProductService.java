/*
 * Copyright 2023 the original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package pl.plantoplate.data.remote.service;

import java.util.List;
import io.reactivex.rxjava3.core.Single;
import pl.plantoplate.data.remote.models.product.Product;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ProductService {

    @GET("api/products")
    Single<List<Product>> getProducts(@Header("Authorization") String token, @Query("type") String type);

    @POST("api/products")
    Single<List<Product>> addProduct(@Header("Authorization") String token, @Body Product product);

    @PATCH("api/products/{id}")
    Single<List<Product>> changeProduct(@Header("Authorization") String token, @Path("id") int productId, @Body Product product);

    @DELETE("api/products/{id}")
    Single<List<Product>> deleteProduct(@Header("Authorization") String token, @Path("id") int productId);
}
