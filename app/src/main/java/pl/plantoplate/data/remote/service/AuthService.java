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

import io.reactivex.rxjava3.core.Single;
import pl.plantoplate.data.remote.models.auth.CodeResponse;
import pl.plantoplate.data.remote.models.auth.JwtResponse;
import pl.plantoplate.data.remote.models.Message;
import pl.plantoplate.data.remote.models.user.UserRegisterData;
import pl.plantoplate.data.remote.models.auth.SignInData;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface AuthService {

    @POST("api/auth/signup")
    Single<CodeResponse> sendUserRegisterData(@Body UserRegisterData info);

    @GET("api/mail/code")
    Single<CodeResponse> getEmailConfirmCode(@Query("email") String email, @Query("type") String type);

    @POST("api/auth/signin")
    Single<JwtResponse> signinUser(@Body SignInData info);

    @POST("api/auth/password/reset")
    Single<Message> resetPassword(@Body SignInData info);

    @GET("api/users/emails")
    Single<Message> userExists(@Query("email") String email);
}
