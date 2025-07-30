package ro.tuc.ds2020.dtos;

import lombok.Data;

import java.util.Objects;
import java.util.UUID;

@Data
public class DeviceDto {


    private UUID deviceId;
    private String description;
    private String address;
    private Double maximumHourlyEnergyConsumption;
    private UUID personId;

    public DeviceDto(){

    }

    public DeviceDto(UUID deviceId, String description, String address, Double maximumHourlyEnergyConsumption, UUID personId){

        this.deviceId = deviceId;
        this.description = description;
        this.address = address;
        this.maximumHourlyEnergyConsumption = maximumHourlyEnergyConsumption;
        this.personId = personId;
    }

    public DeviceDto(UUID deviceId, String description, String address, Double maximumHourlyEnergyConsumption){

        this.deviceId = deviceId;
        this.description = description;
        this.address = address;
        this.maximumHourlyEnergyConsumption = maximumHourlyEnergyConsumption;
    }

    public UUID getPersonId() {
        return personId;
    }

    public void setPersonId(UUID personId) {
        this.personId = personId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeviceDto deviceDto = (DeviceDto) o;
        return   Objects.equals(description, deviceDto.description) &&
                Objects.equals(address, deviceDto.address) &&
                maximumHourlyEnergyConsumption == deviceDto.maximumHourlyEnergyConsumption ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, address, maximumHourlyEnergyConsumption);
    }



}