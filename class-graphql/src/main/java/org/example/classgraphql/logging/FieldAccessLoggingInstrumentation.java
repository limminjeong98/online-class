package org.example.classgraphql.logging;

import graphql.execution.instrumentation.FieldFetchingInstrumentationContext;
import graphql.execution.instrumentation.Instrumentation;
import graphql.execution.instrumentation.InstrumentationContext;
import graphql.execution.instrumentation.InstrumentationState;
import graphql.execution.instrumentation.parameters.InstrumentationFieldFetchParameters;
import graphql.execution.instrumentation.parameters.InstrumentationFieldParameters;
import graphql.schema.DataFetchingEnvironment;
import graphql.schema.GraphQLOutputType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class FieldAccessLoggingInstrumentation implements Instrumentation {


    @Override
    public InstrumentationContext<Object> beginFieldFetch(InstrumentationFieldFetchParameters parameters, InstrumentationState state) {
        DataFetchingEnvironment env = parameters.getEnvironment();
        String fieldName = env.getField().getName();
        GraphQLOutputType parentTypeName = env.getExecutionStepInfo().getParent().getType();

        return new InstrumentationContext<>() {
            @Override
            public void onDispatched() {
                log.info("Fetching field: {}, parentType: {}", fieldName, parentTypeName);
            }

            @Override
            public void onCompleted(Object result, Throwable t) {
                log.info("Fetching field: {}, parentType: {}", fieldName, parentTypeName);
            }
        };
    }
}
