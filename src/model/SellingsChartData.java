
package model;

public class SellingsChartData {
    
    private String date ;
    private Double sellings;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Double getSellings() {
        return sellings;
    }

    public void setSellings(Double sellings) {
        this.sellings = sellings;
    }

    public SellingsChartData(String date, Double sellings) {
        this.date = date;
        this.sellings = sellings;
    }

 


    
    
}
