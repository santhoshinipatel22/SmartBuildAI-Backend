# SmartBuild Mini — Backend API (Spring Boot)

Spring Boot 3.5.5 REST API backend for the SmartBuild Mini Construction Material Management System.

## Deploying to Render

1. Create a new **Web Service** on [Render](https://render.com).
2. Connect your repository: `https://github.com/santhoshinipatel22/TT_backend.git`
3. Setting option A (Using Docker - Recommended):
   - **Environment**: Docker
   - Render will automatically detect the `Dockerfile` and build it.
4. Setting option B (Native Java):
   - **Environment**: Java
   - **Build Command**: `./mvnw clean package -DskipTests`
   - **Start Command**: `java -jar target/smartbuild-backend-0.0.1-SNAPSHOT.jar`
5. **Environment Variables on Render**:
   - `DB_URL`: JDBC connection string to your MySQL database (e.g. `jdbc:mysql://<host>:<port>/<dbname>?useSSL=true&serverTimezone=UTC`)
   - `DB_USERNAME`: MySQL user
   - `DB_PASSWORD`: MySQL password
   - `PORT`: 8080 (Render provides this automatically)
