package com.vivo.internet.moonbox.dal.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AgentFileWithBLOBs extends AgentFile{

    private String content;
}