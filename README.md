# webflux-reactive-mong-db
This proeject cover the basis of webflux reactive stream data.
Here we have the api project and the client working together.
MongoDB has been chosen to persist data.

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