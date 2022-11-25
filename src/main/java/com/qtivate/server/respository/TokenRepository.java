package com.qtivate.server.respository;

import com.qtivate.server.model.Subject;
import com.qtivate.server.model.Token;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface TokenRepository extends MongoRepository<Token, String> {
    @Query("{'key':'?0'}")
    Token findTokenByString(String key);


}
