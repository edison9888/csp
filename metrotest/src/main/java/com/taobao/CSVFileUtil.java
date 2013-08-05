package com.taobao;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class CSVFileUtil {
	public static void readFile(String fileName) throws Exception {
		DataInputStream in = new DataInputStream(new FileInputStream(new File(fileName)));
		BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(in,"utf-8"));
		String stemp;   
		while((stemp = bufferedreader.readLine()) != null){  
			System.out.println(stemp + "\t" + stemp.split(","));
		}
	}
	
	public static void main(String[] args) throws Exception {
		CSVFileUtil.readFile("F://data//tmp_auctions_cat.csv");
	}
}
