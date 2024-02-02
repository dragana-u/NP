package Ispitni.vtorKolokviumIspit.i30;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


class DurationConverter {
    public static String convert(long duration) {
        long minutes = duration / 60;
        duration %= 60;
        return String.format("%02d:%02d", minutes, duration);
    }
}

interface ICallState{
    void answer(long timestamp);
    void hold(long timestamp);
    void resume(long timestamp);
    void end(long timestamp);
}

abstract class CallState implements ICallState{
    Call call;

    public CallState(Call call) {
        this.call = call;
    }
}

class InProgressCalState extends CallState{

    public InProgressCalState(Call call) {
        super(call);
    }

    @Override
    public void answer(long timestamp) {
        throw new RuntimeException();
    }

    @Override
    public void hold(long timestamp) {
        call.holdStarted = timestamp;
        call.state = new PausedCallState(call);
    }

    @Override
    public void resume(long timestamp) {
        throw new RuntimeException();
    }

    @Override
    public void end(long timestamp) {
        call.callEnded=timestamp;
        call.state = new TerminatedCallState(call);
    }
}

class CallStartedState extends CallState{
    public CallStartedState(Call call) {
        super(call);
    }

    @Override
    public void answer(long timestamp) {
        call.callStarted = timestamp;
        call.state = new InProgressCalState(call);
    }

    @Override
    public void hold(long timestamp) {
        throw new RuntimeException();
    }

    @Override
    public void resume(long timestamp) {
        throw new RuntimeException();
    }

    @Override
    public void end(long timestamp) {
        call.callStarted = timestamp;
        call.callEnded = timestamp;
        call.state = new TerminatedCallState(call);
    }
}

class PausedCallState extends CallState{

    public PausedCallState(Call call) {
        super(call);
    }

    @Override
    public void answer(long timestamp) {
        throw new RuntimeException();
    }

    @Override
    public void hold(long timestamp) {
        throw new RuntimeException();
    }

    @Override
    public void resume(long timestamp) {
        call.totalTimeInHold += (timestamp-call.holdStarted);
        call.state = new InProgressCalState(call);
    }

    @Override
    public void end(long timestamp) {
        call.totalTimeInHold += (timestamp-call.holdStarted);
        call.callEnded = timestamp;
        call.state = new TerminatedCallState(call);
    }
}

class TerminatedCallState extends CallState{

    public TerminatedCallState(Call call) {
        super(call);
    }

    @Override
    public void answer(long timestamp) {
        throw new RuntimeException();
    }

    @Override
    public void hold(long timestamp) {
        throw new RuntimeException();
    }

    @Override
    public void resume(long timestamp) {
        throw new RuntimeException();
    }

    @Override
    public void end(long timestamp) {
        throw new RuntimeException();
    }
}

class Call{
    String uuid;
    String dialer;
    String receiver;
    long timestampCalled;

    CallState state = new CallStartedState(this);

    long callStarted;

    long callEnded;

    long totalTimeInHold = 0;

    long holdStarted = -1;

    public Call(String uuid, String dialer, String receiver, long timestampCalled) {
        this.uuid = uuid;
        this.dialer = dialer;
        this.receiver = receiver;
        this.timestampCalled = timestampCalled;
    }

    public void answer (long timestamp) {
       state.answer(timestamp);
    }

    public void hold (long timestamp){
        state.hold(timestamp);
    }

    public void resume (long timestamp){
       state.resume(timestamp);
    }

    public void end(long timestamp){
        state.end(timestamp);
    }

    public long totalTime () {
        return callEnded-callStarted-totalTimeInHold;
    }

    public long getCallStarted() {
        return callStarted;
    }
}

