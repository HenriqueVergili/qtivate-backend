package com.qtivate.server.service;

import com.qtivate.server.exceptions.ExpiredTokenException;
import com.qtivate.server.exceptions.InvalidTokenException;
import com.qtivate.server.model.Token;
import com.qtivate.server.respository.TokenRepository;
import org.bson.BsonTimestamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

@Service
public class TokenService {
    @Autowired
    private TokenRepository tokenRepository;


    public int addToken(String t) {
        try {
            Token token = new Token (t, Calendar.getInstance().getTime());
            tokenRepository.insert(token);
        } catch (Exception error) {
            System.err.println(error.getMessage());
            return 1;
        }
        return 0;
    }

    public int addManyTokens(String[] keys) {
        try {
            // BsonTimestamp end = new BsonTimestamp(new BsonTimestamp().getTime() + 8);
            Calendar start = Calendar.getInstance();
            for (String key : keys) {
                Token token = new Token(key, start.getTime());
                start.add(Calendar.SECOND, 5);
                tokenRepository.insert(token);
            }
        } catch (Exception error) {
            System.err.println(error.getMessage());
            return 1;
        }
        return 0;
    }
    public boolean isTokenValid(String t) throws Exception {
        String[] data = t.split(":");
        if (data.length != 3) throw new InvalidTokenException("Token mal formatado");

        String prefixo = data[0];
        String[] classes = data[1].split(",");
        String tokenString = data[2];
        Token token = tokenExists(t);
        return token.isValid();
    }

    private Token tokenExists(String tokenString) {
        try {
            Token token = tokenRepository.findTokenByString(tokenString);
            if (token == null) throw new Exception();

            return token;
        }catch (Exception e) {
            throw new ExpiredTokenException("Token inválido");
        }
    }
}
