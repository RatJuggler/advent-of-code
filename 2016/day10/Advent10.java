package day10;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;


abstract class Output {

    private final int number;

    Output(final int number) {
        this.number = number;
    }

    int getNumber() {
        return this.number;
    }

    abstract void acceptChip(final int chip);

    abstract String getName();
}

class Bin extends Output {

    private int contents = -1;

    Bin(final int number) {
        super(number);
    }

    int getContents() {
        return this.contents;
    }

    @Override
    void acceptChip(int chip) {
        if (this.contents != -1 && this.contents != chip) {
            throw new IllegalStateException("Bin only expected to accept one chip!");
        }
        this.contents = chip;
    }

    @Override
    String getName() {
        return "Bin-" + this.getNumber();
    }

    @Override
    public String toString() {
        return "Bin{contents=" + contents + '}';
    }
}

class Bot extends Output {

    private int chip1 = -1;
    private int chip2 = -1;
    private Output low = null;
    private Output high = null;

    Bot(final int number) {
        super(number);
    }

    String getChips() {
        if (this.chip1 > this.chip2) {
            return this.chip2 + ":" + this.chip1;
        } else {
            return this.chip1 + ":" + this.chip2;
        }
    }

    void setLow(final Output output) {
        this.low = output;
    }

    void setHigh(final Output output) {
        this.high = output;
    }

    @Override
    void acceptChip(final int chip) {
        if (this.chip1 == -1) {
            this.chip1 = chip;
        } else if (this.chip2 == -1) {
            this.chip2 = chip;
        } else if (chip != this.chip1 && chip != this.chip2){
            throw new IllegalStateException("Bot only expected to accept two chips!");
        }
    }

    @Override
    String getName() {
        return "Bot-" + this.getNumber();
    }

    void proceed() {
        if (this.chip1 == -1 || this.chip2 == -1 || low == null || high == null) {
            return;
        }
        if (this.chip1 > this.chip2) {
            this.high.acceptChip(this.chip1);
            this.low.acceptChip(this.chip2);
        } else {
            this.high.acceptChip(this.chip2);
            this.low.acceptChip(this.chip1);
        }
    }

    @Override
    public String toString() {
        return "Bot{chip1=" + chip1 + ", chip2=" + chip2 + ", low=" + low.getName() + ", high=" + high.getName() + '}';
    }
}

class Factory {

    private final Map<Integer, Bin> bins = new HashMap<>();
    private final Map<Integer, Bot> bots = new HashMap<>();

    Factory() {}

    Bin getBin(final int number) {
        return this.bins.computeIfAbsent(number, Bin::new);
    }

    Bot getBot(final int number) {
        return this.bots.computeIfAbsent(number, Bot::new);
    }

    private Matcher parseInstruction(final String instruction, final String pattern) {
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(instruction);
        if (!m.find()) {
            throw new IllegalStateException("Unable to parse instruction: " + instruction);
        }
        return m;
    }

    private Bot processValueInstruction(final String instruction) {
        String pattern = "^value (?<chip>\\d+) goes to bot (?<bot>\\d+)$";
        Matcher m = this.parseInstruction(instruction, pattern);
        int chip = Integer.parseInt(m.group("chip"));
        int botNumber = Integer.parseInt(m.group("bot"));
        Bot bot = this.getBot(botNumber);
        bot.acceptChip(chip);
        return bot;
    }

    private Output assign(final String output, final int number) {
        if (output.equals("bot")) {
            return this.getBot(number);
        } else if (output.equals("output")) {
            return this.getBin(number);
        } else {
            throw new IllegalStateException(String.format("Unknown output assignment '%s'!", output));
        }
    }

    private Bot processBotInstruction(final String instruction) {
        String pattern = "^bot (?<bot>\\d+) gives low to (?<output1>bot|output) (?<number1>\\d+) and high to (?<output2>bot|output) (?<number2>\\d+)$";
        Matcher m = this.parseInstruction(instruction, pattern);
        int botNumber = Integer.parseInt(m.group("bot"));
        String output1 = m.group("output1");
        int number1 = Integer.parseInt(m.group("number1"));
        String output2 = m.group("output2");
        int number2 = Integer.parseInt(m.group("number2"));
        Bot bot = this.getBot(botNumber);
        bot.setLow(assign(output1, number1));
        bot.setHigh(assign(output2, number2));
        return bot;
    }

    void applyInstruction(final String instruction) {
        Bot bot;
        if (instruction.startsWith("value")) {
            bot = processValueInstruction(instruction);
        } else if (instruction.startsWith("bot")) {
            bot = processBotInstruction(instruction);
        } else {
            throw new IllegalStateException(String.format("Unrecognised instruction '%s'!", instruction));
        }
    }

    void proceed() {
        for (int i = 0; i < this.bots.size(); i++) {
            this.bots.values().forEach(Bot::proceed);
        }
    }

    @Override
    public String toString() {
        return "Factory{bins=" + bins + ", bots=" + bots + '}';
    }
}

public class Advent10 {

    private static Factory processInstructions(final String filename) throws IOException {
        Factory factory = new Factory();
        try (Stream<String> stream = Files.lines(Paths.get(filename))) {
            stream.forEach(factory::applyInstruction);
        }
        factory.proceed();
        System.out.println(factory.toString());
        return factory;
    }

    private static void testFollowInstructions() throws IOException {
        Factory factory = processInstructions("2016/day10/test10a.txt");
        assert factory.getBin(0).getContents() == 5 : "Expected Bin 0 to contain 5!";
        assert factory.getBin(1).getContents() == 2 : "Expected Bin 1 to contain 2!";
        assert factory.getBin(2).getContents() == 3 : "Expected Bin 2 to contain 3!";
        assert factory.getBot(2).getChips().equals("2:5") : "Expected Bot 2 to compare 2:5!";
    }

    public static void main(final String[] args) throws IOException {
        testFollowInstructions();
        processInstructions("2016/day10/input10.txt");
    }

}
