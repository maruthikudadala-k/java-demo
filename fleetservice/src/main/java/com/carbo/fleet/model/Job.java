package com.carbo.fleet.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Document(collection = "jobs")
@CompoundIndex(def = "{'_id': 1, 'users._id': 1}", name = "job_id_user_id_index", unique = true)
public class Job {
    @Id
    private String id;

    @Field("name")
    @NotEmpty(message = "name can not be empty")
    @Size(max = 100, message = "name can not be more than 100 characters.")
    private String name;

    @Field("jobNumber")
    @NotEmpty(message = "jobNumber can not be empty")
    @Size(max = 14, message = "jobNumber can not be more than 14 characters.")
    private String jobNumber;

    @Field("fleet")
    private String fleet;

    @Field("operator")
    private String operator;

    @Field("pad")
    private String pad;

    @Field("location")
    private String location;

    @Field("zipper")
    private Boolean zipper;

    @Field("proposalId")
    private String proposalId;

    @Field("targetStagesPerDay")
    private int targetStagesPerDay;

    @Field("targetDailyPumpTime")
    private float targetDailyPumpTime;



    @Field("proppantSchematicType")
    private String proppantSchematicType = "silos";

    @Field("numberOfUnits")
    private Integer numberOfUnits = 3;

    @Field("coneLbs")
    private Float coneLbs = 1400.0f;

    @Field("blenders")
    private List<OnSiteEquipment> blenders = new ArrayList<>();
    @Field("ePumps")
    private List<OnSiteEquipment> ePumps = new ArrayList<>();
    @Field("auxTrailers")
    private List<OnSiteEquipment> auxTrailers = new ArrayList<>();
    @Field("boostPumps")
    private List<OnSiteEquipment> boostPumps = new ArrayList<>();
    @Field("cables")
    private List<OnSiteEquipment> cables = new ArrayList<>();
    @Field("chemicalFloats")
    private List<OnSiteEquipment> chemicalFloats = new ArrayList<>();
    @Field("frackLocks")
    private List<OnSiteEquipment> frackLocks = new ArrayList<>();
    @Field("ironFloats")
    private List<OnSiteEquipment> ironFloats = new ArrayList<>();
    @Field("monoLines")
    private List<OnSiteEquipment> monoLines = new ArrayList<>();
    @Field("naturalGasTrailers")
    private List<OnSiteEquipment> naturalGasTrailers = new ArrayList<>();
    @Field("switchGears")
    private List<OnSiteEquipment> switchGears = new ArrayList<>();
    @Field("tractors")
    private List<OnSiteEquipment> tractors = new ArrayList<>();

    @Field("hydrationUnits")
    private List<OnSiteEquipment> hydrationUnits = new ArrayList<>();

    @Field("pumps")
    private List<OnSiteEquipment> pumps = new ArrayList<>();

    @Field("chemAds")
    private List<OnSiteEquipment> chemAds = new ArrayList<>();

    @Field("ironManifolds")
    private List<OnSiteEquipment> ironManifolds = new ArrayList<>();

    @Field("dataVans")
    private List<OnSiteEquipment> dataVans = new ArrayList<>();

    @Field("silos")
    private List<OnSiteEquipment> silos = new ArrayList<>();

    @Field("users")
    private List<User> users = new ArrayList<>();

    @Field("curWellId")
    private String curWellId;

    @Field("curStage")
    private String curStage;

    @Field("startDate")
    private Long startDate;

    @Field("startDateStr")
    private String startDateStr;

    @Field("timezone")
    private String timezone;

    @Field("discounts")
    private Map<String, Float> discounts = new HashMap<>();

    @Field("organizationId")
    @Indexed
    private String organizationId;

    @Field("status")
    private String status;

    @Field("beltDirection")
    private String beltDirection = "left";

    @Field("mileageChargeDistance")
    private Integer mileageChargeDistance = 0;

    @Field("activityLogStartTime")
    private String activityLogStartTime = "00:00";

    @Field("wellheadCo")
    private String wellheadCo;

    @Field("wirelineCo")
    private String wirelineCo;

    @Field("waterTransferCo")
    private String waterTransferCo;

