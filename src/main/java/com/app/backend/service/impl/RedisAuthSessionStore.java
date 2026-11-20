package com.app.backend.service.impl;

import com.app.backend.config.AppProperties;
import com.app.backend.constant.RedisKey;
import com.app.backend.entity.User;
import com.app.backend.exception.CommonException;
import com.app.backend.exception.ErrorCode;
import com.app.backend.service.AuthContextService;
import com.app.backend.service.CacheService;
import com.app.backend.service.intf.AuthSessionStore;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;

import java.time.Duration;


@Component
public class RedisAuthSessionStore implements AuthSessionStore {
    private final CacheService cacheService;
    private final AppProperties appProperties;

    public RedisAuthSessionStore(CacheService cacheService, AppProperties appProperties) {
        this.cacheService = cacheService;
        this.appProperties = appProperties;
    }

    @Override
    public void setCacheRefreshToken(Integer userId, Integer authVersion, String sid, String refreshToken) {
        try {
            cacheService.setCache(
                    RedisKey.refreshTokenTypeById(userId, authVersion, sid),
                    DigestUtils.sha256Hex(refreshToken),
                    Duration.ofSeconds(appProperties.getJwt().getRefreshTokenExpireSeconds())//RT_CACHE_TTL
            );
        }
        catch (Exception exception){
            throw new CommonException(ErrorCode.AUTH_STORE_UNAVAILABLE);
        }

    }

    @Override
    public String getCacheRefreshToken(Integer userId, Integer authVersion, String sid) {
        try{
            return cacheService.getCache(RedisKey.refreshTokenTypeById(userId, authVersion, sid), String.class);
        }
        catch (Exception exception){
            throw new CommonException(ErrorCode.AUTH_STORE_UNAVAILABLE);
        }
    }

    @Override
    public void evictRefreshToken(Integer userId, Integer authVersion, String sid) {
        try {
            cacheService.evict(RedisKey.refreshTokenTypeById(userId,
                    authVersion,
                    sid
            ));
        }
        catch (Exception exception){
            throw new CommonException(ErrorCode.AUTH_STORE_UNAVAILABLE);
        }
    }

    @Override
    public void evictByPattenRefreshToken(Integer userId) {
        try {
            cacheService.evictByPatten(RedisKey.refreshTokenPattenTypeById(userId));
        }
        catch (Exception exception){
            throw new CommonException(ErrorCode.AUTH_STORE_UNAVAILABLE);
        }
    }
}
