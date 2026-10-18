import java.util.HashMap;
import java.util.Map;

public class Player {
    private final String name;
    private final Map<SkillName, Integer> skills = new HashMap<>();

    public Player(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getSkill(SkillName name) {
        return skills.get(name);
    }

    public void addSkill(SkillName name, Integer value) {
        skills.put(name, value);
    }
}
