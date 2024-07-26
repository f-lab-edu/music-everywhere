package me.kong.paymentservice.common;

public enum EventConstants {

    GROUP_MEMBER_INCREASE("dev.group.payment.request.increase-group-size"),
    ;

    private final String topicName;

    private EventConstants(String topicName) {
        this.topicName = topicName;
    }

    public String getTopicName() {
        return topicName;
    }
}
