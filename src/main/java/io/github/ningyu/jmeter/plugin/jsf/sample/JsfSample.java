package io.github.ningyu.jmeter.plugin.jsf.sample;

import com.alibaba.fastjson.JSON;
import com.jd.jsf.gd.GenericService;
import com.jd.jsf.gd.config.ConsumerConfig;
import com.jd.jsf.gd.config.RegistryConfig;
import com.jd.jsf.gd.error.RpcException;
import com.jd.jsf.gd.util.RpcContext;
import io.github.ningyu.jmeter.plugin.dubbo.sample.MethodArgument;
import io.github.ningyu.jmeter.plugin.util.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.jmeter.samplers.AbstractSampler;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.Interruptible;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author: zengweipei
 * @CreateTime: 2021-10-19 15:15
 * @Description:
 */
public class JsfSample extends AbstractSampler implements Interruptible {

    private static final Logger log = LoggingManager.getLoggerForClass();

    private JsfServiceUtils jsfServiceUtils;

    @Override
    public SampleResult sample(Entry entry) {
        SampleResult res = new SampleResult();
        res.setSampleLabel(getName());
        //构造请求数据
        res.setSamplerData(getSampleData());
        //调用dubbo
        res.setResponseData(JsonUtils.toJson(callDubbo(res)), StandardCharsets.UTF_8.name());
        //构造响应数据
        res.setDataType(SampleResult.TEXT);
        return res;
    }

    private RegistryConfig registryConfig;


    /**
     * Construct request data
     */
    private String getSampleData() {
        log.info("sample中的实例id" + this.toString() + ",element名称" + this.getName());
        StringBuilder sb = new StringBuilder();
        sb.append("Registry Protocol: ").append(Constants.getRegistryProtocol(this)).append("\n");
        sb.append("Address: ").append(Constants.getAddress(this)).append("\n");
        sb.append("RPC Protocol: ").append(Constants.getRpcProtocol(this)).append("\n");
        sb.append("Timeout: ").append(Constants.getTimeout(this)).append("\n");
        sb.append("Alias: ").append(Constants.getAlias(this)).append("\n");
        sb.append("Retries: ").append(Constants.getRetries(this)).append("\n");
        sb.append("Cluster: ").append(Constants.getCluster(this)).append("\n");
        sb.append("Connections: ").append(Constants.getConnections(this)).append("\n");
        sb.append("LoadBalance: ").append(Constants.getLoadbalance(this)).append("\n");
        sb.append("Async: ").append(Constants.getAsync(this)).append("\n");
        sb.append("Interface: ").append(Constants.getInterface(this)).append("\n");
        sb.append("Method: ").append(Constants.getMethod(this)).append("\n");
        sb.append("Method Args: ").append(Constants.getMethodArgs(this).toString());
        sb.append("Attachment Args: ").append(Constants.getAttachmentArgs(this).toString());
        return sb.toString();
    }

