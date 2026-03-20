package com.xiao.core.customerSales.controller;

import com.xiao.base.ResultModel;
import com.xiao.core.basic.operator.domain.Operator;
import com.xiao.core.customerSales.service.CustomerSalesService;
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
@RequestMapping("/api/customer")
public class CustomerSalesController {

    @Autowired
    private CustomerSalesService customerSalesService;
    
    @LoginRequired(remark="查询客户销售数据")
    @GetMapping("/salesData")
    public ResultModel getCustomerSalesData(HttpServletRequest request, @CurrentUser Operator oper) {
        String reportType = request.getParameter("reportType");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String customerName = request.getParameter("customerName");
        String orgName = request.getParameter("orgName");
        String salesPerson = request.getParameter("salesPerson");
        
        if (StringUtil.isEmpty(reportType)) {
            reportType = "customer";
        }
        if (StringUtil.isEmpty(startDate)) {
            return ResultModel.error("开始日期不能为空");
        }
        if (StringUtil.isEmpty(endDate)) {
            return ResultModel.error("结束日期不能为空");
        }
        
        try {
            List<Map<String, Object>> result = customerSalesService.getCustomerSalesData(
                reportType, startDate, endDate, customerName, orgName, salesPerson);
            
            Map<String, Object> data = new HashMap<>();
            data.put("list", result);
            data.put("total", result != null ? result.size() : 0);
            
            return ResultModel.success(data);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultModel.error("查询失败: " + e.getMessage());
        }
    }
    
    @LoginRequired(remark="查询客户销售数据")
    @PostMapping("/salesData")
    public ResultModel getCustomerSalesDataPost(HttpServletRequest request, @CurrentUser Operator oper) {
        String reportType = request.getParameter("reportType");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String customerName = request.getParameter("customerName");
        String orgName = request.getParameter("orgName");
        String salesPerson = request.getParameter("salesPerson");
        
        if (StringUtil.isEmpty(reportType)) {
            reportType = "customer";
        }
        if (StringUtil.isEmpty(startDate)) {
            return ResultModel.error("开始日期不能为空");
        }
        if (StringUtil.isEmpty(endDate)) {
            return ResultModel.error("结束日期不能为空");
        }
        
        try {
            List<Map<String, Object>> result = customerSalesService.getCustomerSalesData(
                reportType, startDate, endDate, customerName, orgName, salesPerson);
            
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