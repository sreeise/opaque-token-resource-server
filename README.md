## Spring Security 6 Opaque Token Resource Server Example

* Client Credentials used to authorize a REST API for provided scopes.
* Endpoints protected through Basic Auth using client-id and client-secret used to get Bearer Token.
* Use in conjunction with Spring Security Authorization Server.
* [See the authorization server example that works with this resource server.](https://github.com/sreeise/opaque-token-authorization-server)

Get the bearer token using the authorization server. Use the bearer token in the request to the messages endpoints:

        export TOKEN="token"

        curl --location --request GET 'http://localhost:8080/message' \
            --header 'Authorization: Bearer $TOKEN'
