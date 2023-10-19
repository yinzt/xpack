package cn.com.xcsa.xpack.third;

import cn.com.xcsa.api.dto.ThirdDeptDto;
import cn.com.xcsa.api.dto.ThirdUserDto;
import cn.com.xcsa.api.exception.ApiRuntimeException;
import cn.com.xcsa.api.framework.redis.RedisCache;
import cn.com.xcsa.api.util.InfoCode;
import cn.com.xcsa.api.xpack.AgentApi;
import cn.com.xcsa.api.xpack.AgentType;
import cn.com.xcsa.api.xpack.agent.AgentInfo;
import cn.com.xcsa.xpack.third.client.ApiClient;
import cn.com.xcsa.xpack.third.client.DingtalkApiClient;
import cn.com.xcsa.xpack.third.client.WeixinApiClient;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;


@Component
public class AgentApiImpl implements AgentApi {

    private AgentInfo agentInfo;

    private AgentType agentType;

    @Resource
    private RedisCache redisCache;

    private static final String ACCESS_TOKEN_KEY = "wx_access_token_key_";

    @Override
    public AgentApi agentInfo(AgentInfo ai, AgentType at) {
        this.agentInfo = ai;
        this.agentType = at;
        return this;
    }

    @Override
    public List<ThirdUserDto> userList(String deptId) {
        ApiClient api = getApiClient(agentType);
        if (api == null) {
            throw new IllegalArgumentException("不支持的API");
        }
        return api.userList(getAccessToken(), deptId);
    }

    @Override
    public List<ThirdDeptDto> deptList() {
        ApiClient api = getApiClient(agentType);
        if (api == null) {
            throw new IllegalArgumentException("不支持的API");
        }
        return api.deptList(getAccessToken());
    }


    private AccessToken getAccessToken() {
        if (agentInfo == null) {
            throw new IllegalArgumentException("缺少AgentInfo信息");
        }
        String key = ACCESS_TOKEN_KEY + agentInfo.getAppKey();
        AccessToken at = redisCache.getCacheObject(key);
        if (at == null) {
            ApiClient apiClient = getApiClient(agentType);
            if (apiClient == null) {
                throw new ApiRuntimeException(InfoCode.NOT_METHOD, "不支持的类型[" + agentType + "]");
            }
            at = apiClient.getAccessToken(agentInfo.getAppKey(), agentInfo.getAppSecret());
        }
        if (at != null){
            redisCache.setCacheObject(key, at, at.getExpireIn(), TimeUnit.SECONDS);
        }
        return at;
    }


    private ApiClient getApiClient(AgentType at) {
        switch (at) {
            case QY_WEIXIN:
                return new WeixinApiClient();
            case DING_TALK:
                return new DingtalkApiClient();
        }
        return null;
    }

}
