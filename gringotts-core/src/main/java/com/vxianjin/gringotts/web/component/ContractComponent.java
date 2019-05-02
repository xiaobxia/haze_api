package com.vxianjin.gringotts.web.component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.vxianjin.gringotts.common.JsonResult;
import com.vxianjin.gringotts.util.StringUtils;
import com.vxianjin.gringotts.web.client.ContractClient;
import com.vxianjin.gringotts.web.pojo.CfcaContractInfo;
import com.vxianjin.gringotts.web.pojo.CfcaUserInfo;
import com.vxianjin.gringotts.web.util.cfcautil.CfcaCommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 生成合同接口类
 *
 * @author tgy
 * @version [版本号, 2018年1月23日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Component(value = "contractCnt")
public class ContractComponent implements IContractComponent {
	/**
	 * 日志打印
	 */
	private static Logger LOGGER = LoggerFactory.getLogger(ContractComponent.class);

	@Autowired
	private ContractClient client;

	/**
	 * 生成合同
	 *
	 * @param contract
	 * @param userinfo
	 * @return
	 * @throws IOException
	 */
	@Override
	public JsonResult createContract(CfcaContractInfo contract, CfcaUserInfo userinfo) throws IOException {
		String contractNo = getContractNo();
		//设置合同ID
		contract.setContractNo(contractNo);
		String result = client.generateContract(contract, userinfo);
		LOGGER.info(result);
		JsonResult ret = dealCfcaResult(result);
		//成功直接保存合同ID
		if (CfcaCommonUtil.CODE_SUCCESS.equals(ret.getCode())) {

			String cfcaContractInfo = StringUtils.killnull(ret.getData());
			String url = JSON.parseObject(cfcaContractInfo).getString("url");
			if (StringUtils.isNotEmpty(url)) {
				int index1 = url.lastIndexOf("_");
				int index2 = url.indexOf(".html");
				String cfcaContractNo = url.substring(index1 + 1, index2);
				//设置云证合同ID
				contract.setCfcaContractId(cfcaContractNo);
				contract.setUrl(url);
			}
		}

		//重設Data
		ret.setData(contract);
		return ret;
	}

	/**
	 * 处理云证系统返回的json字符串
	 *
	 * @param resultMsg
	 * @return
	 */
	private JsonResult dealCfcaResult(String resultMsg) {
		JsonResult ret = new JsonResult();
		ret.setCount("1");
		ret.setData(resultMsg);

		//云证出错可能会返回一个网页
		if (!StringUtils.isEmpty(resultMsg)) {
			if (resultMsg.contains("<html")) {
				ret.setData(resultMsg);
				ret.setMsg("CFCA DEAL ERROR,AND RETURN HTML PAGE!");
				ret.setResult(CfcaCommonUtil.RET_FAILURE);
				ret.setCode(CfcaCommonUtil.CODE_FAILURE);
				return ret;
			}
		}

		//当是json串处理成json
		JSONObject jsonCfca = JSON.parseObject(resultMsg);

		//返回結果為空或不存在code
		if (jsonCfca.isEmpty() || StringUtils.isEmpty(jsonCfca.getString("code"))) {
			LOGGER.error("Generate contract failed , please check network!");
			ret.setMsg("Generate contract failed , please check network!");
			ret.setResult(CfcaCommonUtil.RET_FAILURE);
			ret.setCode(CfcaCommonUtil.CODE_FAILURE);
			return ret;
		}
		String code = jsonCfca.getString("code");
		String msg = jsonCfca.getString("msg");
		//成功
		if ("0".equals(code)) {
			ret.setMsg("SUCCESS GENERATE CONTRACT!");
			ret.setCode(CfcaCommonUtil.RET_SUCCESS);
			ret.setCode(CfcaCommonUtil.CODE_SUCCESS);
			String data = jsonCfca.getString("data");
			ret.setData(data);
		} else {
			ret.setMsg(msg);
			ret.setResult(CfcaCommonUtil.RET_FAILURE);
			ret.setCode(CfcaCommonUtil.CODE_FAILURE);
		}
		return ret;
	}

	/**
	 * 生成合同ID
	 *
	 * @return
	 */
	private String getContractNo() {
		String contractNo = "JX-";
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String dateString = sdf.format(date);
		return contractNo + dateString;
	}
}
