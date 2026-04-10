package com.xiao.core.productReport.controller;

import com.xiao.base.ResultModel;
import com.xiao.core.basic.operator.domain.Operator;
import com.xiao.core.productReport.service.ProductReportService;
import com.xiao.logannotation.CurrentUser;
import com.xiao.logannotation.LoginRequired;
import com.xiao.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/product")
public class ProductReportController {

    @Autowired
    private ProductReportService productReportService;

    @LoginRequired(remark="查询产品采购报表数据")
    @GetMapping("/purchaseReportforXT")
    public ResultModel getProductPurchaseReportforXT(HttpServletRequest request, @CurrentUser Operator oper) {  
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String personName = request.getParameter("personName");

        if (StringUtil.isEmpty(startDate)) {
            return ResultModel.error("开始日期不能为空");
        }
        if (StringUtil.isEmpty(endDate)) {
            return ResultModel.error("结束日期不能为空");
        }
        if (StringUtil.isEmpty(personName)) {
            return ResultModel.error("业务员不能为空");
        }

        try {
            List<Map<String, Object>> result = productReportService.getProductPurchaseReport(
                startDate, endDate, personName);

            Map<String, Object> data = new HashMap<>();
            data.put("list", result);
            data.put("total", result != null ? result.size() : 0);

            return ResultModel.success(data);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultModel.error("查询失败: " + e.getMessage());
        }
    }

    @LoginRequired(remark="查询产品采购报表数据")
    @PostMapping("/purchaseReportforXT")
    public ResultModel getProductPurchaseReportforXTPost(HttpServletRequest request, @CurrentUser Operator oper) {
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String personName = request.getParameter("personName");

        if (StringUtil.isEmpty(startDate)) {
            return ResultModel.error("开始日期不能为空");
        }
        if (StringUtil.isEmpty(endDate)) {
            return ResultModel.error("结束日期不能为空");
        }
        if (StringUtil.isEmpty(personName)) {
            return ResultModel.error("业务员不能为空");
        }

        try {
            List<Map<String, Object>> result = productReportService.getProductPurchaseReport(
                startDate, endDate, personName);

            Map<String, Object> data = new HashMap<>();
            data.put("list", result);
            data.put("total", result != null ? result.size() : 0);

            return ResultModel.success(data);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultModel.error("查询失败: " + e.getMessage());
        }
    }

    @LoginRequired(remark="查询通用产品采购报表数据")
    @GetMapping("/purchaseReportforTYCP")
    public ResultModel getProductPurchaseReportforTYCP(HttpServletRequest request, @CurrentUser Operator oper) {
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String purchaseMan = request.getParameter("purchaseMan");
        String statDimension = request.getParameter("statDimension");

        if (StringUtil.isEmpty(startDate)) {
            return ResultModel.error("开始日期不能为空");
        }
        if (StringUtil.isEmpty(endDate)) {
            return ResultModel.error("结束日期不能为空");
        }
        if (StringUtil.isEmpty(statDimension)) {
            return ResultModel.error("统计维度不能为空");
        }

        try {
            List<Map<String, Object>> result = productReportService.getProductPurchaseReportForTYCP(
                startDate, endDate, purchaseMan, statDimension);

            Map<String, Object> data = new HashMap<>();
            data.put("list", result);
            data.put("total", result != null ? result.size() : 0);

            return ResultModel.success(data);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultModel.error("查询失败: " + e.getMessage());
        }
    }

    @LoginRequired(remark="查询通用产品采购报表数据")
    @PostMapping("/purchaseReportforTYCP")
    public ResultModel getProductPurchaseReportforTYCPPost(HttpServletRequest request, @CurrentUser Operator oper) {
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String purchaseMan = request.getParameter("purchaseMan");
        String statDimension = request.getParameter("statDimension");

        if (StringUtil.isEmpty(startDate)) {
            return ResultModel.error("开始日期不能为空");
        }
        if (StringUtil.isEmpty(endDate)) {
            return ResultModel.error("结束日期不能为空");
        }
        if (StringUtil.isEmpty(statDimension)) {
            return ResultModel.error("统计维度不能为空");
        }

        try {
            List<Map<String, Object>> result = productReportService.getProductPurchaseReportForTYCP(
                startDate, endDate, purchaseMan, statDimension);

            Map<String, Object> data = new HashMap<>();
            data.put("list", result);
            data.put("total", result != null ? result.size() : 0);

            return ResultModel.success(data);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultModel.error("查询失败: " + e.getMessage());
        }
    }
}
