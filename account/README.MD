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
$> docker run -td --name kapua-sql -p 8181:8181 -p 3306:3306 kapua/kapua-sql
```

5 - Run an instance of the broker component

```
$> docker run -td --name kapua-broker --link kapua-sql:db --link kapua-elasticsearch:es -p 1883:1883 -p
```

6 - Open a new terminal window

7 - Under kapua root folder move to the account service folder

```
$> cd ./account/uservice
```

8 - Run the account server instance

```
$> vertx run -cp target/kapua-account-uservice-0.3.0-SNAPSHOT.jar org.eclipse.kapua.account.service.uservice.server.AccountsServer -Dport=8182
```

9 - Open a new terminal window

10 - Under kapua root folder move to the auth service folder

```
$> ./auth/uservice
```

11 - Run the auth server instance

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
