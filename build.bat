cls
docker compose down

CALL mvn -f GameService clean package -DskipTests
CALL mvn -f AuthService clean package -DskipTests
CALL mvn -f APIGateway clean package -DskipTests
CALL mvn -f UserService clean package -DskipTests

docker compose up --build