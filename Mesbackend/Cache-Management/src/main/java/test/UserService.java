package test;

import config.RedisCacheManager;
import pojo.User;

public class UserService {

    private RedisCacheManager redisCacheManager;

    public UserService() {
        redisCacheManager = new RedisCacheManager("127.0.0.1", 6379);
    }

    public User getUser(String userId) {
        User user = new User();

//        生成缓存键
        String userCacheKey =  userId;

        if (redisCacheManager.exists(userCacheKey)) {
//       从缓存中读取数据
            user = redisCacheManager.get(userCacheKey);
        }

        return user;
    }


}
