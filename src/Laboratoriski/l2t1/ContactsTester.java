package Laboratoriski.l2t1;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Scanner;
abstract class Contact implements Comparable<Contact>{
    private String date;
    private final int year;
    private final int month;
    private final int day;

    public Contact(String date) {
        this.date = date;
        this.year=Integer.parseInt(date.substring(0,4));
        this.month=Integer.parseInt(date.substring(5,7));
        this.day=Integer.parseInt(date.substring(8,10));
    }

    public boolean isNewerThan(Contact c){
        return date.compareTo(c.date)>0;
    }
    @Override
    public int compareTo(Contact o) {
        return o.date.compareTo(date);
    }
    public abstract String getType();
}

class EmailContact extends Contact{

    private String email;
    public EmailContact(String date,String email) {
        super(date);
        this.email=email;
    }

    public String getEmail(){
        return email;
    }
    @Override
    public String getType() {
        return "Email";
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\"")
                .append(email)
                .append("\"");
        return sb.toString();
    }

}

enum Operator{
    VIP,
    ONE,
    TMOBILE
}

class PhoneContact extends Contact{
    private String phone;
    private Operator operator;
    public PhoneContact(String date, String phone) {
        super(date);
        this.phone=phone;
    }

    public Operator getOperator(){
        if(phone.startsWith("070") || phone.startsWith("071") || phone.startsWith("072") ){
            return Operator.TMOBILE;
        }
        else if(phone.startsWith("075") || phone.startsWith("076")){
            return Operator.ONE;
        }else{
            return Operator.VIP;
        }
    }

    public String getPhone() {
        return phone;
    }

    @Override
    public String getType() {
        return "Phone";
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\"")
                .append(phone)
                .append("\"");
        return sb.toString();
    }

}

class Student{
    private String firstName;
    private String lastName;
    private String city;
    private int age;
    private long index;
    Contact[] contacts;
    private int numPhoneContacts;
    private int numEmailContacts;
    private int numContacts;

    public int getNumContacts() {
        return numContacts;
    }

    public Student(String firstName, String lastName, String city, int age, long index) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.city = city;
        this.age = age;
        this.index = index;
        numEmailContacts=0;
        numPhoneContacts=0;
        numContacts=0;
    }

    public void addEmailContact(String date, String email){
        Contact[] tmp = new Contact[numContacts+1];
        for(int i=0;i<numContacts;i++){
            tmp[i]=contacts[i];
        }
        tmp[numContacts++]=new EmailContact(date,email);
        contacts=tmp;
        numEmailContacts++;
    }
    public void addPhoneContact(String date, String phone){
        Contact[] tmp = new Contact[numContacts+1];
        for(int i=0;i<numContacts;i++){
            tmp[i]=contacts[i];
        }
        tmp[numContacts++]=new PhoneContact(date,phone);
        contacts=tmp;
        numPhoneContacts++;

    }

    public Contact[] getEmailContacts(){
        Contact[] tmp = new EmailContact[numEmailContacts];
        int counter=0;
        for(Contact contact : contacts){
            if(contact.getType().equals("Email")){
                tmp[counter++]=contact;
            }
        }
        return tmp;
    }

    public Contact[] getPhoneContacts(){
        Contact[] tmp = new PhoneContact[numPhoneContacts];
        int counter=0;
        for(Contact contact : contacts){
            if(contact.getType().equals("Phone")){
                tmp[counter++]=contact;
            }
        }
        return tmp;
    }

    public String getCity() {
        return city;
    }

    public String getFullName() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(firstName);
        stringBuilder.append(" ");
        stringBuilder.append(lastName);
        return stringBuilder.toString();
    }

    public long getIndex() {
        return index;
    }

    public Contact getLatestContact(){
        int ind=0;
        for(int i=1;i<contacts.length;i++){
            if(contacts[i].isNewerThan(contacts[ind])){
                ind = i;
            }
        }
        return contacts[ind];
    }

    @Override
    public String toString() {
       StringBuilder sb = new StringBuilder();
        sb.append("{\"ime\":\"")
                .append(firstName)
                .append("\", \"prezime\":\"")
                .append(lastName)
                .append("\", \"vozrast\":")
                .append(age).append(", \"grad\":\"")
                .append(city)
                .append("\", \"indeks\":")
                .append(index).append(", \"telefonskiKontakti\":[");
        PhoneContact[] pc = (PhoneContact[]) getPhoneContacts();
        for (int i=0;i<numPhoneContacts;i++) {
            sb.append(pc[i]);
            if(i!=numPhoneContacts-1){
                sb.append(", ");
            }
        }

        sb.append("], ");
        sb.append("\"emailKontakti\":[");
        EmailContact[] em = (EmailContact[]) getEmailContacts();
        for (int j=0;j<numEmailContacts;j++) {
                   sb.append(em[j]);
                   if(j!=numEmailContacts-1){
                       sb.append(", ");
                   }
        }
        sb.append("]}");
        return sb.toString();
    }
}

class Faculty{
    private String name;
    private Student[] students;

    public Faculty(String name, Student[] students) {
        this.name = name;
        this.students = students;
    }
    public int countStudentsFromCity(String cityName){
        int counter=0;
        for (Student student : students) {
            if (cityName.toLowerCase().equals(student.getCity().toLowerCase())) {
                counter++;
            }
        }
        return counter;
    }

