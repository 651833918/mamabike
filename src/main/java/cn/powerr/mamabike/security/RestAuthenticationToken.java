package cn.powerr.mamabike.security;

import cn.powerr.mamabike.user.entity.UserElement;
import lombok.Data;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Data
public class RestAuthenticationToken extends AbstractAuthenticationToken {
    public RestAuthenticationToken(Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
    }

    private UserElement user;

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }
}
