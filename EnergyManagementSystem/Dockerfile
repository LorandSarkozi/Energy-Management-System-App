# Build phase
FROM node:16 AS build 
WORKDIR /app

# Copy package.json and package-lock.json (if present)
COPY package*.json ./

# Install dependencies
RUN npm install

# Copy the rest of the application code
COPY . .

# Build the Angular application (use production configuration)
RUN npm run build --configuration=production

# Production phase
FROM nginx:alpine

# Copy the build output to Nginx's default folder
COPY --from=build /app/dist/energy-management-system /usr/share/nginx/html

# Expose port 80 for the application
EXPOSE 80

# Start nginx server in the foreground
CMD ["nginx", "-g", "daemon off;"]
