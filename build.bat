cls
docker compose down

cd frontend
CALL npm run build
cd ..

rmdir /S /Q nginx\dist
xcopy /E /I /Y frontend\dist nginx\dist


CALL mvn -f GameService clean package -DskipTests
CALL mvn -f AuthService clean package -DskipTests
CALL mvn -f APIGateway clean package -DskipTests
CALL mvn -f UserService clean package -DskipTests

docker compose up --build