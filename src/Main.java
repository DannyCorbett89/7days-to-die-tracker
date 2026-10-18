import org.apache.commons.io.IOUtils;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.classic.methods.HttpPatch;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.io.entity.StringEntity;
import tools.jackson.databind.ObjectMapper;

private Config config;

void main(String[] args) throws IOException {
    loadConfig();

    List<Player> players = new ArrayList<>();
    players.add(loadPlayer("Ussa", "EOS_00023ca7836b4654976976aaece6cc97.ttp"));
    players.add(loadPlayer("Rumi", "EOS_0002658277124e58b8c27e0fccc3688c.ttp"));
    players.add(loadPlayer("Vivi", "EOS_00025db819124cae84ef79cedfc05c94.ttp"));

    SkillName[] skillNames = SkillName.values();
    Arrays.sort(skillNames, Comparator.comparing(SkillName::displayName));
    int numSkillNames = skillNames.length;
    int half = numSkillNames / 2;
    SkillName[] part1 = Arrays.copyOfRange(skillNames, 0, half);
    SkillName[] part2 = Arrays.copyOfRange(skillNames, half, skillNames.length);

    List<String> messageIds = config.getMessageIds();
    sendMessage(messageIds.get(0), getMessageText(players, part1));
    sendMessage(messageIds.get(1), getMessageText(players, part2));
}

private void loadConfig() throws IOException {
    File configFile = new File("config.json");

    if(!configFile.exists()) {
        if(!configFile.createNewFile()) {
            throw new RuntimeException("Unable to create config file: " + configFile.getAbsolutePath());
        }
    }

    ObjectMapper mapper = new ObjectMapper();
    config = mapper.readValue(configFile, Config.class);
}

private void sendMessage(String messageId, String message) throws IOException {
    String webhookUrl = config.getWebhookUrl() + "/messages/" + messageId;
    HttpClient client = HttpClients.createDefault();
    HttpPatch post = new HttpPatch(webhookUrl);
    post.addHeader("Content-type", "application/json");
    String json = "{\"content\": \"" + message + "\"}";
    post.setEntity(new StringEntity(json));
    System.out.println(json);
    HttpResponse execute = client.execute(post);
    String string = IOUtils.toString(((CloseableHttpResponse) execute).getEntity().getContent(), StandardCharsets.UTF_8);

    System.out.println(string);
}

private String getMessageText(List<Player> players, SkillName[] skillNames) {
    int longestSkillName = longestDisplayName(skillNames);

    StringBuilder message = new StringBuilder("```");
    message.append("\n+").append(pad(longestSkillName + 2, '-')).append("+");

    for (Player player : players) {
        message.append(pad(player.getName().length() + 2, '-')).append("+");
    }

    message.append("\n|").append(pad(longestSkillName + 2, ' ')).append("|");

    for (Player player : players) {
        message.append(" ").append(player.getName()).append(" |");
    }

    for (SkillName skillName : skillNames) {
        message.append("\n+").append(pad(longestSkillName + 2, '-')).append("+");

        for (Player player : players) {
            message.append(pad(player.getName().length() + 2, '-')).append("+");
        }

        message.append("\n| ").append(skillName.displayName()).append(pad((longestSkillName + 1) - skillName.displayName().length(), ' ')).append("|");

        for (Player player : players) {
            String skill = String.valueOf(player.getSkill(skillName));
            message.append(" ").append(skill).append(pad((player.getName().length() + 1) - skill.length(), ' ')).append("|");
        }
    }

    message.append("\n+").append(pad(longestSkillName + 2, '-')).append("+");

    for (Player player : players) {
        message.append(pad(player.getName().length() + 2, '-')).append("+");
    }

    message.append("\n```");

    return message.toString().replace("\n", "\\n");
}

public String pad(int padding, char character) {
    return String.format("%" + padding + "s", " ").replace(' ', character);
}

public int longestDisplayName(SkillName[] skillNames) {
    List<Integer> lengths = Arrays.stream(skillNames)
            .map(SkillName::displayName)
            .map(String::length)
            .toList();
    return Collections.max(lengths);
}

private String getRowText(int stat) {
    String text = " " + stat;

    if(stat <= 9) {
        text += "    |";
    } else if(stat <= 99) {
        text += "   |";
    } else {
        text += "  |";
    }

    return text;
}

private Player loadPlayer(String name, String file) throws IOException {
    String saveFile = "/home/danny/.local/share/7DaysToDie/Saves/West Xuyofu Territory/Ussa 3/Player/" + file;
    Path path = Path.of(saveFile);
    byte[] data = Files.readAllBytes(path);

    Player player = new Player(name);

    for (SkillName skillName : SkillName.values()) {
        player.addSkill(skillName, findSkillValue(skillName, data));
    }

    return player;
}

private int findSkillValue(SkillName name, byte[] data) {
    int index = findIndexForString(name, data);
    return data[index];
}

private int findIndexForString(SkillName name, byte[] data) {
    String internal = name.internal();
    byte[] nameBytes = internal.getBytes();
    List<Byte> dataList = toList(data);
    List<Byte> nameBytesList = toList(nameBytes);
    int index = Collections.indexOfSubList(dataList, nameBytesList);
    return index + internal.length();
}

private List<Byte> toList(byte[] array) {
    List<Byte> list = new ArrayList<>();

    for(byte element : array) {
        list.add(element);
    }

    return list;
}