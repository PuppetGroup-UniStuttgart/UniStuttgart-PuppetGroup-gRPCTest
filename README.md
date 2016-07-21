# UniStuttgart-PuppetGroup-gRPCTest
Java project of test gRPC that makes use of proto3 &amp; gRPC features

####Test gRPC:

This is the gRPC implementation of a testing service in order to test JSON-RPC adapter to check the compatabulity with gRPC feature.  

### Run the gRPC Testing API on your own
1. Clone the repository
2. Change your current working directory to "Docker"
3. Run following command: docker build --no-cache -t testgrpcapi . to build the image
4. Run the container: docker run -t testgrpcapi
