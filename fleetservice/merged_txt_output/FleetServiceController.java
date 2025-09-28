// ===== Imported from: com.carbo.fleet.model.Fleet =====
package com.carbo.fleet.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Set;

@Document(collection = "fleets")
@CompoundIndex(def = "{'organizationId': 1, 'name': 1}", name = "organization_name_index", unique = true)
public class Fleet {
    @Id
    private String id;

    @Field("name")
    @NotEmpty(message = "name can not be empty")
    @Size(max = 100, message = "name can not be more than 100 characters.")
    String name;

    @Field("districtId")
    @NotEmpty(message = "district ID can not be empty")
    @Size(max = 100, message = "district ID can not be more than 100 characters.")
    String districtId;

    @Field("fleetType")
    private String fleetType;

    @Field("created")
    private Long created = new Date().getTime();

    @Field("modified")
    private Long modified  = new Date().getTime();

    @Field("ts")
    private Long ts;

    @Field("organizationId")
    private String organizationId;

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

    public Long getTs() {
        return ts;
    }

    public void setTs(Long ts) {
        this.ts = ts;
    }

    public String getDistrictId() {
        return districtId;
    }

    public void setDistrictId(String districtId) {
        this.districtId = districtId;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public Long getCreated() {
        return created;
    }

    public String getFleetType() {
        return fleetType;
    }

    public void setFleetType(String fleetType) {
        this.fleetType = fleetType;
    }
}

// ===== Imported from: com.carbo.fleet.model.Job =====
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

// ===== Imported from: com.carbo.fleet.model.OnSiteEquipment =====
package com.carbo.fleet.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Document(collection = "on-site-equipments")
//@CompoundIndexes({@CompoundIndex(name = "unique_name_organizationId_index", def = "{'name': 1, 'organizationId': 1}", unique = true)})
public class OnSiteEquipment {
    @Id
    private String id;

    @Field("name")
    @NotEmpty (message = "name can not be empty")
    @NotNull (message = "name can not be Null")
    private String name;

    @Field("dfName")
    private String dfName;

    @Field("type")
    @NotEmpty(message = "subtype can not be empty")
    @NotNull(message = "subtype can not be null")
    private String type;

    @Field("fleetId")
    private String fleetId;

    @Field("location")
    private String location;

    @Field("standby")
    private boolean standby;

    @Field("tier")
    private String tier;

    @Field("engines")
    private String engines;

    @Field("engineHours")
    private Integer engineHour;

    @Field("duelFuel")
    private boolean duelFuel;

    @Field("eku")
    private boolean eku;

    @Field("aoi")
    private boolean aoi;

    @Field("size")
    private Float size;

    @Field("plungerSize")
    private String plungerSize;

    @Field("wireless")
    private boolean wireless;

    @Field("engineRebuild")
    private boolean engineRebuild;

    @Field("status")
    private String status;

    @Field("yardStatus")
    private String yardStatus;

    @Field("hardDownStatus")
    private String hardDownStatus;

    @Field("comments")
    private String comments;

    @Field("ts")
    private Long ts;

    @Field("created")
    private Long created = new Date().getTime();

    @Field("modified")
    private Long modified = new Date().getTime();

    @Field("organizationId")
    private String organizationId;

    @Field("currentHour")
    private Float currentHour;

    @Field("modifiedBy")
    private String modifiedBy;

    @Field("newAddStatus")
    private String newAddStatus;

    @Field("date")
    private String date;

    @Field("transmission")
    private String transmission;

    @Field("strokeLength")
    private String strokeLength;

    @Field("noOfPlungers")
    private String noOfPlungers;

    @Field("trailerAxles")
    private String trailerAxles;

    @Field("pumpIronBrandColor")
    private String pumpIronBrandColor;

    @Field("pumpStopped")
    private boolean pumpStopped = true;

    @Field("lastModifiedBy")
    private String lastModifiedBy;

    @Field("fluidEndBrand")
    private String fluidEndBrand;

    @Field("cleanHour")
    private Float cleanHour;

    @Field("dirtyHour")
    private Float dirtyHour;

    @Field("standByHour")
    private Float standByHour;

    @Field("referenceId")
    private String referenceId;

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

    @Field("horsepower")
    private String horsepower;

    @Field("rental")
    private String rental;

    @Field("blenderRate")
    private String blenderRate;

    @Field("controls")
    private String controls;

