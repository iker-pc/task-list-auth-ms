
package com.taskhero.authentication.interceptor;

import com.google.gson.Gson;
import java.util.Base64;
import com.taskhero.authentication.model.User;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import com.auth0.jwk.UrlJwkProvider;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.Jwk;

import java.security.interfaces.RSAPublicKey;
import java.util.Calendar;

@Component
public class RestInterceptorAll extends HandlerInterceptorAdapter {

    private static final String TOKEN_PREFIX = "Bearer ";
    private static final String HEADER_STRING = "Authorization";
    private static final String AUTH0_APP_URL = "https://dev-310e2bq6.eu.auth0.com/";

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) throws Exception {
        /**
         * Wrap the logic of this method in a try/catch
         * If this method fails then we know that something is wrong with the "access_token"
         */
        try {

            String token = req.getHeader(HEADER_STRING);
            JwkProvider provider = new UrlJwkProvider(AUTH0_APP_URL);
            DecodedJWT jwt = JWT.decode(token.replace(TOKEN_PREFIX, ""));
            Jwk jwk = provider.get(jwt.getKeyId());
            Algorithm algorithm = Algorithm.RSA256((RSAPublicKey) jwk.getPublicKey(), null);
            algorithm.verify(jwt);

            // Check expiration
            if (jwt.getExpiresAt().before(Calendar.getInstance().getTime())) {
              return false;
            }

            byte[] decodedBytes = Base64.getDecoder().decode(jwt.getPayload());
            String decodedString = new String(decodedBytes);

            User user = new Gson().fromJson(decodedString, User.class);

            req.getSession().setAttribute("user", user);
            return super.preHandle(req, res, handler);

        } catch (Exception e) {
            System.out.println(e.toString());
            // Users "access_token" is wrong so we should notify them that they're unauthorized (401)
            res.setStatus(401, "401 Unauthorized");
            // Return "false" so the "ValidateController" method isn't called
            return false;
        }
    }
}
