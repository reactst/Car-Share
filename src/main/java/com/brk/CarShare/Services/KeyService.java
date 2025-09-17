package com.brk.CarShare.Services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class KeyService {
    @Value("${publicKey}")
    private String publicKey;

    public String getKey() {
        byte[] encodedPublicKey = Base64.decode(publicKey.getBytes());
        X509EncodedKeySpec spec = new X509EncodedKeySpec(encodedPublicKey);
        //KeyFactory kf = KeyFactory.getInstance("RS256");

        return publicKey;
    }
}