    @Field("hhp")
    private String hhp;

    @Field ("standbyPump")
    private boolean standbyPump = false;

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Long getModified() {
        return modified;
    }

    public void setModified(Long modified) {
        this.modified = modified;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public void updateModified() {
        this.modified = new Date().getTime();
    }

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
        if (name != null && !name.trim().isEmpty()&&!name.trim().equalsIgnoreCase("null")) {
            this.name = name;
        } else {
            throw new IllegalArgumentException("onsite_name cannot be null, empty, or  \"null\" ");
        }
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        if (type != null && !type.trim().isEmpty() && !type.trim().equalsIgnoreCase("null") && !type.trim().equalsIgnoreCase("undefineds")) {
            this.type = type;
        } else {
            throw new IllegalArgumentException("onsite_type can not be null, empty or \"null\""); }
    }

    public String getFleetId() {
        return fleetId;
    }

    public void setFleetId(String fleetId) {
        this.fleetId = fleetId;
    }

    public boolean getStandby() {
        return standby;
    }

    public boolean isStandby() {
        return standby;
    }

    public void setStandby(boolean standby) {
        this.standby = standby;
    }

    public Float getCurrentHour() {
        return currentHour;
    }

    public void setCurrentHour(Float currentHour) {
        this.currentHour = currentHour;
    }

    public String getTier() {
        return tier;
    }

    public void setTier(String tier) {
        this.tier = tier;
    }

    public String getDfName() {
        return dfName;
    }

    public void setDfName(String dfName) {
        this.dfName = dfName;
    }

    public String getEngines() {
        return engines;
    }

    public void setEngines(String engines) {
        this.engines = engines;
    }

    public Integer getEngineHour() {
        return engineHour;
    }

    public void setEngineHour(Integer engineHour) {
        this.engineHour = engineHour;
    }

    public boolean getDuelFuel() {
        return duelFuel;
    }

    public boolean getEku() {
        return eku;
    }

    public boolean getAoi() {
        return aoi;
    }

    public Float getSize() {
        return size;
    }

    public void setSize(Float size) {
        this.size = size;
    }

    public String getPlungerSize() {
        return plungerSize;
    }

    public void setPlungerSize(String plungerSize) {
        this.plungerSize = plungerSize;
    }

    public boolean getWireless() {
        return wireless;
    }

