package cn.powerr.mamabike.user.entity;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class UserElement {
    private Long userId;
    private String mobile;
    private String token;
    private String platform;
    private String pushUserId;
    private String pushChannelId;

    /**
     * 转map
     *
     * @return
     */
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("platform", platform);
        map.put("userId", userId + "");
        map.put("token", token);
        if (pushUserId != null) {
            map.put("pushUserId", pushUserId);
        }
        if (pushChannelId != null) {
            map.put("pushChannelId", pushChannelId);
        }
        return map;
    }

    /**
     * map转对象
     * @param map
     * @return
     */
    public static UserElement fromMap(Map<String, String> map) {
        UserElement ue = new UserElement();
        ue.setPlatform(map.get("platform"));
        ue.setToken(map.get("token"));
        ue.setMobile(map.get("mobile"));
        ue.setUserId(Long.parseLong(map.get("userId")));
        ue.setPushUserId(map.get("pushUserId"));
        ue.setPushChannelId(map.get("pushChannelId"));
        return ue;
    }
}
