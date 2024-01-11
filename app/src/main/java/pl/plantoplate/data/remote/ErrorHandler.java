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
package pl.plantoplate.data.remote;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Map;
import io.reactivex.rxjava3.core.Single;
import retrofit2.HttpException;

public class ErrorHandler<T> {

    public Single<T>  handleHttpError(Throwable throwable, Map<Integer, String> errorMessages) {
        String errorMessage = "";

        if (throwable instanceof UnknownHostException) {
            errorMessage = "Problemy z siecią. Sprawdź swoje połączenie.";
        }
        else if (throwable instanceof SocketTimeoutException) {
            errorMessage = "Przekroczono limit czasu połączenia z serwerem. Spróbuj ponownie później.";
        }
        else if (throwable instanceof HttpException) {
            errorMessage = errorMessages.get(((HttpException) throwable).code());
            if (errorMessage == null) {
                errorMessage = "Nieznany błąd HTTP (" + ((HttpException) throwable).code() + ")";
            }
            if (((HttpException) throwable).code() == 500) {
                errorMessage = "Wystąpił nieznany błąd serwera.";
            }
        } else {
            errorMessage = "Wystąpił nieznany błąd.";
        }
        return Single.error(new Exception(errorMessage, throwable));
    }
}