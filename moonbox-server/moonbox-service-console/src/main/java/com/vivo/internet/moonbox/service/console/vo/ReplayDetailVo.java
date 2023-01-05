package com.vivo.internet.moonbox.service.console.vo;

import java.util.Date;
import java.util.List;

import lombok.Builder;
import lombok.Data;

/**
 * RecordDetailVo - {@link ReplayDetailVo}
 *
 * @author yanjiang.liu
 * @version 1.0
 * @since 2022/8/29 11:19
 */
@Data
@Builder
public class ReplayDetailVo {

    /**
     * 应用名称
     */
    private String appName;

    /**
     * 录制任务id
     */
    private String recordTaskRunId;

    /**
     * 录制traceId
     */
    private String recordTraceId;

    /**
     * 回放traceId
     */
    private String traceId;

    /**
     * 调用类型
     */
    private String invokeType;

    /**
     * 回放环境
     */
    private String environment;

    /**
     * 回放机器
     */
    private String host;

    /**
     * 回放时间
     */
    private Date  replayTime;

    /**
     * 回放状态
     */
    private Integer replayStatus;

    /**
     * 回放结果提示
     */
    private String  replayCode;

    /**
     * 回放提示
     */
    private String  replayMessage;

    /**
     * 入口流量信息
     */
    private InvocationVo  originEntranceInvocation;

    /**
     * 回放响应结果
     */
    private Object           replayResponse;

    /**
     * 入口流量响应结果对比错误列表
     */
    private List<ReplayDiffVo> responseDiffs;

    /**
     * 子调用mock列表
     */
    private List<MockInvocationVo> mockInvocations;
}
