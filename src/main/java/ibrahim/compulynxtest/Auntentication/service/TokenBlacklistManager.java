package ibrahim.compulynxtest.Auntentication.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class TokenBlacklistManager {

    private final Set<String> blacklisttoken = ConcurrentHashMap.newKeySet();


    public void invalidateToken(String token ){
        blacklisttoken.add(token);
    }

    public boolean isTokenBlacklisted(String token){
        return blacklisttoken.contains(token);
    }




}
