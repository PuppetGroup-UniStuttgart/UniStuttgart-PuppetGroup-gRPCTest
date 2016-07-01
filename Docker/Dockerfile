FROM maven:3.3-jdk-7

# Get sources
WORKDIR /
RUN git clone --recursive https://github.com/shruthikuki/CloudLab-IAAS-UniStuttgart-Part3-gRPCTest.git
WORKDIR /CloudLab-IAAS-UniStuttgart-Part3-gRPCTest

# Run maven
RUN mvn clean package

COPY main.proto /api/main.proto

VOLUME /api

CMD java -cp /CloudLab-IAAS-UniStuttgart-Part3-gRPCTest/target/gRPCTest-0.0.1-SNAPSHOT-jar-with-dependencies.jar cloudlab.ops.TestOpsServer