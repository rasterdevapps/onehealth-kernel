package com.onehealth.gateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class TenantPropagationFilter implements GlobalFilter, Ordered {

    private static final Logger log = LoggerFactory.getLogger(TenantPropagationFilter.class);
    private static final String TENANT_CLAIM = "tenant_id";
    private static final String TENANT_HEADER = "X-Tenant-ID";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .flatMap(auth -> {
                    if (auth instanceof JwtAuthenticationToken jwtAuth) {
                        String tenantId = jwtAuth.getToken().getClaimAsString(TENANT_CLAIM);
                        if (tenantId != null && !tenantId.isBlank()) {
                            log.debug("Propagating tenant_id '{}' as X-Tenant-ID header", tenantId);
                            ServerWebExchange mutated = exchange.mutate()
                                    .request(r -> r.header(TENANT_HEADER, tenantId))
                                    .build();
                            return chain.filter(mutated);
                        }
                        log.warn("JWT is missing 'tenant_id' claim; request will be forwarded without X-Tenant-ID");
                    }
                    return chain.filter(exchange);
                })
                .switchIfEmpty(chain.filter(exchange));
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE - 10;
    }
}
