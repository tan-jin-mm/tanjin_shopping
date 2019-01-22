package com.tj.utils;

import com.tj.common.RedisPool;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;

/*
* redis常用api封装
* */
public class RedisPoolUtil {

    public static String set(String key,String value){
        Jedis jedis = null;
        String result = null;
        try{
            jedis = RedisPool.getJedis();
            result = jedis.set(key,value);
        }catch (Exception e){
            e.printStackTrace();
            RedisPool.returnBrokenResource(jedis);
        }finally {
            if(jedis!=null){
                RedisPool.returnResource(jedis);
            }
        }
        return result;
    }

    public static String get(String key){
        Jedis jedis = null;
        String result = null;
        try{
            jedis = RedisPool.getJedis();
            result = jedis.get(key);
        }catch (Exception e){
            e.printStackTrace();
            RedisPool.returnBrokenResource(jedis);
        }finally {
            if(jedis!=null){
                RedisPool.returnResource(jedis);
            }
        }
        return result;
    }
    /*
    * 设置过期时间的key-value
    * */
    public static String setex(String key,String value,int expireTime){
        Jedis jedis = null;
        String result = null;
        try{
            jedis = RedisPool.getJedis();
            result = jedis.setex(key,expireTime,value);
        }catch (Exception e){
            e.printStackTrace();
            RedisPool.returnBrokenResource(jedis);
        }finally {
            if(jedis!=null){
                RedisPool.returnResource(jedis);
            }
        }
        return result;
    }
    public static Long expire(String key,int expireTime){
        Jedis jedis = null;
        Long result = null;
        try{
            jedis = RedisPool.getJedis();
            result = jedis.expire(key,expireTime);
        }catch (Exception e){
            e.printStackTrace();
            RedisPool.returnBrokenResource(jedis);
        }finally {
            if(jedis!=null){
                RedisPool.returnResource(jedis);
            }
        }
        return result;
    }

}
