package com.app.backend.service.intf;

import com.app.backend.entity.User;

public interface AuthSessionStore {
    public void setCacheRefreshToken(Integer userId, Integer authVersion, String sid, String refreshToken);
    public String getCacheRefreshToken(Integer userId, Integer authVersion, String uid);
    public void evictRefreshToken(Integer userId, Integer authVersion, String sid);
    public void evictByPattenRefreshToken(Integer userId);
}