    @Field("goToMeetingId")
    private String goToMeetingId;

    @Field("includeToeStage")
    private Boolean includeToeStage;

    @Field("predefinedChannels")
    private List<String> predefinedChannels;


    @Field("sharedWithOrganizationId")
    private String sharedWithOrganizationId;

    @JsonIgnore
    private Integer padStageTotal;

    @Field("ts")
    private Long ts;

    @Field("rts")
    private Long rts;

    @Field("serviceCompany")
    private String serviceCompany;

    @Field("disableOffline")
    private Boolean disableOffline;

    @Field("created")
    private Long created = new Date().getTime();

    @Field("modified")
    private Long modified = new Date().getTime();

    @Field("backupDate")
    @Indexed
    private Date backupDate;

    @Field("lastModifiedBy")
    private String lastModifiedBy;

    @Field("connectJobTime")
    private boolean connectJobTime;

    @Field("automatize")
    private boolean automatize;

    @Field("additionalJobsFieldTicket")
    private List<String> additionalJobsFieldTicket = new ArrayList<>();

    @Field("metric")
    private boolean metric;


    @Field("hpp")
    private String hpp;

    @Field("mpn")
    private String mpn;

    @Field("connector")
    private String connector;

    @Field("singleOrDouble")
    private String singleOrDouble;

    @Field("length")
    private String length;

    @Field("manufacturer")
    private String manufacturer;

    @Field("btu")
    private float btu;

    @Field("swapOverTime")
    private float swapOverTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJobNumber() {
        return jobNumber;
    }

    public void setJobNumber(String jobNumber) {
        this.jobNumber = jobNumber;
    }

    public String getFleet() {
        return fleet;
    }

