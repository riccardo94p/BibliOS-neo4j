package main.java.models;

import java.util.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Book {
	private int bookId;
	private String title;
	private String author;
	private Double avgRating;
}