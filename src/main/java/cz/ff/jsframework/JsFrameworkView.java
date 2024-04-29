package cz.ff.jsframework;

import java.time.LocalDate;

// I'd like to have this record inside the Repository class
// but Hibernate has some problem with combination of JPQL and projection to a nested record
public record JsFrameworkView(String name, String version, LocalDate deprecationDate, int hypeLevel) {}