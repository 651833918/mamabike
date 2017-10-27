package cn.powerr.mamabike.security;

import cn.powerr.mamabike.cache.CommonCacheUtil;
import cn.powerr.mamabike.common.constant.Constants;
import cn.powerr.mamabike.user.entity.UserElement;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.util.AntPathMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class RestPreAuthenticatedProcessingFilter extends AbstractPreAuthenticatedProcessingFilter {

    /**
     * spring的路径匹配器
     */
    private AntPathMatcher matcher = new AntPathMatcher();

    private List<String> noneSecurityList;

    private CommonCacheUtil commonCacheUtil;

    public RestPreAuthenticatedProcessingFilter(List<String> noneSecurityList, CommonCacheUtil commonCacheUtil) {
        this.noneSecurityList = noneSecurityList;
        this.commonCacheUtil = commonCacheUtil;
    }

    /**
     * 获取用户信息,返回Object到RestAuthenticationProvider的supports方法
     *
     * @param request
     * @return
     */
    @Override
    protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
        List<GrantedAuthority> authorities = null;
        //无需授权的uri
        if (isNoneSecurity(request.getRequestURI().toString()) || "OPTIONS".equals(request.getMethod())) {
            GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_SOME");
            authorities = new ArrayList<>();
            authorities.add(authority);
            return new RestAuthenticationToken(authorities);
        }
        //检查app版本
        String version = request.getHeader(Constants.REQUEST_VERSION_KEY);
        String token = request.getHeader(Constants.REQUEST_TOKEN_KEY);
        if (version == null) {
            request.setAttribute("header-error", 400);
        }
        //检查token
        if (request.getAttribute("header-error") == null) {
            try {
                if (!StringUtils.isBlank(token)) {
                    UserElement ue = commonCacheUtil.getUserByToken(token);
                    if (ue instanceof UserElement) {
                        //检查到token说明用户已经登录 授权给用户BIKE_CLIENT角色 允许访问
                        GrantedAuthority authority = new SimpleGrantedAuthority("BIKE_CLIENT");
                        authorities = new ArrayList<>();
                        authorities.add(authority);
                        RestAuthenticationToken authToken = new RestAuthenticationToken(authorities);
                        authToken.setUser(ue);
                        return authToken;
                    } else {
                        request.setAttribute("header-error", 401);
                    }
                } else {
                    log.warn("Got no token form request header");
                    //token不存在告诉移动端
                    request.setAttribute("header-error", 401);
                }
            } catch (Exception e) {
                log.error("Fail to authenticate user", e);
            }
        }
        if (request.getAttribute("header-error") != null) {
            GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_NONE");
            authorities = new ArrayList<>();
            authorities.add(authority);
        }

        return new RestAuthenticationToken(authorities);
    }

    private boolean isNoneSecurity(String uri) {
        boolean result = false;
        if (this.noneSecurityList != null) {
            for (String pattern : this.noneSecurityList) {
                if (matcher.match(pattern, uri)) {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }


    @Override
    protected Object getPreAuthenticatedCredentials(HttpServletRequest httpServletRequest) {
        return null;
    }
}
