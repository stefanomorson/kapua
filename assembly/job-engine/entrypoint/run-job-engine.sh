#!/bin/sh
java -Dcommons.db.schema.update=true \
     -Dcommons.db.connection.host=${SQL_DB_ADDR} \
     -Dcommons.db.connection.port=${SQL_DB_PORT} \
     -Dcommons.eventbus.url=${SERVICE_BROKER_ADDR} \
     -jar /kapua-service-app-job-engine-*.jar