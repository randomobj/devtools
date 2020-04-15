package com.gitee.randomobject.utils;

import com.gitee.randomobject.annotation.Excel;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * excel工具类
 */
public final class ExcelUtil<T> {

    private Logger log = LoggerFactory.getLogger(ExcelUtil.class);

    /**
     * 导出
     * @param clz 实体类对象
     * @param list 需要导出的实体类对象集合
     * @param sheetName 表名
     * @param sheetSize 多少个sheet
     * @param out 输出流
     * @param isDemo 是否是demo
     */
    public boolean exportExcel(Class<T> clz,List<T> list, String sheetName, int sheetSize,OutputStream out,boolean isDemo) {
        log.info("[导出excel]:clz={},list={},sheetName={},sheetSize={},isDemo={}",clz,list,sheetName,sheetSize,isDemo);
        Field[] allFields = clz.getDeclaredFields();// 得到所有定义字段
        List<Field> fields = new ArrayList<>();
        // 得到所有field并存放到一个list中.
        for (Field field : allFields) {
            if (field.isAnnotationPresent(Excel.class)) {
                fields.add(field);
            }
        }
        // 产生工作薄对象
        HSSFWorkbook workbook = new HSSFWorkbook();
        //将列设置为文本格式
        HSSFCellStyle cellStyle2 = workbook.createCellStyle();
        HSSFDataFormat format = workbook.createDataFormat();
        cellStyle2.setDataFormat(format.getFormat("@"));

        // excel2003中每个sheet中最多有65536行,为避免产生错误所以加这个逻辑.
        if (sheetSize > 65536 || sheetSize < 1) {
            sheetSize = 65536;
        }
        double sheetNo = Math.ceil(list.size() / sheetSize);// 取出一共有多少个sheet.
        for (int index = 0; index <= sheetNo; index++) {
            HSSFSheet sheet = workbook.createSheet();// 产生工作表对象
            workbook.setSheetName(index, sheetName + index);// 设置工作表的名称.
            HSSFRow row;
            HSSFCell cell;// 产生单元格
            row = sheet.createRow(0);// 产生一行
            // 写入各个字段的列头名称
            for (int i = 0; i < fields.size(); i++) {
                Field field = fields.get(i);
                Excel attr = field.getAnnotation(Excel.class);
                int col = getExcelCol(attr.column());// 获得列号
                cell = row.createCell(col);// 创建列
                row.setRowStyle(cellStyle2);
                cell.setCellType(CellType.STRING);// 设置列中写入内容为String类型
                cell.setCellValue(attr.name());// 写入列名
                cell.setCellStyle(cellStyle2);
                // 如果设置了提示信息则鼠标放上去提示.
                if (!attr.prompt().trim().equals("")) {
                    setHSSFPrompt(sheet, "", attr.prompt(), 1, 1000, col, col);// 这里默认设了2-101列提示.
                }
                // 如果设置了combo属性则本列只能选择不能输入
                if (attr.combo().length > 0) {

                }
            }
            int startNo = index * sheetSize;
            int endNo = Math.min(startNo + sheetSize, list.size());
            int nullColumn = 500;//在原有数据上新添nullColumn条空数据
            // 写入各条记录,每条记录对应excel表中的一行
            for (int i = startNo; i < endNo + nullColumn; i++) {
                row = sheet.createRow(i + 1 - startNo);
                T vo;
                if (i < list.size()) {
                    vo = (T) list.get(i); // 得到导出对象.
                } else {
                    vo = null;
                }
                assert  vo !=null;
                for (int j = 0; j < fields.size(); j++) {
                    Field field = fields.get(j);// 获得field.
                    field.setAccessible(true);// 设置实体类私有属性可访问
                    Excel attr = field.getAnnotation(Excel.class);
                    try {
                        //当不是导入demo时就填写数据
                        if (!isDemo) {
                            // 根据ExcelVOAttribute中设置情况决定是否导出,有些情况需要保持为空,希望用户填写这一列.
                            if (attr.isExport()) {
                                cell = row.createCell(getExcelCol(attr.column()));// 创建cell
                                cell.setCellType(CellType.STRING);
                                if (i < list.size()) {
                                    cell.setCellValue(field.get(vo) == null ? "" : String.valueOf(field.get(vo)));// 如果数据存在就填入,不存在填入空格.
                                } else {
                                    cell.setCellValue("");// 填入空格.
                                }
                                //将列设置为文本格式
                                cell.setCellStyle(cellStyle2);
                            }
                        } else {
                            cell = row.createCell(getExcelCol(attr.column()));// 创建cell
                            cell.setCellType(CellType.STRING);
                            cell.setCellValue("");// 如果数据存在就填入,不存在填入空格.
                            //将列设置为文本格式
                            cell.setCellStyle(cellStyle2);
                        }

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        try {
            out.flush();
            workbook.write(out);
            out.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
           log.debug("[导出excel出现异常]:信息={}",e.getMessage());
            return false;
        }
    }

    /***
     * 导入
     * @param clz 接收导入的实体类对象
     * @param sheetName 指定工作簿中的表名
     * @param filePath 需要导入的文件地址
     */
    public boolean importExcel(Class<T> clz, String sheetName, String filePath) {

        log.info("[excel导入]:clz={},sheetName={},filePath={}",clz,sheetName,filePath);
        List<T> list = new ArrayList<>();
        try {
            // 获取文件
            File file = new File(filePath);
            FileInputStream fInputStream = new FileInputStream(file);
            //使用map去装的目的，就是为了接下来，好取值，通过field动态设置值给导入的对象
            HashMap<Integer, Field> map = new HashMap<>();
            //可以根据传入的文件类型，分别创建
            //根据类对象获取字段信息
            Field[] declaredFields = clz.getDeclaredFields();
            for (Field field : declaredFields) {
                //字段上有注解的，表示需要被导入进来
                if (field.isAnnotationPresent(Excel.class)) {
                    Excel annotation = field.getAnnotation(Excel.class);
                    //获得对应的列的序号
                    int excelCol = getExcelCol(annotation.column());
                    map.put(excelCol, field);
                }
            }
            Workbook workbook = Workbook.getWorkbook(fInputStream);
            Sheet sheet;
            if (StringUtil.checkEmpty(sheetName)) {
                sheet = workbook.getSheet(sheetName);
            } else {
                //默认取第一个
                sheet = workbook.getSheet(0);
            }
            assert sheet != null;
            int rows = sheet.getRows();
            for (int i = 0; i < rows; i++) {
                Cell[] cells = sheet.getRow(i);
                T entity = null;
                for (int j = 0; j < cells.length; j++) {
                    String c = cells[j].getContents();// 单元格中的内容.
                    if (c.equals("")) {
                        continue;
                    }
                    entity = (entity == null ? clz.newInstance() : entity);// 如果不存在实例则新建.
                    Field field = map.get(j);// 从map中得到对应列的field.
                    //设置私有字段可以被访问
                    field.setAccessible(true);
                    // 取得类型,并根据对象类型设置值.
                    Class<?> fieldType = field.getType();
                    if ((Integer.TYPE == fieldType)
                            || (Integer.class == fieldType)) {
                        field.set(entity, Integer.parseInt(c));
                    } else if (String.class == fieldType) {
                        field.set(entity, String.valueOf(c));
                    } else if ((Long.TYPE == fieldType)
                            || (Long.class == fieldType)) {
                        field.set(entity, Long.valueOf(c));
                    } else if ((Float.TYPE == fieldType)
                            || (Float.class == fieldType)) {
                        field.set(entity, Float.valueOf(c));
                    } else if ((Short.TYPE == fieldType)
                            || (Short.class == fieldType)) {
                        field.set(entity, Short.valueOf(c));
                    } else if ((Double.TYPE == fieldType)
                            || (Double.class == fieldType)) {
                        field.set(entity, Double.valueOf(c));
                    } else if (Character.TYPE == fieldType) {
                        if ((c != null) && (c.length() > 0)) {
                            field.set(entity, Character
                                    .valueOf(c.charAt(0)));
                        }
                    }
                }
                if (entity != null) {
                    list.add(entity);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.debug("[导入excel出现异常]:信息={}",e.getMessage());
        }

        return false;
    }

    /**
     * 将EXCEL中A,B,C,D,E列映射成0,1,2,3
     *
     * @param col
     */
    public static int getExcelCol(String col) {
        col = col.toUpperCase();
        // 从-1开始计算,字母重1开始运算。这种总数下来算数正好相同。
        int count = -1;
        char[] cs = col.toCharArray();
        for (int i = 0; i < cs.length; i++) {
            //从64开始到
            count += (cs[i] - 64) * Math.pow(26, cs.length - 1 - i);
        }
        return count;
    }

    /**
     * 设置单元格上提示
     *
     * @param sheet
     *            要设置的sheet.
     * @param promptTitle
     *            标题
     * @param promptContent
     *            内容
     * @param firstRow
     *            开始行
     * @param endRow
     *            结束行
     * @param firstCol
     *            开始列
     * @param endCol
     *            结束列
     * @return 设置好的sheet.
     */
    public static HSSFSheet setHSSFPrompt(HSSFSheet sheet, String promptTitle,
                                          String promptContent, int firstRow, int endRow, int firstCol,
                                          int endCol) {
        // 构造constraint对象
        DVConstraint constraint = DVConstraint
                .createCustomFormulaConstraint("DD1");
        // 四个参数分别是：起始行、终止行、起始列、终止列
        CellRangeAddressList regions = new CellRangeAddressList(firstRow,
                endRow, firstCol, endCol);
        // 数据有效性对象
        HSSFDataValidation data_validation_view = new HSSFDataValidation(
                regions, constraint);
        data_validation_view.createPromptBox(promptTitle, promptContent);
        sheet.addValidationData(data_validation_view);
        return sheet;
    }

}
