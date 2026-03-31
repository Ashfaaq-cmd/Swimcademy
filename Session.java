
public class Session {
private int id;
private String title;
private String coach;
private String sessionDate;
private String sessionTime;
private int capacity;
private int slotsAvailable;
private String level;
private double price;

public Session() {
	// TODO Auto-generated constructor stub
}
public int getId() {
	return id;
}

public void setId(int id) {
	this.id = id;
}

public String getTitle() {
	return title;
}

public void setTitle(String title) {
	this.title = title;
}

public String getCoach() {
	return coach;
}

public void setCoach(String coach) {
	this.coach = coach;
}

public String getSessionDate() {
	return sessionDate;
}

public void setSessionDate(String sessionDate) {
	this.sessionDate = sessionDate;
}

public String getSessionTime() {
	return sessionTime;
}

public void setSessionTime(String sessionTime) {
	this.sessionTime = sessionTime;
}

public int getCapacity() {
	return capacity;
}

public void setCapacity(int capacity) {
	this.capacity = capacity;
}

public int getSlotsAvailable() {
	return slotsAvailable;
}

public void setSlotsAvailable(int slotsAvailable) {
	this.slotsAvailable = slotsAvailable;
}

public String getLevel() {
	return level;
}

public void setLevel(String level) {
	this.level = level;
}

public double getPrice() {
	return price;
}

public void setPrice(double price) {
	this.price = price;
}

public Session(int id, String title, String coach, String sessionDate, String sessionTime, int capacity,
		int slotsAvailable, String level, double price) {
	super();
	this.id = id;
	this.title = title;
	this.coach = coach;
	this.sessionDate = sessionDate;
	this.sessionTime = sessionTime;
	this.capacity = capacity;
	this.slotsAvailable = slotsAvailable;
	this.level = level;
	this.price = price;
}


@Override
public String toString() {
    return title + " (" + level + ") - " + sessionDate + " " + sessionTime;
}
}
