package com.tj.common;

import com.tj.utils.PropertiesUtils;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
@Component
public class RedisPool {
    private static JedisPool pool;
    //初始化连接池
    public static void  initPool(){
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(PropertiesUtils.MAXTOTAL);
        config.setMaxIdle(PropertiesUtils.MAXIDLE);
        config.setMinIdle(PropertiesUtils.MINIDLE);
        config.setTestOnBorrow(PropertiesUtils.TESTBORROW);
        config.setTestOnReturn(PropertiesUtils.TESTRETURN);
        //在连接耗尽时，是否阻塞，false抛出异常，true等待连接，直到超时，默认true
        config.setBlockWhenExhausted(true);
        pool = new JedisPool(config, PropertiesUtils.REDISIP, PropertiesUtils.REDISPORT,1000*2,PropertiesUtils.REDISPASSWORD,0);
    }
    static {
        initPool();
    }

    public static Jedis getJedis(){
        return pool.getResource();
    }
    public static void returnResource(Jedis jedis){
        pool.returnResource(jedis);
    }
    public static void returnBrokenResource(Jedis jedis){
        pool.returnBrokenResource(jedis);
    }

    public static void main(String[] args) {
        Jedis jedis = getJedis();
        jedis.set("root","123");
        returnResource(jedis);
        pool.destroy();
        System.out.println("=====redis end=====");
    }
    
}
