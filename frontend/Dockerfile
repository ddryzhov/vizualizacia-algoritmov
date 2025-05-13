# Stage 1: Build the React application
FROM node:20-alpine AS build
# Set working directory inside the build container
WORKDIR /app
# Copy the rest of the source code
COPY . .
# Install dependencies without modifying package-lock.json
RUN npm ci --legacy-peer-deps
# Build the production-ready static files
RUN npm run build

# Stage 2: Serve the built files with Nginx
FROM nginx:alpine
# Copy build artifacts from the build stage
COPY --from=build /app/build /usr/share/nginx/html
# Expose port 80 for HTTP traffic
EXPOSE 80
# Start Nginx in the foreground
CMD ["nginx","-g","daemon off;"]