    private Object callDubbo(SampleResult res) {
        // This instance is heavy, encapsulating the connection to the registry and the connection to the provider,
        // so please cache yourself, otherwise memory and connection leaks may occur.
        ConsumerConfig<GenericService> reference = new ConsumerConfig();
        // set application
        /** registry center */
        String address = Constants.getAddress(this);
        if (StringUtils.isBlank(address)) {
            setResponseError(res, ErrorCode.MISS_ADDRESS);
            return ErrorCode.MISS_ADDRESS.getMessage();
        }

        String rpcProtocol = Constants.getRpcProtocol(this).replaceAll("://", "");
        String protocol = Constants.getRegistryProtocol(this);
        if(registryConfig == null){
            Integer registryTimeout = null;
            try {
                if (!StringUtils.isBlank(Constants.getRegistryTimeout(this))) {
                    registryTimeout = Integer.valueOf(Constants.getRegistryTimeout(this));
                }
            } catch (NumberFormatException e) {
                setResponseError(res, ErrorCode.TIMEOUT_ERROR);
                return ErrorCode.TIMEOUT_ERROR.getMessage();
            }
            registryConfig = new RegistryConfig();
            registryConfig.setProtocol(protocol);
            registryConfig.setIndex(address);
            if (registryTimeout != null) {
                registryConfig.setTimeout(registryTimeout);
            }
        }

        reference.setProtocol(rpcProtocol);
        reference.setRegistry(registryConfig);

        try {
            // set interface
            String interfaceName = Constants.getInterface(this);
            if (StringUtils.isBlank(interfaceName)) {
                setResponseError(res, ErrorCode.MISS_INTERFACE);
                return ErrorCode.MISS_INTERFACE.getMessage();
            }

            if (!jsfServiceUtils.containsKey(interfaceName)) {
                reference.setInterfaceId(interfaceName);

                // set retries
                Integer retries = null;
                try {
                    if (!StringUtils.isBlank(Constants.getRetries(this))) {
                        retries = Integer.valueOf(Constants.getRetries(this));
                    }
                } catch (NumberFormatException e) {
                    setResponseError(res, ErrorCode.RETRIES_ERROR);
                    return ErrorCode.RETRIES_ERROR.getMessage();
                }
                if (retries != null) {
                    reference.setRetries(retries);
                }

                // set cluster
                String cluster = Constants.getCluster(this);
                if (!StringUtils.isBlank(cluster)) {
                    reference.setCluster(Constants.getCluster(this));
                }

                // set version
                String alias = Constants.getAlias(this);
                if (!StringUtils.isBlank(alias)) {
                    reference.setAlias(alias);
                }
                // set timeout
                Integer timeout = null;
                try {
                    if (!StringUtils.isBlank(Constants.getTimeout(this))) {
                        timeout = Integer.valueOf(Constants.getTimeout(this));
                    }
                } catch (NumberFormatException e) {
                    setResponseError(res, ErrorCode.TIMEOUT_ERROR);
                    return ErrorCode.TIMEOUT_ERROR.getMessage();
                }
                if (timeout != null) {
                    reference.setTimeout(timeout);
                }

                // set loadBalance
                String loadBalance = Constants.getLoadbalance(this);
                if (!StringUtils.isBlank(loadBalance)) {
                    reference.setLoadbalance(loadBalance);
                }

                // set async
                String async = Constants.getAsync(this);
                if (!StringUtils.isBlank(async)) {
                    reference.setAsync(Constants.ASYNC.equals(async) ? true : false);
                }

                // set generic
                reference.setGeneric(true);
                // The registry's address is to generate the ReferenceConfigCache key
                jsfServiceUtils.putJsfGeneric(interfaceName,reference.refer());
            }

            GenericService genericService = jsfServiceUtils.getGenericService(interfaceName);

            if (genericService == null) {
                setResponseError(res, ErrorCode.GENERIC_SERVICE_IS_NULL);
                return MessageFormat.format(ErrorCode.GENERIC_SERVICE_IS_NULL.getMessage(), interfaceName);
            }
            String[] parameterTypes = null;
            Object[] parameterValues = null;
            List<MethodArgument> args = Constants.getMethodArgs(this);
            List<String> paramterTypeList = new ArrayList<String>();
            ;
            List<Object> parameterValuesList = new ArrayList<Object>();
            
            for (MethodArgument arg : args) {
                ClassUtils.parseParameter(paramterTypeList, parameterValuesList, arg);
            }
            parameterTypes = paramterTypeList.toArray(new String[paramterTypeList.size()]);
            parameterValues = parameterValuesList.toArray(new Object[parameterValuesList.size()]);

            List<MethodArgument> attachmentArgs = Constants.getAttachmentArgs(this);
            if (attachmentArgs != null && !attachmentArgs.isEmpty()) {
                RpcContext.getContext().setAttachments(attachmentArgs.stream().collect(Collectors.toMap(MethodArgument::getParamType, MethodArgument::getParamValue)));
            }
            String methodName = Constants.getMethod(this);
            if (StringUtils.isBlank(methodName)) {
                setResponseError(res, ErrorCode.MISS_METHOD);
                return ErrorCode.MISS_METHOD.getMessage();
            }
            res.sampleStart();
            Object result = null;
            try {
                result = genericService.$invoke(methodName, parameterTypes, parameterValues);
                res.setResponseOK();
            } catch (Exception e) {
                log.error("Exception：", e);
                if (e instanceof RpcException) {
                    RpcException rpcException = (RpcException) e;
                    setResponseError(res, String.valueOf(rpcException.getErrorCode()), rpcException.getMessage());
                } else {
                    setResponseError(res, ErrorCode.UNKNOWN_EXCEPTION);
                }
                result = e;
            }
            res.sampleEnd();
            return result;
        } catch (Exception e) {
            log.error("UnknownException：", e);
            setResponseError(res, ErrorCode.UNKNOWN_EXCEPTION);
            return e;
        }
    }


    public void setResponseError(SampleResult res, ErrorCode errorCode) {
        setResponseError(res, errorCode.getCode(), errorCode.getMessage());
    }

    public void setResponseError(SampleResult res, String code, String message) {
        res.setSuccessful(false);
        res.setResponseCode(code);
        res.setResponseMessage(message);
    }

    @Override
    public boolean interrupt() {
        Thread.currentThread().interrupt();
        return true;
    }
}
