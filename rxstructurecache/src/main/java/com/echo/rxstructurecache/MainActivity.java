package com.echo.rxstructurecache;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.echo.rxstructurecache.cache.RxStructureCache;
import com.echo.rxstructurecache.model.User;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RxStructureCache rxStructureCache = new RxStructureCache();
        rxStructureCache.init(this);

        rxStructureCache.getDataFromCacheThenLoader(User.class, getUserR())
                .subscribe(new Subscriber<List<User>>() {
                    @Override
                    public void onCompleted() {
                        Log.e("jyj", "onC");

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("jyj", "onE " + e.getMessage());

                    }

                    @Override
                    public void onNext(List<User> users) {
                        Log.e("jyj", "onN " + users.get(0).getId());

                    }
                })
        ;
    }


    private Observable<List<User>> getUserR() {
        List<User> users = new ArrayList<>();
        User user = new User();
        user.setId(0);
        user.setName("jiang");
        user.setAge("29");
        user.setSex("male");
        users.add(user);

        user = new User();
        user.setId(1);
        user.setName("ying");
        user.setAge("29");
        user.setSex("male");
        users.add(user);

        return Observable.just(users);
    }

}
