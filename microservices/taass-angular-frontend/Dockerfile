# Versione ottimizzata per la programmazione (live-reload)
FROM node:12.13.0-alpine as build-step
WORKDIR /app
ENV PATH /app/node_modules/.bin:$PATH
COPY package.json /app/package.json
RUN npm install
COPY . /app
EXPOSE 4200
CMD ng serve --host 0.0.0.0 --poll 2000

# Versione ottimizzata per il "prodotto finito"
# FROM node:12.13.0-alpine as build-step
# WORKDIR /app
# COPY package.json ./
# RUN npm install
# COPY . .
# RUN npm run build

# FROM nginx:1.17.5-alpine as prod-stage
# COPY --from=build-step /app/dist/taass-angular-frontend /usr/share/nginx/html
# EXPOSE 80 # Attenzione al proxy nginx
# CMD ["nginx", "-g", "daemon off;"]
