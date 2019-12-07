package com.gitee.randomobject;

import com.gitee.randomobject.entity.LcInfo;
import com.gitee.randomobject.utils.ExcelUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RunApp {


    public static void main(String[] args) throws IOException {
        ExcelUtil<LcInfo> lcInfoExcelUtil = new ExcelUtil<>();
//        lcInfoExcelUtil.importExcel(LcInfo.class, " ","");
        List<LcInfo> list = new ArrayList<>();
        int i =99;
        for (;;){
            LcInfo lcInfo = new LcInfo();
            lcInfo.setAppName(String.valueOf(i));
            lcInfo.setEmailpass(String.valueOf(i));
            lcInfo.setPassword(String.valueOf(i));
            lcInfo.setUsername(String.valueOf(i));
            list.add(lcInfo);
            if (i<=0) break;
            i--;
        }
        lcInfoExcelUtil.exportExcel(LcInfo.class, list,"lc账号列表", 999,new FileOutputStream(new File("D:\\第一次.xls")), false);
    }
}
