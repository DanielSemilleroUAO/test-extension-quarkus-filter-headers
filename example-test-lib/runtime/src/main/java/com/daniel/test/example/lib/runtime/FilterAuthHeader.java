package com.daniel.test.example.lib.runtime;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import org.jboss.resteasy.reactive.server.ServerRequestFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Provider
@ApplicationScoped
public class FilterAuthHeader {

    private static final Logger LOGGER = LoggerFactory.getLogger(FilterAuthHeader.class.getName());

    public static final String AUTH_HEADER = "X-Header-Custom";
    public static final String PATH_HEALTH = "/health";
    public static final String API_KEY_SECRET = "tu-secreto";

    @ServerRequestFilter(preMatching = true, priority = 1)
    public Uni<Response> filterRequestHeaders(ContainerRequestContext request) {
        if (!request.getUriInfo().getPath().contains(PATH_HEALTH)) {
            LOGGER.info("X-AUTH FILTER: {}", request.getUriInfo().getRequestUri().getPath());
            String apiKey = request.getHeaderString(AUTH_HEADER);
            if (apiKey == null) {
                LOGGER.info("FILTER [AUTH-TOKEN] Empty X-Header-Custom");
                return abortRequestBaseUni("X-Header-Custom not send", Response.Status.UNAUTHORIZED);
            } else if (!apiKey.equals(API_KEY_SECRET)) {
                LOGGER.info("FILTER [AUTH-TOKEN] X-Header-Custom not valid");
                return abortRequestBaseUni("X-Header-Custom not valid", Response.Status.UNAUTHORIZED);
            }
        }
        return Uni.createFrom().nullItem();
    }

    public static Uni<Response> abortRequestBaseUni(String message, Response.Status status) {
        return Uni.createFrom().item(abortRequestBase(message, status));
    }

    public static Response abortRequestBase(String message, Response.Status status) {
        return Response.status(status).entity(message).build();
    }

}
