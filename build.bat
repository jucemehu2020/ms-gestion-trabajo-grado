@echo off
set SPRING_DATASOURCE_URL=jdbc:mysql://dockertest.unicauca.edu.co:4207/computacion
set SPRING_DATASOURCE_USERNAME=computacion
set SPRING_DATASOURCE_PASSWORD=DB-C0mput4c10N
set SPRING_JWT_KEY=SecretKey
set SPRING_JWT_EXPIRATION=86400000
set SPRING_MAIL_SMTP=smtp.gmail.com
set SPRING_MAIL_PORT=587
set SPRING_MAIL_USERNAME=maestriaprueba2024@gmail.com
set SPRING_MAIL_PASSWORD=qawl sbgr mawn dzkm
set SPRING_SERVER_TRABAJO_GRADO_PORT=8083

mvn clean package
