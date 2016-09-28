/*
 * Copyright (c) 2016  Ni YueMing<niyueming@163.com>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 *
 */

package net.nym.napply.http;

import android.content.Context;

import net.nym.napply.library.https.okhttp.OkHttpRequest;

import java.io.IOException;
import java.util.Locale;

import okhttp3.Response;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * @author niyueming
 * @date 2016-08-12
 * @time 17:21
 */
public class RxOkHttpRequestUtils {

    public static Observable<String> test(final Context context){
       return Observable.create(new Observable.OnSubscribe<String>() {
           @Override
           public void call(Subscriber<? super String> subscriber) {
               Response response = new OkHttpRequest(context)
                       .url("http://www.baidu.com")
                       .execute();
               if (response == null){
                   subscriber.onError(new Throwable("response is null"));
                   return;
               }
               if (response.isSuccessful()){
                   try {
                       subscriber.onNext(response.body().string());
                       subscriber.onCompleted();
                   } catch (IOException e) {
                       e.printStackTrace();
                       subscriber.onError(e);
                   }
               }else {
                   subscriber.onError(new Throwable(String.format(Locale.getDefault(),"response is fail,code=%d",response.code())));
               }
           }
       }).subscribeOn(Schedulers.io());
    }
}
