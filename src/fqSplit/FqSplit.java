package fqSplit;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import java.util.zip.GZIPOutputStream;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

public class FqSplit {
	
	private static long maxnumber=150000000L;
	private static long number = 1L;
	
	public static Options options = new Options();
	
	static {
		options.addOption("h", "help", false, "The help information list.");
		options.addOption("f", "fastq", true, "The fastq format file list that contains the sequencing information, you can give more than one file with format: file1.fq,file2.fq");
		options.addOption("o", "out", true, "The output file prefix");
		options.addOption("x", "xtimes", true, "The times of fq data that will be saved, you can choose multiple number with format: 3,2,1");
	}
	
	public static void printHelp(Options options) {
		HelpFormatter hf=new HelpFormatter();
		hf.printHelp("cut the fq file into small files", options);
	}
	
	public static void main(String[] args) {
		CommandLineParser parser = new PosixParser();
		CommandLine cl = null;
		try {
			cl = parser.parse(options, args);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if (cl.hasOption("h") || !cl.hasOption("f") || !cl.hasOption("o")) {
			printHelp(options);
			System.exit(1);
		}
		
		String fqfiles = cl.getOptionValue("f");
		List<String> fqfilelist = new ArrayList<String>();
		for (String fqfile: fqfiles.split(",")) {
			fqfilelist.add(fqfile);
		}
		String outprefix = cl.getOptionValue("o");
		String xtimes = cl.hasOption("x") ? cl.getOptionValue("x") : "3,2,1";
		HashMap<Long, OutputStreamWriter> x = new HashMap<Long, OutputStreamWriter>();
		for (String xtime: xtimes.split(",")) {
			Long key = (long) ((Float.parseFloat(xtime)*30000000L)/2);
			if (key > maxnumber) {
				maxnumber = key;
			}
			String outfilename = outprefix + "." + xtime + "x.fq.gz"; 
			OutputStreamWriter outwriter = null;
			try {
				outwriter = new OutputStreamWriter(new GZIPOutputStream(new FileOutputStream(outfilename)), "UTF-8");
			} catch (IOException e) {
				e.printStackTrace();
			}
			x.put(key, outwriter);
		}
		
		for (String fqfile: fqfilelist) {
			Fastq fastq = null;
			try {
				fastq = new Fastq(fqfile);			
	
			while(fastq.hasNext()) {
				FastqRecord fr = fastq.nextRecord();
				Iterator<Entry<Long, OutputStreamWriter>> xiterator = x.entrySet().iterator();
				while(xiterator.hasNext()) {
					Entry<Long, OutputStreamWriter> entry = xiterator.next();
					Thread thread = new Thread(new Runnable() {
						public void run() {
							if(number <= entry.getKey()) {
								try {
									entry.getValue().write(fr.toString());
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						}
					});
					thread.run();						
				}
				if (number > maxnumber) break;
				number += 1;
				
			}
			
			if (number > maxnumber) break;
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		Iterator<Entry<Long, OutputStreamWriter>> xiterator = x.entrySet().iterator();
		while (xiterator.hasNext()) {
			try {
				xiterator.next().getValue().close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}

}
