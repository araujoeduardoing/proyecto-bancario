# Imagen base de Node.js
FROM node:20-alpine

# Forzar uso de IPv4
ENV NODE_OPTIONS=--dns-result-order=ipv4first

WORKDIR /app

# Copiar archivos de dependencias
COPY package*.json ./

# Instalar dependencias
RUN npm ci

# Copiar el código fuente
COPY . .

# Exponer el puerto 4201 
EXPOSE 4201

# Comando por defecto para iniciar el servidor de desarrollo
CMD ["npm", "start"]
