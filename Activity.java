package com.peter.foward;


import java.util.Date;

public class Activity {
    //completed
    private int id_completed;
    private Date DateStarted;
    private Date DateCompleted;
    private Float Returns;
    private String Benefits;
    private String AssociatedPeople;
    private String AssociatedBusiness;
    private Float Cost;
    private String OtherResources;
    private String Procedure;

    //plan
    private Long idPlan;
    private Date DatePlannedStart;
    private Date DatePlannedEnd;
    private Float AnticipatedCost;
    private Float AnticipatedReturns;
    private String AnticipatedBenefits;

    //template
    private  long id;
    private String name;
    private String category;
    private String subcategory;
    private Float AvgReturnPerMonth;
    private String SuccessIndexList;
    private Float AvgTimeTaken;
    private Float TimeChange;

    public Activity(int id_completed, Date DateStarted, Date DateCompleted, Float Returns,
                   String Benefits, String AssociatedPeople, String AssociatedBusiness, Float Cost,
                   String OtherResources, String Procedure, Long idPlan, Date DatePlannedStart,
                   Date DatePlannedEnd,Float AnticipatedCost, Float AnticipatedReturns, String AnticipatedBenefits,
                   long id, String name, String category, String subcategory, Float AvgReturnPerMonth,
                   String SuccessIndexList, Float AvgTimeTaken, Float TimeChange) {
        this.id_completed = id_completed;
        this.DateStarted = DateStarted;
        this.DateCompleted = DateCompleted;
        this.Returns = Returns;
        this.Benefits = Benefits;
        this.AssociatedPeople = AssociatedPeople;
        this.AssociatedBusiness = AssociatedBusiness;
        this.Cost = Cost;
        this.OtherResources = OtherResources;
        this.Procedure = Procedure;
        this.idPlan = idPlan;
        this.DatePlannedStart = DatePlannedStart;
        this.DatePlannedEnd = DatePlannedEnd;
        this.AnticipatedCost=AnticipatedCost;
        this.AnticipatedReturns = AnticipatedReturns;
        this.AnticipatedBenefits = AnticipatedBenefits;
        this.id = id;
        this.name = name;
        this.category = category;
        this.subcategory = subcategory;
        this.AvgReturnPerMonth = AvgReturnPerMonth;
        this.SuccessIndexList = SuccessIndexList;
        this.AvgTimeTaken = AvgTimeTaken;
        this.TimeChange = TimeChange;
    }

    public Activity() {

    }

    // Setters and Getters
    public int getId_completed() {
        return id_completed;
    }

    public void setId_completed(int id_completed) {
        this.id_completed = id_completed;
    }

    public Date getDateStarted() {
        return DateStarted;
    }

    public void setDateStarted(Date DateStarted) {
        this.DateStarted = DateStarted;
    }

    public Date getDateCompleted() {
        return DateCompleted;
    }

    public void setDateCompleted(Date DateCompleted) {
        this.DateCompleted = DateCompleted;
    }

    public Float getReturns() {
        return Returns;
    }

    public void setReturns(Float Returns) {
        this.Returns = Returns;
    }

    public String getBenefits() {
        return Benefits;
    }

    public void setBenefits(String Benefits) {
        this.Benefits = Benefits;
    }

    public String getAssociatedPeople() {
        return AssociatedPeople;
    }

    public void setAssociatedPeople(String AssociatedPeople) {
        this.AssociatedPeople = AssociatedPeople;
    }

    public String getAssociatedBusiness() {
        return AssociatedBusiness;
    }

    public void setAssociatedBusiness(String AssociatedBusiness) {
        this.AssociatedBusiness = AssociatedBusiness;
    }

    public Float getCost() {
        return Cost;
    }

    public void setCost(Float Cost) {
        this.Cost = Cost;
    }
    public Float getAnticipatedCost() {
        return AnticipatedCost;
    }

    public void setAnticipatedCost(Float AnticipatedCost) {
        this.AnticipatedCost = AnticipatedCost;
    }

    public String getOtherResources() {
        return OtherResources;
    }

    public void setOtherResources(String OtherResources) {
        this.OtherResources = OtherResources;
    }

    public String getProcedure() {
        return Procedure;
    }

    public void setProcedure(String Procedure) {
        this.Procedure = Procedure;
    }

    public Long getIdPlan() {
        return idPlan;
    }

    public void setIdPlan(Long idPlan) {
        this.idPlan = idPlan;
    }

    public Date getDatePlannedStart() {
        return DatePlannedStart;
    }

    public void setDatePlannedStart(Date DatePlannedStart) {
        this.DatePlannedStart = DatePlannedStart;
    }

    public Date getDatePlannedEnd() {
        return DatePlannedEnd;
    }

    public void setDatePlannedEnd(Date DatePlannedEnd) {
        this.DatePlannedEnd = DatePlannedEnd;
    }

    public Float getAnticipatedReturns() {
        return AnticipatedReturns;
    }

    public void setAnticipatedReturns(Float AnticipatedReturns) {
        this.AnticipatedReturns = AnticipatedReturns;
    }

    public String getAnticipatedBenefits() {
        return AnticipatedBenefits;
    }

    public void setAnticipatedBenefits(String AnticipatedBenefits) {
        this.AnticipatedBenefits = AnticipatedBenefits;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

    public Float getAvgReturnPerMonth() {
        return AvgReturnPerMonth;
    }

    public void setAvgReturnPerMonth(Float AvgReturnPerMonth) {
        this.AvgReturnPerMonth = AvgReturnPerMonth;
    }

    public String getSuccessIndexList() {
        return SuccessIndexList;
    }

    public void setSuccessIndexList(String SuccessIndexList) {
        this.SuccessIndexList = SuccessIndexList;
    }

    public Float getAvgTimeTaken() {
        return AvgTimeTaken;
    }

    public void setAvgTimeTaken(Float AvgTimeTaken) {
        this.AvgTimeTaken = AvgTimeTaken;
    }

    public Float getTimeChange() {
        return TimeChange;
    }

    public void setTimeChange(Float TimeChange) {
        this.TimeChange = TimeChange;
    }
}
