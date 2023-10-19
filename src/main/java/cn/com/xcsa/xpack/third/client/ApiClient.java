package cn.com.xcsa.xpack.third.client;

import cn.com.xcsa.api.dto.ThirdDeptDto;
import cn.com.xcsa.api.dto.ThirdUserDto;
import cn.com.xcsa.xpack.third.AccessToken;

import java.util.List;

public interface ApiClient {

    AccessToken getAccessToken(String appKey, String appSecret);

    List<ThirdUserDto> userList(AccessToken accessToken, String deptId);

    List<ThirdDeptDto> deptList(AccessToken accessToken);
}
