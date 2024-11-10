package org.example.coffeshop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
public class GetQueueResponse {

    private int maxNumberOfQueues;
    private int maxQueueSize;
    private List<QueueResponse> queues;

    @Data
    @AllArgsConstructor
    public static class QueueResponse {
        private long id;
        private int currentSize;
        private List<OrderResponse> orders;

        @Data
        public static class OrderResponse {
            private long id;
            private String itemName;
            private double price;
            private String status;
            private String customerName;
        }
    }
}