    public Student getStudent(long index){
        int ind=0;
        for(int i=0;i<students.length;i++){
            if(students[i].getIndex() == index){
                ind=i;
                break;
            }
        }
        return students[ind];
    }
    public double getAverageNumberOfContacts(){
        double sum=0.0;
        for(Student s : students){
            sum+=s.getNumContacts();
        }
        return sum/students.length;
    }

    public Student getStudentWithMostContacts(){
        int max=students[0].getNumContacts();
        int ind=0;
        for(int i=1;i<students.length;i++){
            if(students[i].getNumContacts()>max){
                ind=i;
                max=students[i].getNumContacts();
            }else if(students[i].getNumContacts()==max){
                if(students[i].getIndex()>students[ind].getIndex()){
                    ind=i;
                    max=students[i].getNumContacts();
                }
            }
        }
        return students[ind];
    }

    @Override
    //{"fakultet":"FINKI", "studenti":
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"fakultet\":\"")
                .append(name)
                .append("\", \"studenti\":")
                .append("[");
//        for(Student s : students){
//            sb.append(s.toString());
//        }
        for (int i=0; i<students.length-1; i++) {
            sb.append(students[i].toString()).append(", ");
        }
        sb.append(students[students.length-1].toString()).append("]}");
        return sb.toString();
    }
}

public class ContactsTester {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int tests = scanner.nextInt();
        Faculty faculty = null;

        int rvalue = 0;
        long rindex = -1;

        DecimalFormat df = new DecimalFormat("0.00");

        for (int t = 0; t < tests; t++) {

            rvalue++;
            String operation = scanner.next();

            switch (operation) {
                case "CREATE_FACULTY": {
                    String name = scanner.nextLine().trim();
                    int N = scanner.nextInt();

                    Student[] students = new Student[N];

                    for (int i = 0; i < N; i++) {
                        rvalue++;

                        String firstName = scanner.next();
                        String lastName = scanner.next();
                        String city = scanner.next();
                        int age = scanner.nextInt();
                        long index = scanner.nextLong();

                        if ((rindex == -1) || (rvalue % 13 == 0))
                            rindex = index;

                        Student student = new Student(firstName, lastName, city,
                                age, index);
                        students[i] = student;
                    }

                    faculty = new Faculty(name, students);
                    break;
                }

                case "ADD_EMAIL_CONTACT": {
                    long index = scanner.nextInt();
                    String date = scanner.next();
                    String email = scanner.next();

                    rvalue++;

                    if ((rindex == -1) || (rvalue % 3 == 0))
                        rindex = index;

                    faculty.getStudent(index).addEmailContact(date, email);
                    break;
                }

                case "ADD_PHONE_CONTACT": {
                    long index = scanner.nextInt();
                    String date = scanner.next();
                    String phone = scanner.next();

                    rvalue++;

                    if ((rindex == -1) || (rvalue % 3 == 0))
                        rindex = index;

                    faculty.getStudent(index).addPhoneContact(date, phone);
                    break;
                }

                case "CHECK_SIMPLE": {
                    System.out.println("Average number of contacts: "
                            + df.format(faculty.getAverageNumberOfContacts()));

                    rvalue++;

                    String city = faculty.getStudent(rindex).getCity();
                    System.out.println("Number of students from " + city + ": "
                            + faculty.countStudentsFromCity(city));

                    break;
                }

                case "CHECK_DATES": {

                    rvalue++;

                    System.out.print("Latest contact: ");
                    Contact latestContact = faculty.getStudent(rindex)
                            .getLatestContact();
                    if (latestContact.getType().equals("Email"))
                        System.out.println(((EmailContact) latestContact)
                                .getEmail());
                    if (latestContact.getType().equals("Phone"))
                        System.out.println(((PhoneContact) latestContact)
                                .getPhone()
                                + " ("
                                + ((PhoneContact) latestContact).getOperator()
                                .toString() + ")");

                    if (faculty.getStudent(rindex).getEmailContacts().length > 0
                            && faculty.getStudent(rindex).getPhoneContacts().length > 0) {
                        System.out.print("Number of email and phone contacts: ");
                        System.out
                                .println(faculty.getStudent(rindex)
                                        .getEmailContacts().length
                                        + " "
                                        + faculty.getStudent(rindex)
                                        .getPhoneContacts().length);

                        System.out.print("Comparing dates: ");
                        int posEmail = rvalue
                                % faculty.getStudent(rindex).getEmailContacts().length;
                        int posPhone = rvalue
                                % faculty.getStudent(rindex).getPhoneContacts().length;

                    System.out.println(faculty.getStudent(rindex)
                               .getEmailContacts()[posEmail].isNewerThan(faculty
                             .getStudent(rindex).getPhoneContacts()[posPhone]));
                    }

                    break;
                }

                case "PRINT_FACULTY_METHODS": {
                    System.out.println("Faculty: " + faculty.toString());
                    System.out.println("Student with most contacts: "
                            + faculty.getStudentWithMostContacts().toString());
                    break;
                }

            }

        }

        scanner.close();
    }
}