class TelcoApp{
    Map<String,List<Call>> callsByNumber = new HashMap<>();
    Map<String,Call> callsByUuid = new HashMap<>();
    void addCall (String uuid, String dialer, String receiver, long timestamp){
        Call c = new Call(uuid,dialer,receiver,timestamp);
        List<Call> callsByDialer = callsByNumber.get(dialer);
        List<Call> callsByReceiver = callsByNumber.get(receiver);
        if(callsByDialer == null){
            callsByDialer = new ArrayList<>();
        }
        if(callsByReceiver == null){
            callsByReceiver = new ArrayList<>();
        }
        callsByDialer.add(c);
        callsByReceiver.add(c);
        callsByNumber.putIfAbsent(dialer,callsByDialer);
        callsByNumber.putIfAbsent(receiver,callsByReceiver);
        callsByUuid.put(uuid,c);
    }
    void updateCall (String uuid, long timestamp, String action){
        Call call = callsByUuid.get(uuid);
        if(action.equals("ANSWER")){
            call.answer(timestamp);
        }else if(action.equals("END")){
            call.end(timestamp);
        }else if(action.equals("HOLD")){
            call.hold(timestamp);
        }else{//RESUME
            call.resume(timestamp);
        }
    }
    void printChronologicalReport(String phoneNumber){
        List<Call> calls = callsByNumber.get(phoneNumber);
        calls.forEach(call -> {
            printingReport(phoneNumber, call);
        });
    }

    private static void printingReport(String phoneNumber, Call call) {
        String callTime = DurationConverter.convert(call.totalTime());
        String s = "";
        if(callTime.equals("00:00")){
            s = s.concat(call.callStarted + " MISSED CALL " + callTime);
        }else{
            s = s.concat(call.callStarted + " " + call.callEnded + " " + callTime);
        }
        if (call.dialer.equals(phoneNumber)) {
            System.out.printf("D %s %s\n", call.receiver,s);
        } else {
            System.out.printf("R %s %s\n", call.dialer,s);
        }
    }

    void printReportByDuration(String phoneNumber){
        List<Call> calls = callsByNumber.get(phoneNumber).stream().sorted(Comparator.comparing(Call::totalTime).thenComparing(Call::getCallStarted).reversed()).collect(Collectors.toList());
        calls.forEach(c -> printingReport(phoneNumber,c));
    }

//    void printCallsDuration(){
//
//        callsByUuid
//                .values()
//                .stream().sorted(Comparator.comparing(Call::totalTime).reversed())
//                .forEach(j ->{
//                    System.out.printf("%s <-> %s : %s\n",
//                            j.dialer,
//                            j.receiver,
//                            DurationConverter.convert(j.totalTime()));
//                });
//    }
public void printCallsDuration() {
    TreeMap<String, Long> result = callsByUuid.values().stream().collect(Collectors.groupingBy(
            c -> String.format("%s <-> %s", c.dialer, c.receiver),
            TreeMap::new,
            Collectors.summingLong(Call::totalTime)
    ));

    result.entrySet().stream()
            .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
            .forEach(entry -> System.out.printf("%s : %s%n", entry.getKey(), DurationConverter.convert(entry.getValue())));
}

}

public class TelcoTest2 {
    public static void main(String[] args) {
        TelcoApp app = new TelcoApp();

        Scanner sc = new Scanner(System.in);

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] parts = line.split("\\s+");
            String command = parts[0];

            if (command.equals("addCall")) {
                String uuid = parts[1];
                String dialer = parts[2];
                String receiver = parts[3];
                long timestamp = Long.parseLong(parts[4]);
                app.addCall(uuid, dialer, receiver, timestamp);
            } else if (command.equals("updateCall")) {
                String uuid = parts[1];
                long timestamp = Long.parseLong(parts[2]);
                String action = parts[3];
                app.updateCall(uuid, timestamp, action);
            } else if (command.equals("printChronologicalReport")) {
                String phoneNumber = parts[1];
                app.printChronologicalReport(phoneNumber);
            } else if (command.equals("printReportByDuration")) {
                String phoneNumber = parts[1];
                app.printReportByDuration(phoneNumber);
            } else {
                app.printCallsDuration();
            }
        }

    }
}
