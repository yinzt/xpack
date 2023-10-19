package cn.com.xcsa.xpack.third.client;

import cn.com.xcsa.api.dto.ThirdDeptDto;
import cn.com.xcsa.api.dto.ThirdUserDto;
import cn.com.xcsa.xpack.third.AccessToken;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DingtalkApiClient implements ApiClient{


    private static final String DING_TALK_URL = "https://oapi.dingtalk.com";

    private static final String ACCESS_TOKEN_URL = "/v1.0/oauth2/accessToken";


    @Override
    public AccessToken getAccessToken(String appKey, String appSecret) {
        String url = DING_TALK_URL + ACCESS_TOKEN_URL;
        Map<String,Object> body = new HashMap<>();
        body.put("appKey", appKey);
        body.put("appSecret", appSecret);
        HttpResponse response = HttpUtil.createRequest(Method.POST, url)
                .body(JSON.toJSONString(body)).executeAsync();
        if (!response.isOk()){
            return null;
        }
        JSONObject obj = JSON.parseObject(response.bodyBytes());
        return new AccessToken(obj.getString("accessToken"),obj.getInteger("expireIn"));
    }

    @Override
    public List<ThirdUserDto> userList(AccessToken accessToken, String deptId) {
        return null;
    }

    @Override
    public List<ThirdDeptDto> deptList(AccessToken accessToken) {
        return null;
    }
}
