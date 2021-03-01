package com.jenkins.demo.export;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 导出解析处理器
 *
 * @author sunlingao@zbj.com
 * @title
 * @date 2017年9月21日
 */
public final class ExportAnalyseHandler {

    private static final Logger LOG = LoggerFactory.getLogger(ExportAnalyseHandler.class);
    public static final Integer BIG_LINE = 65535;

    private ExportAnalyseHandler() {
    }

    /**
     * 实体导出成workbook对象
     *
     * @param sheetMap
     * @return
     * @author sunlingao@zbj.com
     * @date 2017年9月21日
     * @version
     */
    public static <T> Workbook exportListToBook(Map<String[], List<T>> sheetMap) {
        /** row移动下标 */
        HSSFWorkbook workbook = new HSSFWorkbook();
        sheetMap.forEach((titles, list) -> {
            int index = 0;
            HSSFSheet sheet = workbook.createSheet("name");
            if (titles != null && titles.length > 0) {
                /** 生成每行标题 */
                HSSFRow row = sheet.createRow(0);
                for (int i = 0; i < titles.length; i++) {
                    HSSFCell cell = row.createCell(i);
                    cell.setCellValue(titles[i]);
                }
                index = 1;
            }
            if (CollectionUtils.isEmpty(list)) {
                LOG.warn("实体数据为空!");
                return;
            }
            /** 填充数据 */
            for (int i = 0; i < list.size(); i++) {
//    			if(i>= Constants.MAX_SIZE)break;//超过最大导出行数
                HSSFRow row = sheet.createRow(index++);
                fillCell(row, list.get(i));
            }
        });

        return workbook;
    }

    /**
     * 导出实体
     * @param sheetMap
     * @param <T>
     * @return
     */
    public static <T> Workbook exportListToBook(Map<String, List<T>> sheetMap, String[] titles) {
        /** row移动下标 */
        HSSFWorkbook workbook = new HSSFWorkbook();
        sheetMap.forEach((sheetName, list) -> {
            int index = 0;
            HSSFSheet sheet = workbook.createSheet(sheetName);
            if (titles != null && titles.length > 0) {
                /** 生成每行标题 */
                HSSFRow row = sheet.createRow(0);
                for (int i = 0; i < titles.length; i++) {
                    HSSFCell cell = row.createCell(i);
                    cell.setCellValue(titles[i]);
                }
                index = 1;
            }
            if (CollectionUtils.isEmpty(list)) {
                LOG.warn("实体数据为空!");
                return;
            }
            /** 填充数据 */
            for (int i = 0; i < list.size(); i++) {
//    			if(i>= Constants.MAX_SIZE)break;//超过最大导出行数
                HSSFRow row = sheet.createRow(index++);
                fillCell(row, list.get(i));
            }
        });

        return workbook;
    }

    public static <T> Workbook exportListToBook(List<T> list) {
        Map<String[], List<T>> sheets = new HashMap<>();
        sheets.put(new String[0], list);
        return exportListToBook(sheets);
    }

    public static <T> Workbook exportListToBook(List<T> listResult, String[] titles) {
        Map<String, List<T>> sheets = new HashMap<>();
        if (listResult.size() <= BIG_LINE) {
            String sheetName = "1-" + listResult.size();
            sheets.put(sheetName, listResult);
        } else {
            for (int i = 0; i < listResult.size() / BIG_LINE + 1; i++) {
                String sheetName;
                int start = i * BIG_LINE;
                int end = (i + 1) * BIG_LINE;
                List<T> newListResult;
                if (end > listResult.size()) {
                    newListResult = listResult.subList(start, listResult.size());
                    sheetName = start + "-" + listResult.size();
                } else {
                    sheetName = start + "-" + end;
                    newListResult = listResult.subList(start, end);
                }
                sheets.put(sheetName, newListResult);
            }
        }
        return exportListToBook(sheets, titles);
    }

    /**
     * 填充单元格
     *
     * @param obj
     * @author sunlingao@zbj.com
     * @date 2017年9月21日
     * @version
     */
    private static void fillCell(HSSFRow row, Object obj) {
        int cellCount = 0;
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            HSSFCell cell = row.createCell(cellCount);
            String fieldName = field.getName();
            String getMethodName = "get" + StringUtils.capitalize(fieldName);
            try {
                Method method = obj.getClass().getMethod(getMethodName);
                String cellValue = String.valueOf(method.invoke(obj));
                cell.setCellValue("null".equals(cellValue) ? "" : cellValue);
            } catch (Exception e) {
                LOG.error("fillCell error method is {} msg is {}", getMethodName, e);
                cell.setCellValue("");
            }
            cellCount++;

        }
    }

    /**
     * 将数据导出到Excel07
     * @param dataList
     * @param titles
     * @param <T>
     * @return
     */
    public static <T> Workbook exportListToBook07(List<T> dataList, String[] titles) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet(1 + "-" + dataList.size());
        // 生成标题行
        if (titles != null && titles.length > 0) {
            XSSFRow row = sheet.createRow(0);
            for (int i = 0; i < titles.length; i++) {
                XSSFCell cell = row.createCell(i);
                cell.setCellValue(titles[i]);
            }
        }
        if (CollectionUtils.isEmpty(dataList)) {
            LOG.warn("实体数据为空!");
            return workbook;
        }
        // 填充数据
        int size = dataList.size();
        for (int i = 0; i < size; i++) {
           XSSFRow row = sheet.createRow(i + 1);
           fillCell07(row, dataList.get(i));
        }
        return workbook;
    }

    /**
     * 使用对象字段填充表格的行
     * @param row
     * @param obj
     */
    private static void fillCell07(XSSFRow row, Object obj) {
        int cellCount = 0;
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field: fields) {
            XSSFCell cell = row.createCell(cellCount);
            String fieldName = field.getName();
            String getMethodName = "get" + StringUtils.capitalize(fieldName);
            try {
                Method method = obj.getClass().getMethod(getMethodName);
                String cellValue = String.valueOf(method.invoke(obj));
                cell.setCellValue("null".equals(cellValue) ? "" : cellValue);
            } catch (Exception e) {
                LOG.error("fillCell error method is {} msg is {}", getMethodName, e);
                cell.setCellValue("");
            }
            cellCount++;
        }
    }
}
