# Deployment Guide

## Prerequisites

- Java 17 or higher
- Maven 3.8.1 or higher
- Git
- (Optional) Docker

## Local Development Deployment

### 1. Clone Repository

```bash
git clone https://github.com/marvin54-hub/Car_Wash_System.git
cd Car_Wash_System
```

### 2. Configure Environment

```bash
cp .env.example .env
# Edit .env with your configuration
```

### 3. Build Application

```bash
mvn clean package -DskipTests
```

### 4. Run Application

```bash
mvn spring-boot:run
```

Or using the JAR:

```bash
java -jar target/carwash-system-1.0.0.jar
```

### 5. Access Application

- **Customer Portal:** http://localhost:8080
- **Admin Dashboard:** http://localhost:8080/admin/dashboard
- **H2 Console:** http://localhost:8080/h2-console

## Production Deployment

### 1. Build Optimized JAR

```bash
mvn clean package -DskipTests -Dmaven.test.skip=true
```

### 2. Create Dockerfile

```dockerfile
FROM openjdk:17-slim

WORKDIR /app

COPY target/carwash-system-1.0.0.jar .

EXPOSE 8080

CMD ["java", "-jar", "carwash-system-1.0.0.jar"]
```

### 3. Build Docker Image

```bash
docker build -t car-wash-system:1.0.0 .
```

### 4. Run Docker Container

```bash
docker run -d \
  -e SPRING_MAIL_USERNAME=your-email@gmail.com \
  -e SPRING_MAIL_PASSWORD=your-app-password \
  -e SPRING_DATASOURCE_PASSWORD=your-db-password \
  -p 8080:8080 \
  -v carwash-data:/app/data \
  car-wash-system:1.0.0
```

### 5. Docker Compose Setup

**docker-compose.yml:**

```yaml
version: '3.8'

services:
  car-wash-app:
    build: .
    container_name: car-wash-system
    ports:
      - "8080:8080"
    environment:
      SPRING_MAIL_USERNAME: ${SPRING_MAIL_USERNAME}
      SPRING_MAIL_PASSWORD: ${SPRING_MAIL_PASSWORD}
      SPRING_DATASOURCE_PASSWORD: ${SPRING_DATASOURCE_PASSWORD}
      SERVER_PORT: 8080
    volumes:
      - carwash-data:/app/data
    restart: unless-stopped

volumes:
  carwash-data:
```

Run with:

```bash
docker-compose up -d
```

## Cloud Deployment

### Heroku

1. **Create Procfile:**
   ```
   web: java -Dserver.port=$PORT $JAVA_OPTS -jar target/carwash-system-1.0.0.jar
   ```

2. **Deploy:**
   ```bash
   heroku create car-wash-system
   git push heroku main
   ```

### AWS Elastic Beanstalk

1. **Install EB CLI:**
   ```bash
   pip install awsebcli
   ```

2. **Initialize EB:**
   ```bash
   eb init -p java-17 car-wash-system
   ```

3. **Deploy:**
   ```bash
   eb create
   eb deploy
   ```

### Google Cloud App Engine

1. **Create app.yaml:**
   ```yaml
   runtime: java17
   env: standard
   entrypoint: "java -jar target/carwash-system-1.0.0.jar"
   ```

2. **Deploy:**
   ```bash
   gcloud app deploy
   ```

## Environment Variables

**Required for Production:**

```env
SPRING_MAIL_USERNAME=production-email@gmail.com
SPRING_MAIL_PASSWORD=production-app-password
SPRING_DATASOURCE_PASSWORD=production-db-password
SERVER_PORT=8080
SPRING_PROFILES_ACTIVE=prod
```

## Database Migration (H2 to PostgreSQL)

For production, consider migrating from H2 to PostgreSQL:

1. **Update pom.xml:**
   ```xml
   <dependency>
       <groupId>org.postgresql</groupId>
       <artifactId>postgresql</artifactId>
       <scope>runtime</scope>
   </dependency>
   ```

2. **Update application.properties:**
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/carwash
   spring.datasource.driverClassName=org.postgresql.Driver
   spring.jpa.database-platform=org.hibernate.dialect.PostgreSQL10Dialect
   ```

## Monitoring & Logging

### Application Logs

```bash
# View logs from Docker container
docker logs -f car-wash-system

# View logs from JAR
tail -f logs/application.log
```

### Health Check

```bash
curl http://localhost:8080/actuator/health
```

### Metrics

```bash
curl http://localhost:8080/actuator/metrics
```

## Backup Strategy

### Database Backup

```bash
# Backup H2 database
cp ./data/carwashdb.mv.db ./backups/carwashdb_$(date +%Y%m%d_%H%M%S).mv.db

# Or using cron for automated backups
0 2 * * * cp /app/data/carwashdb.mv.db /backups/carwashdb_$(date +\%Y\%m\%d).mv.db
```

### Application Rollback

```bash
# Tag releases
git tag -a v1.0.0 -m "Release version 1.0.0"
git push origin v1.0.0

# Rollback to previous version
git checkout v0.9.0
mvn clean package
```

## Performance Tuning

### JVM Tuning

```bash
java -Xms256m -Xmx512m -jar carwash-system-1.0.0.jar
```

### Database Optimization

- Create indexes on frequently queried columns
- Run periodic vacuum/analyze on PostgreSQL
- Monitor query performance

## Troubleshooting

### Port Already in Use

```bash
# Find process using port 8080
lsof -i :8080

# Kill process
kill -9 <PID>
```

### Database Connection Failed

```bash
# Check database credentials
# Verify database server is running
# Check firewall rules
```

### Email Not Sending

```bash
# Verify Gmail credentials
# Check App Password is correct (16 chars, no spaces)
# Enable 2-Factor Authentication on Gmail
# Check Gmail allows "Less Secure App Access" if needed
```

## Maintenance

### Update Dependencies

```bash
mvn versions:display-dependency-updates
mvn versions:use-latest-releases
mvn clean test
```

### Clean Up Old Logs

```bash
# Keep only last 30 days
find ./logs -name "*.log" -mtime +30 -delete
```