    public boolean getEngineRebuild() {
        return engineRebuild;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getYardStatus() {
        return yardStatus;
    }

    public void setYardStatus(String yardStatus) {
        this.yardStatus = yardStatus;
    }

    public String getHardDownStatus() {
        return hardDownStatus;
    }

    public void setHardDownStatus(String hardDownStatus) {
        this.hardDownStatus = hardDownStatus;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Long getTs() {
        return ts;
    }

    public void setTs(Long ts) {
        this.ts = ts;
    }

    public Long getCreated() {
        return created;
    }

    public void setCreated(Long created) {
        this.created = created;
    }

    public boolean isDuelFuel() {
        return duelFuel;
    }

    public void setDuelFuel(boolean duelFuel) {
        this.duelFuel = duelFuel;
    }

    public String getNewAddStatus() {
        return newAddStatus;
    }

    public void setNewAddStatus(String newAddStatus) {
        this.newAddStatus = newAddStatus;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTransmission() {
        return transmission;
    }

    public void setTransmission(String transmission) {
        this.transmission = transmission;
    }

    public String getStrokeLength() {
        return strokeLength;
    }

    public void setStrokeLength(String strokeLength) {
        this.strokeLength = strokeLength;
    }

    public String getNoOfPlungers() {
        return noOfPlungers;
    }

    public void setNoOfPlungers(String noOfPlungers) {
        this.noOfPlungers = noOfPlungers;
    }

    public String getTrailerAxles() {
        return trailerAxles;
    }

    public void setTrailerAxles(String trailerAxles) {
        this.trailerAxles = trailerAxles;
    }

    public String getPumpIronBrandColor() {
        return pumpIronBrandColor;
    }

    public void setPumpIronBrandColor(String pumpIronBrandColor) {
        this.pumpIronBrandColor = pumpIronBrandColor;
    }

    public boolean getPumpStopped() {
        return pumpStopped;
    }

    public String getFluidEndBrand() {
        return fluidEndBrand;
    }

    public void setFluidEndBrand(String fluidEndBrand) {
        this.fluidEndBrand = fluidEndBrand;
    }

    public Float getCleanHour() {
        return cleanHour;
    }

    public void setCleanHour(Float cleanHour) {
        this.cleanHour = cleanHour;
    }

    public Float getDirtyHour() {
        return dirtyHour;
    }

    public void setDirtyHour(Float dirtyHour) {
        this.dirtyHour = dirtyHour;
    }

    public Float getStandByHour() {
        return standByHour;
    }

    public void setStandByHour(Float standByHour) {
        this.standByHour = standByHour;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public boolean isEku() {
        return eku;
    }

    public void setEku(boolean eku) {
        this.eku = eku;
    }

    public boolean isAoi() {
        return aoi;
    }

    public void setAoi(boolean aoi) {
        this.aoi = aoi;
    }

    public boolean isWireless() {
        return wireless;
    }

    public void setWireless(boolean wireless) {
        this.wireless = wireless;
    }

    public boolean isEngineRebuild() {
        return engineRebuild;
    }

    public void setEngineRebuild(boolean engineRebuild) {
        this.engineRebuild = engineRebuild;
    }

    public String getHpp() {
        return hpp;
    }

    public void setHpp(String hpp) {
        this.hpp = hpp;
    }

    public boolean isPumpStopped() {
        return pumpStopped;
    }

    public void setPumpStopped(boolean pumpStopped) {
        this.pumpStopped = pumpStopped;
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

    public String getHorsepower() {return horsepower;}

    public void setHorsepower(String horsepower) {this.horsepower = horsepower;}

    public String getRental() {return rental;}

    public void setRental(String rental) {this.rental = rental;}

    public String getBlenderRate() {
        return blenderRate;
    }

    public void setBlenderRate(String blenderRate) {
        this.blenderRate = blenderRate;
    }

    public String getControls() {return controls;}

    public void setControls(String controls) {this.controls = controls;}

    public String getHhp() {
        return hhp;
    }

    public void setHhp(String hhp) {
        this.hhp = hhp;
    }

    public boolean isStandbyPump() {
        return standbyPump;
    }

    public void setStandbyPump(boolean standbyPump) {
        this.standbyPump = standbyPump;
    }
}

// ===== Imported from: com.carbo.fleet.services.FleetService =====
package com.carbo.fleet.services;

import com.carbo.fleet.events.model.FleetDetails;
import com.carbo.fleet.model.Error;
import com.carbo.fleet.model.Job;
import com.carbo.fleet.model.OnSiteEquipment;
import com.carbo.fleet.model.PumpTypeEnum;
import com.carbo.fleet.repository.FleetMongoDbRepository;
import com.carbo.fleet.model.Fleet;
import com.carbo.fleet.repository.JobMongoDbRepository;
import com.carbo.fleet.repository.OnSiteEquipmentMongoDbRepository;
import com.carbo.fleet.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

import static com.carbo.fleet.utils.ControllerUtil.getOrganizationId;

@Service
public class FleetService {
    private final FleetMongoDbRepository fleetRepository;

    private final OnSiteEquipmentMongoDbRepository onSiteEquipmentMongoDbRepository;

    private final JobMongoDbRepository jobMongoDbRepository;

    @Autowired
    public FleetService(FleetMongoDbRepository fleetRepository, OnSiteEquipmentMongoDbRepository onSiteEquipmentMongoDbRepository,
                        JobMongoDbRepository jobMongoDbRepository) {
        this.fleetRepository = fleetRepository;
        this.onSiteEquipmentMongoDbRepository = onSiteEquipmentMongoDbRepository;
        this.jobMongoDbRepository = jobMongoDbRepository;
    }

    public List<Fleet> getAll() {
        return fleetRepository.findAll();
    }

    public List<Fleet> getByOrganizationId(String organizationId) {
        return fleetRepository.findByOrganizationId(organizationId);
    }

    public Optional<Fleet> getFleet(String fleetId) {
        return fleetRepository.findById(fleetId);
    }

    public Fleet saveFleet(Fleet fleet) {
        return fleetRepository.save(fleet);
    }

    public void updateFleet(Fleet fleet) {
        fleetRepository.save(fleet);
    }

    public void deleteFleet(String fleetId) {
        fleetRepository.deleteById(fleetId);
    }

    public Optional<Fleet> findDistinctByOrganizationIdAndName(String organizationId, String name) {
        return fleetRepository.findDistinctByOrganizationIdAndName(organizationId, name);
    }


//    String organizationId = getOrganizationId(request);
//    // Create an aggregation pipeline to retrieve the proposal and related information.
//    Aggregation aggregation =
//            Aggregation.match(Criteria.where("sharedWithOrganizationId").is(organizationId)),
//            Aggregation.project().and(ConvertOperators.ToString.toString("$_id")).as(PROPOSAL_ID_STRING).and("version").as("version")
//                       .and("role").as("role").and("awaiting").as("awaiting").and("proposalAction").as("proposalAction").and("proposalStatus")
//                       .as("proposalStatus").and("showProposalName").as("showProposalName").and("opportunity").as("opportunity")
//                       .and("companyId").as("companyId").and("priceBookId").as("priceBookId").and("priceBy").as("priceBy")
//                       .and("wellEquipments").as("wellEquipments").and(ORGANIZATION_ID).as(ORGANIZATION_ID).and("auditDetails")
//                       .as("auditDetails"),
//            LookupOperation.newLookup().from("proposal-basic-information-v2").localField(PROPOSAL_ID_STRING).foreignField(PROPOSAL_ID)
//                           .as("basicInformation"),
//            LookupOperation.newLookup().from("proposal-design-v2").localField(PROPOSAL_ID_STRING).foreignField(PROPOSAL_ID).as("design"),
//            LookupOperation.newLookup().from("proposal-fluids-v2").localField(PROPOSAL_ID_STRING).foreignField(PROPOSAL_ID).as("fluids"),
//            LookupOperation.newLookup().from("proposal-proppants-v2").localField(PROPOSAL_ID_STRING).foreignField(PROPOSAL_ID)
//                           .as("proppants"),
//            LookupOperation.newLookup().from("proposal-well-v2").localField(PROPOSAL_ID_STRING).foreignField(PROPOSAL_ID).as("wells"));
//    List<Proposal> proposal = null;
//    // Check if the provided proposal ID is not empty.
//            if (!StringUtils.isEmpty(id)) {
//        // Execute the aggregation and retrieve the results.
//        proposal = mongoTemplate.aggregate(aggregation, "proposal-v2", Proposal.class).getMappedResults();
//    }
//

    public ResponseEntity getFleetData(HttpServletRequest request) {
        try {
            String organizationId = getOrganizationId(request);
            Map<String,Map<String, Map<PumpTypeEnum, Integer>>> fleetDataforAllOrganizations = new HashMap<>();
            List<Job> jobList = jobMongoDbRepository.findBySharedWithOrganizationIdAndStatus(organizationId, "In Progress");

            Set<String> filterFleetName = jobList.stream()
                    .map(Job::getFleet)
                    .collect(Collectors.toSet());

            Set<String> organizationIds = jobList.stream().map(Job::getOrganizationId).collect(Collectors.toSet());

            Map<String, FleetDetails> fleets = new HashMap<>();
            for(Job job : jobList){
                FleetDetails fleetDataFromJobs = new FleetDetails();
                fleetDataFromJobs.setName(job.getFleet());
                fleetDataFromJobs.setOrganizationId(job.getOrganizationId());
                fleets.put(job.getId(),fleetDataFromJobs);
            }

            List<Fleet> fleetLists = fleetRepository.findByOrganizationIdInAndNameIn(organizationIds, filterFleetName);

            List<Fleet> filteredFleetLists = new ArrayList<>();

            for (Fleet fleet : fleetLists) {
                // Check all combinations of fleet name and organization ID
                for (FleetDetails allFleetData : fleets.values()) {
                    if (allFleetData.getName().equals(fleet.getName())
                            && allFleetData.getOrganizationId().contains(fleet.getOrganizationId())) {
                        filteredFleetLists.add(fleet);
                        break;  // Break once a matching combination is found for the current fleet
                    }
                }
            }

            Set<String> fleetIds = filteredFleetLists.stream().map(Fleet::getId).collect(Collectors.toSet());

            List<OnSiteEquipment> data = onSiteEquipmentMongoDbRepository.findByFleetIdIn(fleetIds);
            for(String ogrId: organizationIds) {
                // Iterate through each fleet

                Map<String, Map<PumpTypeEnum, Integer>> fleetData = new HashMap<>();

                for (Fleet fleet : fleetLists) {
                    if(fleet.getOrganizationId().equals(ogrId)) {
                        String fleetId = !ObjectUtils.isEmpty(fleet.getId()) ? fleet.getId() : "";
                        String fleetName = fleet.getName();

                        // Initialize counts for the three categories
                        int dualPumpCount = 0;
                        int ePumpCount = 0;
                        int dieselPump = 0;

                        // Iterate through the onSiteEquipment entries
                        for (OnSiteEquipment onSiteEquipment : data) {
                            if (onSiteEquipment.getFleetId().equalsIgnoreCase(fleetId)) {
                                if (onSiteEquipment.getType().equalsIgnoreCase("pumps") && onSiteEquipment.getDuelFuel()) {
                                    dualPumpCount++;
                                }
                                if (onSiteEquipment.getType().equalsIgnoreCase("pumps") && !onSiteEquipment.getDuelFuel()) {
                                    dieselPump++;
                                }
                                if (onSiteEquipment.getType().equalsIgnoreCase("epumps")) {
                                    ePumpCount++;
                                }
                            }
                        }

                        if (fleetData.containsKey(fleetName)) {
                            Map<PumpTypeEnum, Integer> existingFleetCounts = fleetData.get(fleetName);
                            existingFleetCounts.put(PumpTypeEnum.DUAL, existingFleetCounts.get(PumpTypeEnum.DUAL) + dualPumpCount);
                            existingFleetCounts.put(PumpTypeEnum.ELECTRIC, existingFleetCounts.get(PumpTypeEnum.ELECTRIC) + ePumpCount);
                            existingFleetCounts.put(PumpTypeEnum.DIESEL, existingFleetCounts.get(PumpTypeEnum.DIESEL) + dieselPump);
                        } else {
                            Map<PumpTypeEnum, Integer> fleetCounts = new HashMap<>();
                            fleetCounts.put(PumpTypeEnum.DUAL, dualPumpCount);
                            fleetCounts.put(PumpTypeEnum.ELECTRIC, ePumpCount);
                            fleetCounts.put(PumpTypeEnum.DIESEL, dieselPump);

                            // Add the fleet data to the main fleetData map with fleetName as the key
                            fleetData.put(fleetName, fleetCounts);
                        }
                    }
                }
                fleetDataforAllOrganizations.put(ogrId,fleetData);
            }
            return ResponseEntity.ok(fleetDataforAllOrganizations);
        } catch (Exception e) {
            Error error = Error.builder()
                    .errorCode(Constants.UNABLE_TO_FETCH_DATA_CODE)
                    .errorMessage(Constants.UNABLE_TO_FETCH_DATA_MESSAGE).build();
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}



// ===== Imported from: com.carbo.fleet.utils.ControllerUtil.getOrganizationId =====
package com.carbo.fleet.utils;

import org.springframework.security.oauth2.provider.OAuth2Authentication;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Map;

public class ControllerUtil {
    public static String getOrganizationId(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        return ((Map) ((OAuth2Authentication) principal).getUserAuthentication().getDetails()).get("organizationId").toString();
    }
    public static String getOrganizationType(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        return ((Map) ((OAuth2Authentication) principal).getUserAuthentication().getDetails()).get("organizationType").toString();
    }
    public static String getUserName(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        return ((Map) ((OAuth2Authentication) principal).getUserAuthentication().getDetails()).get("userName").toString();
    }
}

// ===== Imported from: com.carbo.fleet.utils.ControllerUtil.getOrganizationType =====
package com.carbo.fleet.utils;

import org.springframework.security.oauth2.provider.OAuth2Authentication;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Map;

public class ControllerUtil {
    public static String getOrganizationId(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        return ((Map) ((OAuth2Authentication) principal).getUserAuthentication().getDetails()).get("organizationId").toString();
    }
    public static String getOrganizationType(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        return ((Map) ((OAuth2Authentication) principal).getUserAuthentication().getDetails()).get("organizationType").toString();
    }
    public static String getUserName(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        return ((Map) ((OAuth2Authentication) principal).getUserAuthentication().getDetails()).get("userName").toString();
    }
}

// ===== Current file: src\main\java\com\carbo\fleet\controllers\FleetServiceController.java =====
package com.carbo.fleet.controllers;

import com.carbo.fleet.model.Job;
import com.carbo.fleet.model.OnSiteEquipment;
import com.carbo.fleet.services.FleetService;
import com.carbo.fleet.model.Fleet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

import static com.carbo.fleet.utils.ControllerUtil.getOrganizationId;
import static com.carbo.fleet.utils.ControllerUtil.getOrganizationType;

@RestController
@RequestMapping(value = "v1/fleets")
public class FleetServiceController {
    private static final Logger logger = LoggerFactory.getLogger(FleetServiceController.class);

    private final FleetService fleetService;
    private final MongoTemplate mongoTemplate;

    @Autowired
    public FleetServiceController(FleetService fleetService, MongoTemplate mongoTemplate) {
        this.fleetService = fleetService;
        this.mongoTemplate = mongoTemplate;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public List<Fleet> getFleets(HttpServletRequest request) {
        String organizationId = getOrganizationId(request);
        String organizationType = getOrganizationType(request);
        List<Fleet> all = new ArrayList<>();
        if(organizationType.contentEquals("OPERATOR")){
            Query query = new Query();
            query.addCriteria(Criteria.where("sharedWithOrganizationId").is(organizationId));
            List<Job> jobs = mongoTemplate.find(query, Job.class);
            Set<String> fleets = jobs.stream().map(Job::getFleet).collect(Collectors.toSet());
            Set<String> organizationIds = jobs.stream().map(Job::getOrganizationId).collect(Collectors.toSet());
            
            Query query1 = new Query();
            query1.addCriteria(Criteria.where("name").in(fleets));
            query1.addCriteria(Criteria.where("organizationId").in(organizationIds));
            all = mongoTemplate.find(query1, Fleet.class);
        }else {
            all = fleetService.getByOrganizationId(organizationId);
        }
        return all;
    }

    @RequestMapping(value = "/{fleetId}", method = RequestMethod.GET)
    public Fleet getFleet(@PathVariable("fleetId") String fleetId) {
        logger.debug("Looking up data for fleet {}", fleetId);

        Fleet fleet = fleetService.getFleet(fleetId).get();
        return fleet;
    }

    @RequestMapping(value = "/{fleetId}", method = RequestMethod.PUT)
    public void updateFleet(@PathVariable("fleetId") String fleetId, @RequestBody Fleet fleet) {
        fleetService.updateFleet(fleet);
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public void saveFleet(@RequestBody Fleet fleet) {
        fleetService.saveFleet(fleet);
    }

    @RequestMapping(value = "/{fleetId}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFleet(@PathVariable("fleetId") String fleetId) {
        fleetService.deleteFleet(fleetId);
    }

    @RequestMapping(value = "/findDistinctByOrganizationIdAndName", method = RequestMethod.GET)
    public Optional<Fleet> findDistinctByOrganizationIdAndName(HttpServletRequest request, String name) {
        String organizationId = getOrganizationId(request);
        return fleetService.findDistinctByOrganizationIdAndName(organizationId, name);
    }

    @GetMapping("/data")
    public ResponseEntity getFleetData(HttpServletRequest request) {
        return fleetService.getFleetData(request);
    }

    @RequestMapping(value = "/calendarFleets", method = RequestMethod.GET)
    public List<Fleet> getFleetsForCalendar(HttpServletRequest request) {
        String organizationId = getOrganizationId(request);
        String organizationType = getOrganizationType(request);
        List<Fleet> all = new ArrayList<>();
        if(organizationType.contentEquals("OPERATOR")){
            Query query = new Query();
            query.addCriteria(Criteria.where("sharedWithOrganizationId").is(organizationId));
            List<Job> jobList = mongoTemplate.find(query, Job.class);
            List<Job> jobs = new ArrayList<>();
            if(!ObjectUtils.isEmpty(jobList)){
                for (Job job : jobList){
                    if(!ObjectUtils.isEmpty(job.getProposalId())){
                        jobs.add(job);
                    }
                }
            }
            Set<String> fleets = jobs.stream().map(Job::getFleet).collect(Collectors.toSet());
            Set<String> organizationIds = jobs.stream().map(Job::getOrganizationId).collect(Collectors.toSet());

            Query query1 = new Query();
            query1.addCriteria(Criteria.where("name").in(fleets));
            query1.addCriteria(Criteria.where("organizationId").in(organizationIds));
            all = mongoTemplate.find(query1, Fleet.class);
        }else {
            all = fleetService.getByOrganizationId(organizationId);
        }
        return all;
    }
}









