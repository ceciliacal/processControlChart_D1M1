package projectchart;

public class Data {
	
	private Integer year;
	private Integer month;
	private Integer count;
	
	public Data(int myYear, int myMonth, int myCount) {
		setYear(myYear);
		setMonth(myMonth);
		setCount(myCount);
		
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public Integer getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	
}
