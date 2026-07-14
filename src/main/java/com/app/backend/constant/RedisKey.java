package com.app.backend.constant;


public class RedisKey {
    private static final String ROOM_TYPE_PREFIX = "room_type:";
    private static final String HOUSE_PREFIX = "house:";
    private static final String MANAGER_GROUP_PREFIX = "manager_group:";
    private static final String DEVICE_CATEGORY_PREFIX = "device_category:";
    private static final String DEVICE_TYPE_PREFIX = "device_type:";
    private static final String MANUFACTURER_PREFIX = "manufacturer:";
    private static final String DEVICE_MODEL_PREFIX = "device_model:";
    private static final String ROLE_PREFIX = "role:";
    private static final String USER_PREFIX = "user:";


    public static String roomTypeAll() {
        return ROOM_TYPE_PREFIX + "all";
    }

    public static String roomTypeById(Integer id) {
        return ROOM_TYPE_PREFIX + "id:" + id;
    }

    public static String houseAll() {
        return HOUSE_PREFIX + "all";
    }

    public static String houseById(Integer id) {
        return HOUSE_PREFIX + "id:" + id;
    }

    public static String managerGroupAll() {
        return MANAGER_GROUP_PREFIX + "all";
    }

    public static String managerGroupById(Integer id) {
        return MANAGER_GROUP_PREFIX + "id:" + id;
    }

    public static String deviceCategoryAll() {
        return DEVICE_CATEGORY_PREFIX + "all";
    }

    public static String deviceCategoryById(Integer id) {
        return DEVICE_CATEGORY_PREFIX + "id:" + id;
    }

    public static String deviceTypeAll() {
        return DEVICE_TYPE_PREFIX + "all";
    }

    public static String deviceTypeById(Integer id) {
        return DEVICE_TYPE_PREFIX + "id:" + id;
    }

    public static String manufacturerAll() {
        return MANUFACTURER_PREFIX + "all";
    }

    public static String manufacturerById(Integer id) {
        return MANUFACTURER_PREFIX + "id:" + id;
    }

    public static String deviceModelAll() {
        return DEVICE_MODEL_PREFIX + "all";
    }

    public static String deviceModelById(Integer id) {
        return DEVICE_MODEL_PREFIX + "id:" + id;
    }

    public static String roleAll() {
        return ROLE_PREFIX + "all";
    }

    public static String roleById(Integer id) {
        return ROLE_PREFIX + "id:" + id;
    }

    public static String userProfileById(Integer id) {
        return USER_PREFIX + "profile:" + id;
    }

    public static String userOtpByEmail(String email) {
        return USER_PREFIX + "otp:" + email;
    }
}
