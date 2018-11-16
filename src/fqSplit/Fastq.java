package fqSplit;

import java.io.*;
import java.util.Scanner;
import java.util.zip.GZIPInputStream;

public class Fastq {
	private String FastqFile;
	private Scanner reader;
	public Fastq(){
		
	}
	public Fastq(String FastqFile) throws IOException{
		this.FastqFile=FastqFile;
		this.reader=new Scanner(new GZIPInputStream(new FileInputStream(FastqFile)));
	}
	
	
	public void SetFileName(String FastqFile){
		this.FastqFile=FastqFile;
	}
	public void SetReader() throws IOException{
		this.reader=new Scanner(new GZIPInputStream(new FileInputStream(this.FastqFile)));
	}
	public String GetFileName(){
		return this.FastqFile;	
	}
	public FastqRecord nextRecord() throws FileNotFoundException{
		String line1=reader.nextLine();
		String line2=reader.nextLine();
		String line3=reader.nextLine();
		String line4=reader.nextLine();
		String[] num=line1.trim().split(" ");
		String readname=num[0].replaceAll("@", "");
		String readinfo=num.length >1 ? num[1] : "";
		String sequence=line2.trim();
		String quality=line4.trim();
		FastqRecord fr=new FastqRecord(readname, readinfo, sequence, quality);
		return fr;
	}
	public boolean hasNext() throws IOException{
		return this.reader.hasNextLine();
	}

}