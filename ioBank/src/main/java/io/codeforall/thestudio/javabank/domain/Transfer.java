package io.codeforall.thestudio.javabank.domain;

public class Transfer {

    private Integer srcId;
    private Integer dstId;
    private Double amount;

    public Integer getDstId() {
        return dstId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Integer getSrcId() {
        return srcId;
    }

    public void setSrcId(Integer srcId) {
        this.srcId = srcId;
    }

    public void setDstId(Integer dstId) {
        this.dstId = dstId;
    }
}