    public void setFleet(String fleet) {
        this.fleet = fleet;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getPad() {
        return pad;
    }

    public void setPad(String pad) {
        this.pad = pad;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Boolean getZipper() {
        return zipper;
    }

    public void setZipper(Boolean zipper) {
        this.zipper = zipper;
    }

    public String getProposalId() {
        return proposalId;
    }

    public void setProposalId(String proposalId) {
        this.proposalId = proposalId;
    }

    public int getTargetStagesPerDay() {
        return targetStagesPerDay;
    }

    public void setTargetStagesPerDay(int targetStagesPerDay) {
        this.targetStagesPerDay = targetStagesPerDay;
    }

    public float getTargetDailyPumpTime() {
        return targetDailyPumpTime;
    }

    public void setTargetDailyPumpTime(float targetDailyPumpTime) {
        this.targetDailyPumpTime = targetDailyPumpTime;
    }




    public String getProppantSchematicType() {
        return proppantSchematicType;
    }

    public void setProppantSchematicType(String proppantSchematicType) {
        this.proppantSchematicType = proppantSchematicType;
    }



    public List<OnSiteEquipment> getBlenders() {
        return blenders;
    }

    public void setBlenders(List<OnSiteEquipment> blenders) {
        this.blenders = blenders;
    }

    public List<OnSiteEquipment> getHydrationUnits() {
        return hydrationUnits;
    }

    public void setHydrationUnits(List<OnSiteEquipment> hydrationUnits) {
        this.hydrationUnits = hydrationUnits;
    }

    public List<OnSiteEquipment> getPumps() {
        return pumps;
    }

    public void setPumps(List<OnSiteEquipment> pumps) {
        this.pumps = pumps;
    }

    public List<OnSiteEquipment> getChemAds() {
        return chemAds;
    }

    public void setChemAds(List<OnSiteEquipment> chemAds) {
        this.chemAds = chemAds;
    }

    public List<OnSiteEquipment> getIronManifolds() {
        return ironManifolds;
    }

    public void setIronManifolds(List<OnSiteEquipment> ironManifolds) {
        this.ironManifolds = ironManifolds;
    }

    public List<OnSiteEquipment> getDataVans() {
        return dataVans;
    }

    public void setDataVans(List<OnSiteEquipment> dataVans) {
        this.dataVans = dataVans;
    }

    public List<OnSiteEquipment> getSilos() {
        return silos;
    }

    public void setSilos(List<OnSiteEquipment> silos) {
        this.silos = silos;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public String getCurWellId() {
        return curWellId;
    }

    public void setCurWellId(String curWellId) {
        this.curWellId = curWellId;
    }

    public String getCurStage() {
        return curStage;
    }

    public void setCurStage(String curStage) {
        this.curStage = curStage;
    }

    public Long getTs() {
        return ts;
    }

    public void setTs(Long ts) {
        this.ts = ts;
    }

    public Long getRts() {
        return rts;
    }

    public void setRts(Long rts) {
        this.rts = rts;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public Long getStartDate() {
        return startDate;
    }

    public void setStartDate(Long startDate) {
        this.startDate = startDate;
    }

    public Integer getNumberOfUnits() {
        return numberOfUnits;
    }

    public void setNumberOfUnits(Integer numberOfUnits) {
        this.numberOfUnits = numberOfUnits;
    }

    public Map<String, Float> getDiscounts() {
        return discounts;
    }

    public void setDiscounts(Map<String, Float> discounts) {
        this.discounts = discounts;
    }

    public Long getCreated() {
        return created;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Float getConeLbs() {
        return coneLbs;
    }

    public void setConeLbs(Float coneLbs) {
        this.coneLbs = coneLbs;
    }

    public String getBeltDirection() {
        return beltDirection;
    }

    public void setBeltDirection(String beltDirection) {
        this.beltDirection = beltDirection;
    }

    public Integer getMileageChargeDistance() {
        return mileageChargeDistance;
    }

    public void setMileageChargeDistance(Integer mileageChargeDistance) {
        this.mileageChargeDistance = mileageChargeDistance;
    }

    public String getWellheadCo() {
        return wellheadCo;
    }

    public void setWellheadCo(String wellheadCo) {
        this.wellheadCo = wellheadCo;
    }

    public String getWirelineCo() {
        return wirelineCo;
    }

    public void setWirelineCo(String wirelineCo) {
        this.wirelineCo = wirelineCo;
    }

    public String getActivityLogStartTime() {
        return activityLogStartTime;
    }

    public void setActivityLogStartTime(String activityLogStartTime) {
        this.activityLogStartTime = activityLogStartTime;
    }

    public Long getModified() {
        return modified;
    }

    public void updateModified() {
        this.modified = new Date().getTime();
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public String getGoToMeetingId() {
        return goToMeetingId;
    }

    public void setGoToMeetingId(String goToMeetingId) {
        this.goToMeetingId = goToMeetingId;
    }

    public Boolean getIncludeToeStage() {
        return includeToeStage;
    }

    public void setIncludeToeStage(Boolean includeToeStage) {
        this.includeToeStage = includeToeStage;
    }

    public Date getBackupDate() {
        return backupDate;
    }

    public void setBackupDate(Date backupDate) {
        this.backupDate = backupDate;
    }

    public String getStartDateStr() {
        return startDateStr;
    }

    public void setStartDateStr(String startDateStr) {
        this.startDateStr = startDateStr;
    }

    public Boolean getDisableOffline() {
        return disableOffline;
    }

    public void setDisableOffline(Boolean disableOffline) {
        this.disableOffline = disableOffline;
    }



    public List<String> getPredefinedChannels() {
        return predefinedChannels;
    }

    public void setPredefinedChannels(List<String> predefinedChannels) {
        this.predefinedChannels = predefinedChannels;
    }

    public String getSharedWithOrganizationId() {
        return sharedWithOrganizationId;
    }

    public void setSharedWithOrganizationId(String sharedWithOrganizationId) {
        this.sharedWithOrganizationId = sharedWithOrganizationId;
    }

    public String getServiceCompany() {
        return serviceCompany;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public void setServiceCompany(String serviceCompany) {
        this.serviceCompany = serviceCompany;
    }

    public boolean isConnectJobTime() {
        return connectJobTime;
    }

    public void setConnectJobTime(boolean connectJobTime) {
        this.connectJobTime = connectJobTime;
    }

    public boolean isAutomatize() {
        return automatize;
    }

    public void setAutomatize(boolean automatize) {
        this.automatize = automatize;
    }

    public List<String> getAdditionalJobsFieldTicket() {
        return additionalJobsFieldTicket;
    }

    public void setAdditionalJobsFieldTicket(List<String> additionalJobsFieldTicket) {
        this.additionalJobsFieldTicket = additionalJobsFieldTicket;
    }


    public boolean isMetric() {
        return metric;
    }

    public void setMetric(boolean metric) {
        this.metric = metric;
    }

    public void setPadStageTotal(Integer padStageTotal) {
        this.padStageTotal = padStageTotal;
    }

    public void setCreated(Long created) {
        this.created = created;
    }

    public void setModified(Long modified) {
        this.modified = modified;
    }

    public String getHpp() {
        return hpp;
    }

    public void setHpp(String hpp) {
        this.hpp = hpp;
    }

    public String getMpn() {
        return mpn;
    }

    public void setMpn(String mpn) {
        this.mpn = mpn;
    }

    public String getConnector() {
        return connector;
    }

    public void setConnector(String connector) {
        this.connector = connector;
    }

    public String getSingleOrDouble() {
        return singleOrDouble;
    }

    public void setSingleOrDouble(String singleOrDouble) {
        this.singleOrDouble = singleOrDouble;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public List<OnSiteEquipment> getePumps() {
        return ePumps;
    }

    public void setePumps(List<OnSiteEquipment> ePumps) {
        this.ePumps = ePumps;
    }

    public List<OnSiteEquipment> getAuxTrailers() {
        return auxTrailers;
    }

    public void setAuxTrailers(List<OnSiteEquipment> auxTrailers) {
        this.auxTrailers = auxTrailers;
    }

    public List<OnSiteEquipment> getBoostPumps() {
        return boostPumps;
    }

    public void setBoostPumps(List<OnSiteEquipment> boostPumps) {
        this.boostPumps = boostPumps;
    }

    public List<OnSiteEquipment> getCables() {
        return cables;
    }

    public void setCables(List<OnSiteEquipment> cables) {
        this.cables = cables;
    }

    public List<OnSiteEquipment> getChemicalFloats() {
        return chemicalFloats;
    }

    public void setChemicalFloats(List<OnSiteEquipment> chemicalFloats) {
        this.chemicalFloats = chemicalFloats;
    }

    public List<OnSiteEquipment> getFrackLocks() {
        return frackLocks;
    }

    public void setFrackLocks(List<OnSiteEquipment> frackLocks) {
        this.frackLocks = frackLocks;
    }

    public List<OnSiteEquipment> getIronFloats() {
        return ironFloats;
    }

    public void setIronFloats(List<OnSiteEquipment> ironFloats) {
        this.ironFloats = ironFloats;
    }

    public List<OnSiteEquipment> getMonoLines() {
        return monoLines;
    }

    public void setMonoLines(List<OnSiteEquipment> monoLines) {
        this.monoLines = monoLines;
    }

    public List<OnSiteEquipment> getNaturalGasTrailers() {
        return naturalGasTrailers;
    }

    public void setNaturalGasTrailers(List<OnSiteEquipment> naturalGasTrailers) {
        this.naturalGasTrailers = naturalGasTrailers;
    }

    public List<OnSiteEquipment> getSwitchGears() {
        return switchGears;
    }

    public void setSwitchGears(List<OnSiteEquipment> switchGears) {
        this.switchGears = switchGears;
    }

    public List<OnSiteEquipment> getTractors() {
        return tractors;
    }

    public void setTractors(List<OnSiteEquipment> tractors) {
        this.tractors = tractors;
    }

    public String getWaterTransferCo() {
        return waterTransferCo;
    }

    public void setWaterTransferCo(String waterTransferCo) {
        this.waterTransferCo = waterTransferCo;
    }

    public float getBtu() {
        return btu;
    }

    public void setBtu(float btu) {
        this.btu = btu;
    }

    public float getSwapOverTime() {
        return swapOverTime;
    }

    public void setSwapOverTime(float swapOverTime) {
        this.swapOverTime = swapOverTime;
    }
}
