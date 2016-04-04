# RxStructureCache
A RxCache for structure data.

##Why
1. I need to cache some structure data, such as tweets, rank list and so on.
2. I need some feature like this: firstly load data from cache, then continue to load data from server.

##How
1. make your structure data model implement ```CacheAble``` interface.
    ```
    public class Tweet implements CacheAble {
        private long id;
        private String content;
        private long userId;
    }
    ```
2. Implement your ```loader```
3. Init ```RxStructureCache```
    ```
        RxStructureCache rxStructureCache = RxStructureCache.getInstance(this, 20);
    ```
4.  When you need firstly get cached data, and then remote data, just call```getFromCacheThenLoader```
5. Please refer to ```app``` module

##Todo
1. Test it in production environment.
2. Support non-structure data?

##Libraries
[GreenDAO](https://github.com/greenrobot/greenDAO)

##Thanks
[RxCache](https://github.com/VictorAlbertos/RxCache), I like it, but I think it's lack of some features I need.


