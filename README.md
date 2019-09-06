# Symphony Tabs

Provides Table editing and sign-off within Symphony.


## Configuration

This is a Spring Boot application.  You probably want to set the following parameters in `application.properties`:

```
server.port=4000                                      # Port Number

server.ssl.key-store-type=PKCS12                      # Spring SSL Config
server.ssl.key-store=classpath:keystore/localhost.p12 # Spring SSL Config
server.ssl.key-store-password=changeit                # Spring SSL Config
server.ssl.key-alias=localhost                        # Spring SSL Config
baseUrl=https://localhost:4000                        # Address to refer to the application
```

## Deploying

This is a Symphony application.  You can run manually on any host like this:

```
https://<your pod>.symphony.com/client/index.html?bundle=https://localhost:4000/bundle-local.json
```

Or you can load this into the admin console to install the application on a pod:

```
https://localhost:4000/bundle-pod.json
```

## Release Notes 

 