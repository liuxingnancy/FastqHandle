package fqSplit;

public class FastqRecord {
	private String readname;
	private String sequence;
	private String quality;
	private String readinfo;
	
	public FastqRecord(String readname, String readinfo, String sequence, String quality){
		this.readname=readname;
		this.readinfo=readinfo;
		this.sequence=sequence;
		this.quality=quality;
	}
	public FastqRecord(){
		
	}
	public void SetReadname(String readname){
		this.readname=readname;
	}
	public void SetSequence(String sequence){
		this.sequence=sequence;
	}
	public void SetQuality(String quality){
		this.quality=quality;
	}
	public void SetReadinfo(String readinfo){
		this.readinfo=readinfo;
	}
	public String GetReadname(){
		return this.readname;
	}
	public String GetSequence(){
		return this.sequence;
	}
	public String GetQuqality(){
		return this.quality;
	}
	public String GetReadinfo(){
		return this.readinfo;
	}
	public String toString(){
		return "@"+this.readname+" "+this.readinfo+"\n"+this.sequence+"\n+\n"+this.quality+"\n";
	}
}
