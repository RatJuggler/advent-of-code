package day10;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;


abstract class Output {

    private final int number;

    Output(final int number) {
        this.number = number;
    }

    abstract void acceptChip(final int chip);

}

class Bin extends Output {

    private int contents;

    Bin(final int number) {
        super(number);
    }

    int getContents() {
        return this.contents;
    }

    @Override
    void acceptChip(int chip) {
        this.contents = chip;
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
        } else {
            throw new IllegalStateException("Bot only expected to accept two chips!");
        }
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

}

class Factory {

    private final Map<Integer, Bin> bins = new HashMap<>();
    private final Map<Integer, Bot> bots = new HashMap<>();

    Factory() {}

    private Matcher parseInstruction(final String instruction, final String pattern) {
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(instruction);
        if (!m.find()) {
            throw new IllegalStateException("Unable to parse instruction: " + instruction);
        }
        return m;
    }

    private void processValue(final String instruction) {
        String pattern = "^value (?<value>\\d+) goes to bot (?<bot>\\d+)$";
        Matcher m = this.parseInstruction(instruction, pattern);
        String value = m.group("value");
        String bot = m.group("bot");
    }

    private void processBot(final String instruction) {
        String pattern = "^bot (?<bot>\\d+) gives low to (?<output1>bot|output) (?<number1>\\d+) and high to (?<output2>bot|output) (?<number2>\\d+)$";
        Matcher m = this.parseInstruction(instruction, pattern);
        String bot = m.group("bot");
        String output1 = m.group("output1");
        String number1 = m.group("number1");
        String output2 = m.group("output2");
        String number2 = m.group("number2");
    }

    void applyInstruction(final String instruction) {
        if (instruction.startsWith("value")) {
            processValue(instruction);
        } else if (instruction.startsWith("bot")) {
            processBot(instruction);
        } else {
            throw new IllegalStateException(String.format("Unrecognised instruction '%s'!", instruction));
        }
    }

    Optional<Bin> getBin(final int number) {
        return Optional.ofNullable(this.bins.get(number));
    }

    Optional<Bot> getBot(final int number) {
        return Optional.ofNullable(this.bots.get(number));
    }

}

public class Advent10 {

    private static Factory processInstructions(final String filename) throws IOException {
        Factory factory = new Factory();
        try (Stream<String> stream = Files.lines(Paths.get(filename))) {
            stream.forEach(factory::applyInstruction);
        }
        return factory;
    }

    private static void testFollowInstructions() throws IOException {
        Factory factory = processInstructions("2016/day10/test10a.txt");
        assert factory.getBin(0).orElseThrow().getContents() == 5 : "Expected Bin 0 to contain 5!";
        assert factory.getBin(1).orElseThrow().getContents() == 2 : "Expected Bin 1 to contain 2!";
        assert factory.getBin(2).orElseThrow().getContents() == 3 : "Expected Bin 2 to contain 3!";
        assert factory.getBot(2).orElseThrow().getChips().equals("2:5") : "Expected Bot 2 to compare 2:5!";
    }

    public static void main(final String[] args) throws IOException {
        testFollowInstructions();
        processInstructions("2016/day10/input10.txt");
    }

}
