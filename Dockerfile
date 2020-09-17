FROM openjdk:8-jre-alpine
WORKDIR root/
COPY build/install ./

EXPOSE 8080
CMD /root/movies-extractor-telegram-bot/bin/movies-extractor-telegram-bot