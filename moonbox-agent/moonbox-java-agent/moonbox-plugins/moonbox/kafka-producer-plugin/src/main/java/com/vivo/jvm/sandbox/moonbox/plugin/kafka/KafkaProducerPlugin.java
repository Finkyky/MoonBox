package com.vivo.jvm.sandbox.moonbox.plugin.kafka;

import com.alibaba.jvm.sandbox.api.event.Event;
import com.alibaba.jvm.sandbox.repeater.plugin.api.InvocationProcessor;
import com.alibaba.jvm.sandbox.repeater.plugin.core.impl.spi.AbstractInvokePluginAdapter;
import com.alibaba.jvm.sandbox.repeater.plugin.core.model.EnhanceModel;
import com.alibaba.jvm.sandbox.repeater.plugin.spi.InvokePlugin;
import com.vivo.internet.moonbox.common.api.model.InvokeType;
import org.kohsuke.MetaInfServices;

import java.util.Collections;
import java.util.List;

/**
 * Kafka producer插件，主要用于增强doSend(ProducerRecord<K, V> record, Callback callback)方法，
 * 由于kafka tps能够达到非常高，因此录制时采样率可以设置低些
 */
@MetaInfServices(InvokePlugin.class)
public class KafkaProducerPlugin extends AbstractInvokePluginAdapter {

    @Override
    protected List<EnhanceModel> getEnhanceModels() {
        EnhanceModel.MethodPattern doSendMethod = EnhanceModel.MethodPattern.builder()
                .methodName("doSend")
                .parameterType(new String[]{"org.apache.kafka.clients.producer.ProducerRecord", "org.apache.kafka.clients.producer.Callback"})
                .build();
        EnhanceModel enhanceModel = EnhanceModel.builder().classPattern("org.apache.kafka.clients.producer.KafkaProducer")
                .methodPatterns(new EnhanceModel.MethodPattern[]{doSendMethod})
                .watchTypes(Event.Type.BEFORE, Event.Type.RETURN, Event.Type.THROWS)
                .build();
        return Collections.singletonList(enhanceModel);
    }

    @Override
    protected InvocationProcessor getInvocationProcessor() {
        return new KafkaInvocationProcessor(getType());
    }

    @Override
    public InvokeType getType() {
        return InvokeType.KAFKA_PRODUCER;
    }

    @Override
    public String identity() {
        return InvokeType.KAFKA_PRODUCER.getInvokeName();
    }

    @Override
    public boolean isEntrance() {
        return false;
    }
}
