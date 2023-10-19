package cn.com.xcsa.xpack.third.client;


import cn.com.xcsa.api.dto.ThirdDeptDto;
import cn.com.xcsa.api.dto.ThirdUserDto;
import cn.com.xcsa.xpack.third.AccessToken;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wuhui
 */
@Slf4j
public class WeixinApiClient implements ApiClient {

    private static final String QIYE_WEIXIN_URL = "https://qyapi.weixin.qq.com/cgi-bin";
    private static final String ACCESS_TOKEN_URL = "/gettoken?";
    private static final String DEPARTMENT_LIST_URL = "/department/list?";
    private static final String USER_LIST_URL = "/user/list?";


    @Override
    public AccessToken getAccessToken(String corpId, String corpSecret) {
        String url = QIYE_WEIXIN_URL + ACCESS_TOKEN_URL + "corpid=" + corpId + "&corpsecret=" + corpSecret;
        HttpResponse response = HttpUtil.createRequest(Method.GET, url).executeAsync();
        if (!response.isOk()) {
            return null;
        }
        JSONObject obj = JSON.parseObject(response.bodyBytes());
        if (obj.getInteger("errcode") == 0) {
            return new AccessToken(obj.getString("access_token"),obj.getInteger("expires_in"));
        }
        return null;
    }



    public List<ThirdDeptDto> deptList(AccessToken accessToken) {
        String url = QIYE_WEIXIN_URL + DEPARTMENT_LIST_URL + "access_token="
                + accessToken.getAccessToken();
        HttpResponse response = HttpUtil.createRequest(Method.GET, url).executeAsync();
        if (!response.isOk()) {
            return null;
        }
        JSONObject obj = JSON.parseObject(response.bodyBytes());
        List<ThirdDeptDto> tds = new ArrayList<>();
        if (obj.getInteger("errcode") != 0) {
            log.error("Get weixin deptList error result={}",obj);
        }
        JSONArray depts = obj.getJSONArray("department");
        for (int i = 0; i < depts.size(); i++) {
            JSONObject o = depts.getJSONObject(i);
            ThirdDeptDto dept = new ThirdDeptDto();
            dept.setDeptId(o.getString("id"));
            dept.setName(o.getString("name"));
            dept.setEnName(o.getString("name_en"));
            dept.setParentId(o.getString("parentid"));
            dept.setOrder(o.getInteger("order"));
            dept.setLeaders(o.getList("department_leader",String.class));
            tds.add(dept);
        }
        return tds;
    }



    public List<ThirdUserDto> userList(AccessToken accessToken, String deptId) {
        String url = QIYE_WEIXIN_URL + USER_LIST_URL + "access_token="
                + accessToken.getAccessToken() + "&department_id=" + deptId;
        HttpResponse response = HttpUtil.createRequest(Method.GET, url).executeAsync();
        if (!response.isOk()) {
            return null;
        }
        JSONObject obj = JSON.parseObject(response.bodyBytes());
        List<ThirdUserDto> tus = new ArrayList<>();
        if (obj.getInteger("errcode") != 0) {
            log.error("Get weixin userList error result={}",obj);
        }
        JSONArray depts = obj.getJSONArray("userlist");
        for (int i = 0; i < depts.size(); i++) {
            JSONObject o = depts.getJSONObject(i);
            ThirdUserDto tu = new ThirdUserDto();
            tu.setUserId(o.getString("userid"));
            tu.setName(o.getString("name"));
            tu.setPhone(o.getString("mobile"));
            tu.setLoginName(o.getString("userid"));
            tu.setEmail(o.getString("email"));
            tu.setDepartments(o.getList("department",String.class));
            tus.add(tu);
        }
        return tus;
    }



}
