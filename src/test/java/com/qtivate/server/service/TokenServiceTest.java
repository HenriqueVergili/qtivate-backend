package com.qtivate.server.service;

import com.qtivate.server.exceptions.ExpiredTokenException;
import com.qtivate.server.exceptions.InvalidTokenException;
import com.qtivate.server.model.Token;
import com.qtivate.server.respository.TokenRepository;
import org.bson.BsonTimestamp;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Date;

import static com.mongodb.internal.connection.tlschannel.util.Util.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TokenServiceTest {
    @Mock
    private TokenRepository tokenRepository;

    @InjectMocks
    private TokenService tokenService;

    @Test(expected = ExpiredTokenException.class)
    public void shouldThrowInvalidTokenIfExpired() throws Exception {
        Date oldDate = new Date();
        oldDate.setTime(0);
        String tokenString = "puc:123,456:testing";
        Token token = new Token (tokenString, oldDate);
        tokenRepository.insert(token);
        tokenService.isTokenValid(tokenString);
    }

    @Test(expected = ExpiredTokenException.class)
    public void shouldThrowInvalidTokenIfDoesntExist() throws Exception {
        tokenService.isTokenValid("puc:123:lorem");
    }

    @Test(expected = InvalidTokenException.class)
    public void shouldThrowBadRequestIfTokenMalFormated() throws Exception {
        tokenService.isTokenValid("lorem");
    }

    @Test
    public void shouldReturnTrueIfTokenValid() throws Exception {
        BsonTimestamp dateNow = new BsonTimestamp(new Date().getTime());
        String tokenString = "puc:123,456:testing";
        Token token = new Token (tokenString, new Date());
        when(tokenRepository.findTokenByString(tokenString)).thenReturn(token);
        boolean isValid = tokenService.isTokenValid(tokenString);
        assertTrue(isValid);
    }
}
