FROM public.ecr.aws/lambda/java:21
COPY target/classes ${LAMBDA_TASK_ROOT}
COPY target/dependency/* ${LAMBDA_TASK_ROOT}/lib/
CMD [ "com.example.app.api.Handler.LambdaHandler::handleRequest"]
# FROM public.ecr.aws/lambda/java:21
# COPY target/app.jar ${LAMBDA_TASK_ROOT}/app.jar
# CMD ["com.example.app.api.Handler.LambdaHandler::handleRequest"]