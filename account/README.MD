## Running Kapua Account Service as a stand alone server


** Note: This is not an official Eclipse Kapua feature **

To run the account service as a stand alone micro service do the following:

Pre-requisites:
- Docker runtime installed
- Vertex runtime installed (Optional)

### Startup the services

1 - Open a new terminal window
2 - Build Eclipse Kapua skipping the tests

```
$> mvn clean install -Dmaven.test.skip=true -Pdocker
```

3 - Open a new terminal window

4 - Run an instance of the database component

```
docker run -td --name kapua-sql -p 8181:8181 -p 3306:3306 kapua/kapua-sql
docker run -td --name kapua-elasticsearch -p 9200:9200 -p 9300:9300 elasticsearch:5.4.0 -Ecluster.name=kapua-datastore -Ediscovery.type=single-node -Etransport.host=_site_ -Etransport.ping_schedule=-1 -Etransport.tcp.connect_timeout=30s
docker run -td --name kapua-broker --link kapua-sql:db --link kapua-elasticsearch:es -p 1883:1883 -p 61614:61614 kapua/kapua-broker
```

5 - Open a new terminal window

6 - Under kapua root folder move to the account service folder

```
$> cd ./account/uservice
```

7 - Run the account server instance

```
$> vertx run -cp target/kapua-account-uservice-0.3.0-SNAPSHOT.jar org.eclipse.kapua.account.service.uservice.server.AccountsServer -Dport=8182
```

8 - Open a new terminal window

9 - Under kapua root folder move to the auth service folder

```
$> ./auth/uservice
```

10 - Run the auth server instance

```
$> vertx run -cp ./target/kapua-auth-uservice-0.3.0-SNAPSHOT.jar org.eclipse.kapua.auth.service.uservice.server.AuthServer -Dport=8183
```

### Interact with services

1 - Get a security token:

```
curl -X POST \
  http://localhost:8183/authentication/user \
  -H 'accept: application/json' \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -d '{
  "username": "kapua-sys",
  "password": [
    "kapua-password"
  ]
}'
```

Copy the security token from the response (the security token is the content of the field tokenId in the response)

2 - Send request the list of the sub accounts of the "kapua-sys" account (replace the string token-goes-here with the token id copied at the previous step)

```
curl -X GET \
  http://localhost:8182/AQ/accounts \
  -H 'accept: application/json' \
  -H 'authorization: token-goes-here' \
  -H 'cache-control: no-cache'
```
