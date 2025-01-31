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
package pl.plantoplate.ui.main.settings.change_permissions;

import android.app.Application;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import java.util.List;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import pl.plantoplate.data.remote.models.user.UserInfo;
import pl.plantoplate.data.remote.repository.UserRepository;

/**
 * This class is responsible for changing the permissions of the user.
 */
public class ChangePermissionsViewModel extends AndroidViewModel {

    private final CompositeDisposable compositeDisposable;
    private final UserRepository userRepository;
    private final SharedPreferences prefs;
    private final MutableLiveData<String> responseMessage;
    private final MutableLiveData<List<UserInfo>> usersInfo;

    public ChangePermissionsViewModel(@NonNull Application application) {
        super(application);
        prefs = application.getSharedPreferences("prefs", 0);
        userRepository = new UserRepository();
        compositeDisposable = new CompositeDisposable();

        responseMessage = new MutableLiveData<>();
        usersInfo = new MutableLiveData<>();
    }

    public MutableLiveData<String> getResponseMessage() {
        return responseMessage;
    }

    public MutableLiveData<List<UserInfo>> getUsersInfo() {
        return usersInfo;
    }

    public void fetchUsersInfo() {
        String token = "Bearer " + prefs.getString("token", "");

        Disposable disposable = userRepository.getUsersInfo(token)
                .subscribe(userInfo -> usersInfo.setValue(filterUsers(userInfo, prefs.getString("email", ""))),
                        throwable -> responseMessage.setValue(throwable.getMessage()));

        compositeDisposable.add(disposable);
    }

    public void changePermissions(UserInfo userInfo) {
        String token = "Bearer " + prefs.getString("token", "");

        Disposable disposable = userRepository.changePermissions(token, userInfo)
                .subscribe(response -> {
                    usersInfo.setValue(filterUsers(response, prefs.getString("email", "")));
                    String role = "";
                    if (userInfo.getRole().equals("USER")) {
                        role = "Użytkownik";
                    } else if (userInfo.getRole().equals("ADMIN")) {
                        role = "Administrator";
                    }
                    responseMessage.setValue("Zmieniono uprawnienia użytkownika " + userInfo.getUsername() + " na " + role);
                }, throwable -> responseMessage.setValue(throwable.getMessage()));

        compositeDisposable.add(disposable);
    }

    /**
     * This method filters the list of users and removes the current user from it.
     *
     * @param response list of users
     * @param email    current user email
     * @return filtered list of users
     */
    public List<UserInfo> filterUsers(List<UserInfo> response, String email){
        for (int i = 0; i < response.size(); i++) {
            if (response.get(i).getEmail().equals(email)) {
                response.remove(i);
                break;
            }
        }
        return response;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }
}