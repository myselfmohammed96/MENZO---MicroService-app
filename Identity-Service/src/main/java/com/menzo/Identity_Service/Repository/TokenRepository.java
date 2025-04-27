package com.menzo.Identity_Service.Repository;

import com.menzo.Identity_Service.Entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Integer> {

    public Optional<Token> findByToken(String token);

    public Optional<Token> findByUserId(Integer userId);

    @Query(nativeQuery = true, value = "SELECT * FROM tokens WHERE user_id = :id AND is_logged_out = false")
    public List<Token> findAllTokensByUser(Long id);
}
