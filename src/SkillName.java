import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum SkillName {
    CRAFTINGHARVESTINGTOOLS("Harvesting Tools"),
    CRAFTINGREPAIRTOOLS("Repair Tools"),
    CRAFTINGSALVAGETOOLS("Salvage Tools"),
    CRAFTINGKNUCKLES("Knuckles"),
    CRAFTINGBLADES("Blades"),
    CRAFTINGCLUBS("Clubs"),
    CRAFTINGSLEDGEHAMMERS("Sledgehammers"),
    CRAFTINGBOWS("Bows"),
    CRAFTINGSPEARS("Spears"),
    CRAFTINGHANDGUNS("Handguns"),
    CRAFTINGSHOTGUNS("Shotguns"),
    CRAFTINGRIFLES("Rifles"),
    CRAFTINGMACHINEGUNS("Machine Guns"),
    CRAFTINGEXPLOSIVES("Explosives"),
    CRAFTINGROBOTICS("Robotics"),
    CRAFTINGARMOR("Armour"),
    CRAFTINGBACKPACK("Backpack"),
    CRAFTINGMEDICAL("Medical"),
    CRAFTINGFOOD("Food"),
    CRAFTINGSEEDS("Seeds"),
    CRAFTINGELECTRICIAN("Electrician"),
    CRAFTINGTRAPS("Traps"),
    CRAFTINGWORKSTATIONS("Workstations"),
    CRAFTINGVEHICLES("Vehicles");

    private String displayName;

    SkillName(String displayName) {
        this.displayName = displayName;
    }

    public String internal() {
        return name().toLowerCase();
    }

    public String displayName() {
        return displayName;
    }
}
