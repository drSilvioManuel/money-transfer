# Money-transfer service

The service already contains several accounts with id [0..6], 
so you can try update and transfer operations without of creation an account.

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

# deposit account
curl -X PUT -i -H "Content-Type: application/json" -d '{"type":0,"money":70}' http://localhost:8080/v1/accounts/3/update

# withdraw account
curl -X PUT -i -H "Content-Type: application/json" -d '{"type":1,"money":10}' http://localhost:8080/v1/accounts/3/update

# transfer money from one account to another
curl -X PUT -i -H "Content-Type: application/json" -d '{"type":2,"money":10}' http://localhost:8080/v1/accounts/4/transfer/6
```
