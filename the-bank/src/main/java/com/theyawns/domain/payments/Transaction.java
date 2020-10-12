package com.theyawns.domain.payments;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.theyawns.Constants;
import com.theyawns.ruleengine.HasID;

import java.io.IOException;
import java.io.Serializable;

public class Transaction implements /*IdentifiedDataSerializable,*/ Serializable, HasID {

    private String transactionId;
    private String acctNumber;      // TODO: this won't be part of object eventually - txn id keys
    private String merchantId;

    //private long timestamp;
    private Double amount = 0.0;
    private String location; // Should this be a separate object, enrichment source?

    private int fraudResult = -1;
    private Boolean paymentResult = Boolean.TRUE;
    private int ruleSetsToApply;

    private long timeEnqueuedForRuleEngine; // nanotime
    private long timeEnqueuedForAggregator; // nanotime
    private long timeSpentQueued; // sum of RE + Aggregator

    // Being a little sloppy with encapsulation, will allow direct access to these
//    public LatencyMetric processingTime = new LatencyMetric();
//    public LatencyMetric endToEndTime = new LatencyMetric();

    // No-arg constructor for use by serialization
    public Transaction() {
        //timestamp = System.currentTimeMillis();
    }

    public Transaction(String txnId) {
        this.transactionId = txnId;
    }

    @Deprecated
    public Transaction(int num) {
        this();
        this.transactionId = ""+num;
    }

    public Transaction(Transaction copyfrom) {
        this();
        this.acctNumber = copyfrom.acctNumber;
        this.transactionId = copyfrom.transactionId;
        this.merchantId = copyfrom.merchantId;
        this.amount = copyfrom.amount;
    }

    public String getItemID() {
        return transactionId;
    }
    public void setItemID(String id) { transactionId = id; }

    public void setAccountNumber(String acct) { this.acctNumber = acct; }
    public String getAccountNumber() {
        return acctNumber;
    }

    public void setAmount(Double amt) { this.amount = amt; }
    public Double getAmount() {
        return amount;
    }

    public void setMerchantId(String id) {
        this.merchantId = id;
    }
    public String getMerchantId() { return merchantId; }

    public void setLocation(String l) { this.location = l; }
    public String getLocation() { return location; }

    public void setFraudResult(int result) {
        fraudResult = result;
    }
    public int getFraudResult() {
        return fraudResult;
    }

    public Boolean getPaymentResult() {
        return paymentResult;
    }
    public void setPaymentResult(boolean result) {
        paymentResult = result;
    }

    public void setRuleSetsToApply(int count) { ruleSetsToApply = count; }
    public int getRuleSetsToApply() { return ruleSetsToApply; }

    //public void setTimeEnqueuedForRuleEngine(long time) { timeEnqueuedForRuleEngine = time; }
    public void setTimeEnqueuedForRuleEngine() { timeEnqueuedForRuleEngine = System.nanoTime(); }
    public long getTimeEnqueuedForRuleEngine() { return timeEnqueuedForRuleEngine; }

    public void setTimeEnqueuedForAggregator() { timeEnqueuedForAggregator = System.nanoTime(); }
    public long getTimeEnqueuedForAggregator() { return timeEnqueuedForAggregator; }

    public void addToQueueWaitTime(long value) {
        timeSpentQueued += value;
    }
    public long getQueueWaitTime() {
        return timeSpentQueued;
    }
    @Override
    public String toString() {
        return "Transaction " + transactionId + " account " + acctNumber + " merchant " + merchantId + " amount " + amount;
    }

    // IdentifiedDataSerializable implementation

    //@Override
    public int getFactoryId() {
        return Constants.IDS_FACTORY_ID;
    }

    //@Override
    public int getClassId() {
        return Constants.IDS_TRANSACTION_ID;
    }

    //@Override
    public void writeData(ObjectDataOutput objectDataOutput) throws IOException {
        objectDataOutput.writeUTF(transactionId);
        objectDataOutput.writeUTF(acctNumber);
        objectDataOutput.writeUTF(merchantId);
        objectDataOutput.writeDouble(amount);
        objectDataOutput.writeUTF(location);
        objectDataOutput.writeInt(fraudResult);
        objectDataOutput.writeBoolean(paymentResult);
        objectDataOutput.writeInt(ruleSetsToApply);
        objectDataOutput.writeLong(timeEnqueuedForRuleEngine);
        objectDataOutput.writeLong(timeEnqueuedForAggregator);
        objectDataOutput.writeLong(timeSpentQueued);

//        objectDataOutput.writeObject(processingTime);
//        objectDataOutput.writeObject(endToEndTime);
    }

    //@Override
    public void readData(ObjectDataInput objectDataInput) throws IOException {
        transactionId = objectDataInput.readUTF();
        acctNumber = objectDataInput.readUTF();
        merchantId = objectDataInput.readUTF();
        amount = objectDataInput.readDouble();
        location = objectDataInput.readUTF();
        fraudResult = objectDataInput.readInt();
        paymentResult = objectDataInput.readBoolean();
        ruleSetsToApply = objectDataInput.readInt();
        timeEnqueuedForRuleEngine = objectDataInput.readLong();
        timeEnqueuedForAggregator = objectDataInput.readLong();
        timeSpentQueued = objectDataInput.readLong();

//        processingTime = objectDataInput.readObject(LatencyMetric.class);
//        endToEndTime = objectDataInput.readObject(LatencyMetric.class);
    }
}
