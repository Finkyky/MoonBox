package com.vivo.jvm.sandbox.moonbox.plugin.local.date.time;

import com.alibaba.jvm.sandbox.api.ProcessControlException;
import com.alibaba.jvm.sandbox.api.event.BeforeEvent;
import com.alibaba.jvm.sandbox.api.event.Event;
import com.alibaba.jvm.sandbox.repeater.plugin.api.InvocationListener;
import com.alibaba.jvm.sandbox.repeater.plugin.api.InvocationProcessor;
import com.alibaba.jvm.sandbox.repeater.plugin.core.cache.MoonboxRepeatCache;
import com.alibaba.jvm.sandbox.repeater.plugin.core.impl.api.DefaultEventListener;
import com.alibaba.jvm.sandbox.repeater.plugin.core.impl.spi.AbstractInvokePluginAdapter;
import com.alibaba.jvm.sandbox.repeater.plugin.core.model.EnhanceModel;
import com.alibaba.jvm.sandbox.repeater.plugin.core.model.MoonboxContext;
import com.alibaba.jvm.sandbox.repeater.plugin.core.utils.SysTimeUtils;
import com.alibaba.jvm.sandbox.repeater.plugin.domain.RepeaterConfig;
import com.alibaba.jvm.sandbox.repeater.plugin.spi.InvokePlugin;
import com.google.common.collect.Lists;
import com.vivo.internet.moonbox.common.api.model.InvokeType;
import org.kohsuke.MetaInfServices;

import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;

/**
 * JavaLocalTimePlugin
 */
@MetaInfServices(InvokePlugin.class)
public class JavaLocalTimePlugin extends AbstractInvokePluginAdapter {
    /**
     * java.time.LocalDateTime 暂时只增强 public static LocalDateTime now(Clock clock) 方法
     * 由于是系统中的方法（jdk 内置方法），因此需要设置 IsIncludeBootstrap 属性
     */
    @Override
    protected List<EnhanceModel> getEnhanceModels() {
        EnhanceModel.MethodPattern mp = EnhanceModel.MethodPattern.builder()
                .methodName("now")
                .parameterType(new String[]{"java.time.Clock"})
                .build();
        EnhanceModel em = EnhanceModel.builder()
                .classPattern("java.time.LocalTime")
                .methodPatterns(new EnhanceModel.MethodPattern[]{mp})
                .watchTypes(Event.Type.BEFORE, Event.Type.RETURN, Event.Type.THROWS)
                .setIsIncludeBootstrap(true)
                .build();
        return Lists.newArrayList(em);
    }

    @Override
    protected JavaLocalTimeEventListener getEventListener(InvocationListener listener) {
        return new JavaLocalTimeEventListener(getType(), isEntrance(), listener, getInvocationProcessor());
    }

    @Override
    protected InvocationProcessor getInvocationProcessor() {
        return null;
    }

    @Override
    public InvokeType getType() {
        return InvokeType.LOCAL_DATE_TIME;
    }

    @Override
    public boolean enable(RepeaterConfig config) {
        return MoonboxContext.getInstance().isRepeatMode() && super.enable(config);
    }

    @Override
    public String identity() {
        return InvokeType.LOCAL_DATE_TIME.getInvokeName();
    }

    @Override
    public boolean isEntrance() {
        return false;
    }

    public static class JavaLocalTimeEventListener extends DefaultEventListener {

        public JavaLocalTimeEventListener(InvokeType invokeType, boolean entrance, InvocationListener listener, InvocationProcessor processor) {
            super(invokeType, entrance, listener, processor);
        }

        /**
         * 事件是否可以通过
         * <p>
         * 降级之后只有回放流量可以通过
         *
         * @param event 事件
         * @return 是否通过
         */
        @Override
        protected boolean access(Event event) throws Exception {
            if (MoonboxContext.getInstance().isRepeatMode() && event instanceof BeforeEvent && MoonboxRepeatCache.isRepeatFlow()) {
                long time = SysTimeUtils.getTime();
                LocalTime localTime = Instant.ofEpochMilli(time).atZone(ZoneId.systemDefault()).toLocalTime();
                ProcessControlException.throwReturnImmediately(localTime);
            }
            return super.access(event);
        }
    }
}
