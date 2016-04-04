package com.echo.rxstructurecache;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.echo.rxstructurecache.cache.RxStructureCache;
import com.echo.rxstructurecache.model.Tweet;
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

        //rxStructureCache.getDataFromCacheThenLoader(User.class, getUserR())
        rxStructureCache.getDataFromLoaderAndCache(getUserR())
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
                        Log.e("jyj", "onN " + users.size());

                    }
                });

        rxStructureCache.getDataFromCacheThenLoader(Tweet.class, getTweetsR())
                .subscribe(new Subscriber<List<Tweet>>() {
                    @Override
                    public void onCompleted() {
                        Log.e("jyj", "t onC");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("jyj", "t onE");
                    }

                    @Override
                    public void onNext(List<Tweet> tweets) {
                        Log.e("jyj", "t onN " + tweets.size());
                    }
                });
    }


    private Observable<List<User>> getUserR() {
        List<User> users = new ArrayList<>();
        User user;
        for (int i = 10; i < 27; i++) {
            user = new User();
            user.setId(i);
            user.setName("jiang" + i);
            user.setAge("29");
            user.setSex("male");
            users.add(user);

        }

        return Observable.just(users);
    }

    private Observable<List<Tweet>> getTweetsR() {
        List<Tweet> tweets = new ArrayList<>();
        Tweet tweet;
        for (int i = 10; i < 20; i++) {
            tweet = new Tweet();
            tweet.setId(i);
            tweet.setContent("content " + i);
            tweet.setUserId(i);
            tweets.add(tweet);
        }
        return Observable.just(tweets);
    }

}
