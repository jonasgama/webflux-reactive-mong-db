# webflux-reactive-mong-db


save
curl --location --request POST 'http://localhost:8082/v2' --header 'Content-Type: application/json' --data-raw '{
"id":"test",
"description":"a/c",
"price":999.99
}'

list
curl --location --request GET 'http://localhost:8082/v2' --header 'Content-Type: application/json'


find
curl --location --request GET 'http://localhost:8082/v2/test' --header 'Content-Type: application/json'