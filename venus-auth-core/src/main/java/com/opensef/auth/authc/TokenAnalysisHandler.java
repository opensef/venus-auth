package com.opensef.auth.authc;

/**
 * 解析token处理器，从用户传入的参数中解析出token，例如从header和url中解析
 */
public interface TokenAnalysisHandler {

    String getToken();

}
