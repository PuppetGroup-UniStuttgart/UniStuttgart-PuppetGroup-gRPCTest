package cloudlab.ops;

import cloudlab.TestOpsProto.Main;
import cloudlab.TestOpsProto.TestGrpc;
import com.google.common.util.concurrent.SettableFuture;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Created by shreyasbr on 01-07-2016.
 */
public class TestOpsClient {
    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50053).usePlaintext(true).build();
        TestGrpc.TestBlockingStub blockingStub = TestGrpc.newBlockingStub(channel);
        TestGrpc.TestStub stub = TestGrpc.newStub(channel);

        Main.StringReply stringReply = blockingStub.stringTest(Main.StringRequest.newBuilder().setName("Kuki").setPlace("Madras").build());
        System.out.println("stringReply.getOutput() = " + stringReply.getOutput());

        Main.IntegerReply integerReply = blockingStub.integerTest(Main.IntegerRequest.newBuilder().setFirstNumber(3).setSecondNumber(4).build());
        System.out.println("integerReply.getOutput() = " + integerReply.getOutput());

        Main.FloatReply floatReply = blockingStub.floatTest(Main.FloatRequest.newBuilder().setFirstNumber(2.3f).setSecondNumber(3.7f).build());
        System.out.println("floatReply.getOutput() = " + floatReply.getOutput());

        Main.DoubleReply doubleReply = blockingStub.doubleTest(Main.DoubleRequest.newBuilder().setFirstNumber(2.3).setSecondNumber(3.7).build());
        System.out.println("doubleReply.getOutput() = " + doubleReply.getOutput());

        Main.EvenOddReply boolReply = blockingStub.boolTest(Main.EvenOddRequest.newBuilder().setNumber(4).build());
        System.out.println("boolReply.getOutput() = " + boolReply.getOutput());

        Main.EnumReply enumReply = blockingStub.enumTest(Main.EnumRequest.newBuilder().setStatusValue(2).build());
        System.out.println("enumReply.getOutput() = " + enumReply.getOutput());

        Main.StringReply repeatedReply = blockingStub.repeatedTest(Main.Array.newBuilder().addItems("shoes").addItems("bag").build());
        System.out.println("repeatedReply.getOutput() = " + repeatedReply.getOutput());

        Map<String, String> sampleMap = new HashMap<String, String>();
        sampleMap.put("name", "Kuki");
        sampleMap.put("place", "Helsinki");
        Main.StringReply mapReply = blockingStub.mapTest(Main.MapRequest.newBuilder().putAllMap(sampleMap).build());
        System.out.println("mapReply.getOutput() = " + mapReply.getOutput());

        Iterator<Main.StringStreamReply> serverStreamReply = blockingStub.serverStream(Main.StringRequest.newBuilder().setName("Kuki").setPlace("Chennai").build());
        Main.StringStreamReply nameReply = serverStreamReply.next();
        Main.StringStreamReply placeReply = serverStreamReply.next();
        System.out.println(nameReply.getName() + " is from " + placeReply.getPlace());

        final SettableFuture<Void> finishFutureClient = SettableFuture.create();
        StreamObserver<Main.StringRequest> requestClientStream = stub.clientStream(new StreamObserver<Main.StringReply>() {
            @Override
            public void onNext(Main.StringReply value) {
                System.out.println("client stream value.getOutput() = " + value.getOutput());
            }

            @Override
            public void onError(Throwable t) {
                t.printStackTrace();
                finishFutureClient.setException(t);
            }

            @Override
            public void onCompleted() {
                System.out.println("Client stream completed");
                finishFutureClient.set(null);
            }
        });
        requestClientStream.onNext(Main.StringRequest.newBuilder().setName("Kuki").build());
        requestClientStream.onNext(Main.StringRequest.newBuilder().setPlace("Mangalore").build());
        requestClientStream.onCompleted();
        while (!finishFutureClient.isDone()) {

        }
        try {
            finishFutureClient.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        final SettableFuture<Void> finishFutureBidirectional = SettableFuture.create();
        StreamObserver<Main.StringRequest> requestBidirectionalStream = stub.bidirectionalStream(new StreamObserver<Main.StringStreamReply>() {
            int index = 0;
            @Override
            public void onNext(Main.StringStreamReply value) {
                if(index == 0) {
                    System.out.println("bidirectional stream value.getName() = " + value.getName());
                    index++;
                } else {
                    System.out.println("bidirectional stream value.getPlace() = " + value.getPlace());
                }

            }

            @Override
            public void onError(Throwable t) {
                t.printStackTrace();
                finishFutureBidirectional.setException(t);
            }

            @Override
            public void onCompleted() {
                System.out.println("Client stream completed");
                finishFutureBidirectional.set(null);
            }
        });
        requestBidirectionalStream.onNext(Main.StringRequest.newBuilder().setName("Kuki").build());
        requestBidirectionalStream.onNext(Main.StringRequest.newBuilder().setPlace("Mangalore").build());
        requestBidirectionalStream.onCompleted();
        while (!finishFutureBidirectional.isDone()) {

        }
        try {
            finishFutureBidirectional.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
