FROM eclipse-temurin:21.0.10_7-jre-alpine-3.23

RUN addgroup -S appgroup && adduser -S appuser -G appgroup

USER appuser

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=5s --start-period=60s --retries=3 \
CMD wget -q -O /dev/null http://localhost:8080/actuator/health || exit 1

COPY infrastructure/build/libs/infrastructure-1.0.0.jar app.jar

ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:InitialRAMPercentage=50.0","-XX:MaxRAMPercentage=75.0", "-jar", "app.jar"]