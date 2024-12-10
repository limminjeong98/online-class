package org.example.classgraphql.directive;

import graphql.schema.*;
import graphql.schema.idl.SchemaDirectiveWiring;
import graphql.schema.idl.SchemaDirectiveWiringEnvironment;
import org.example.classgraphql.exception.UnauthorizedException;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationDirective implements SchemaDirectiveWiring {
    @Override
    public GraphQLFieldDefinition onField(SchemaDirectiveWiringEnvironment<GraphQLFieldDefinition> environment) {
        GraphQLFieldDefinition fieldDefinition = environment.getFieldDefinition();
        GraphQLObjectType parentType = (GraphQLObjectType) environment.getFieldsContainer();

        // 원래의 DataFetcher를 조회
        DataFetcher<?> originalDataFetcher = environment.getCodeRegistry().getDataFetcher(parentType, fieldDefinition);

        // 인증 검사를 수행하는 새로운 DataFetcher를 생성
        DataFetcher<?> authDataFetcher = (DataFetchingEnvironment datafetchingEnvironment) -> {
            String userId = datafetchingEnvironment.getGraphQlContext().get("X-USER-ID");
            if (userId == null || userId.trim().isEmpty() || userId.equals("-1")) {
                throw new UnauthorizedException("Unauthorized: Missing X-USER-ID header");
            }
            return originalDataFetcher.get(datafetchingEnvironment);
        };

        // 변경된 DataFetcher를 등록
        environment.getCodeRegistry().dataFetcher(parentType, fieldDefinition, authDataFetcher);

        return SchemaDirectiveWiring.super.onField(environment);
    }
}
