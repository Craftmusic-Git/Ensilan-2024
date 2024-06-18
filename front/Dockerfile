FROM node:20.14.0

WORKDIR /usr/src/app

COPY . /usr/src/app

RUN npm install -g @angular/cli

RUN npm install

ENV API_URL=http://localhost:8080

CMD ["ng", "serve", "--host", "0.0.0.0"]
