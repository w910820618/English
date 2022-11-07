package org.example;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) throws Exception {

        System.out.println("请输入需要的单词发音类型：0-美式  1-英式");
        Scanner scanner = new Scanner(System.in);
        String targetType = scanner.next();
        //有道api  美式：type=0   英式：type=0
        String baseUrl = "http://dict.youdao.com/dictvoice?type=" + targetType + "&audio=";

        System.out.println("请输入单词txt地址，如： E:\\YouDaoDownload\\ankiword.txt");
        scanner = new Scanner(System.in);
        String targetWordTxt = scanner.next();
        File root = new File(targetWordTxt);
        ArrayList<String> words = excel(targetWordTxt);

        System.out.println("请输入操作系统 类型: 0-MacOS 1-Windows");
        scanner = new Scanner(System.in);
        String ocType = scanner.next();

        String rootDir = "";

        if (ocType.equals("0")) {
            rootDir = root.getParent() + "/words";
        } else {
            rootDir = root.getParent() + "\\words";
        }


        System.out.println("以行为单位读取文件内容，一次读一整行：");
        for (int i = 0; i < words.size(); i++) {
            //默认第一行为标题行，i = 0
            String word = words.get(i);
            String wordUrl = baseUrl + word;
            DownloadUtils downloadUtils = new DownloadUtils(wordUrl, word, "mp3", rootDir);
            try {
                downloadUtils.httpDownload();
                System.out.print("\t \t \t下载成功");
            } catch (Exception e) {
                System.out.print("\t \t \t下载失败");
                e.printStackTrace();
            }
            System.out.println();
        }

    }

    public static ArrayList<String> excel(String path) throws Exception {
        File file = new File(path);
        if (!file.exists()) {
            throw new Exception("文件不存在!");
        }
        InputStream in = new FileInputStream(file);

        // 读取整个Excel
        XSSFWorkbook sheets = new XSSFWorkbook(in);
        // 获取第一个表单Sheet
        XSSFSheet sheetAt = sheets.getSheetAt(0);
        // 循环获取每一行数据
        ArrayList<String> dirverName = new ArrayList<>();
        for (int i = 0; i < sheetAt.getPhysicalNumberOfRows(); i++) {
            //默认第一行为标题行，i = 0
            XSSFRow row = sheetAt.getRow(i);
            if (row != null) {
                dirverName.add(row.getCell(0).toString());
            }
        }
        return dirverName;
    }

}