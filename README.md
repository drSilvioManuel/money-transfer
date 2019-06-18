# Money-transfer service


## Installation



```bash
mvn clean compile

```

## Usage

```bash
# run server
mvn exec:java

# add account
curl -X POST -i -H "Content-Type: application/json" -d '{"type":3,"money":125.8}' http://localhost:8080/v1/accounts/add

# update account
curl -X PUT -i -H "Content-Type: application/json" -d '{"type":0,"money":70}' http://localhost:8080/v1/accounts/3/update

# transfer money from one account to another
curl -X PUT -i -H "Content-Type: application/json" -d '{"type":0,"money":10}' http://localhost:8080/v1/accounts/4/transfer/6
```
